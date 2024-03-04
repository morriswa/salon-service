package org.morriswa.salon.dao;

import org.morriswa.salon.enumerated.AppointmentStatus;
import org.morriswa.salon.enumerated.ContactPreference;
import org.morriswa.salon.exception.BadRequestException;
import org.morriswa.salon.model.Appointment;
import org.morriswa.salon.model.AppointmentOpening;
import org.morriswa.salon.model.AppointmentRequest;
import org.morriswa.salon.model.ProvidedService;
import org.morriswa.salon.utility.TimeZoneUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * AUTHOR: William A. Morris, Makenna Loewenherz
 * DATE CREATED: 2024-02-01
 * PURPOSE: Implements Schedule DAO to CRUD the salon's schedule
 */
@Component
public class ScheduleDaoImpl  implements ScheduleDao{

    // this app was designed to support one salon
    // these variables define important salon rules
    private final ZoneId SALON_TIME_ZONE;
    private final LocalTime SALON_OPEN;
    private final LocalTime SALON_CLOSE;
    private final ZoneId UTC = ZoneId.of("+00:00");

    private final NamedParameterJdbcTemplate database;
    private final Logger log = LoggerFactory.getLogger(ScheduleDaoImpl.class);


    @Autowired
    public ScheduleDaoImpl(
            TimeZoneUtil time,
            NamedParameterJdbcTemplate database) {
        // initialize time settings
        SALON_TIME_ZONE = time.getZoneOfSalon();
        SALON_OPEN = time.getSalonOpen();
        SALON_CLOSE = time.getSalonClose();

        this.database = database;
    }


    private ProvidedService retrieveProvidedService(Long serviceId) throws BadRequestException {

        final var queryLength = """
            select *
            from provided_service
            where service_id=:serviceId
            """;

        final var params = new HashMap<String, Object>(){{
            put("serviceId", serviceId);
        }};

        try {
            return database.query(queryLength, params, rs -> {
                if (rs.next()) return new ProvidedService(
                        rs.getLong("service_id"),
                        rs.getBigDecimal("default_cost"),
                        rs.getInt("default_length"),
                        rs.getString("provided_service_name"));
                else throw new SQLException("Service not found");
            });
        } catch (Exception e) {
            throw new BadRequestException(e.getCause().getMessage());
        }

    }

    @Override
    public List<AppointmentOpening> retrieveAppointmentOpenings(AppointmentRequest request) throws BadRequestException {

        // get beginning of day in salon's timezone
        final var salonOpen = request.searchDate().toInstant()
                .atZone(SALON_TIME_ZONE)
                .withHour(SALON_OPEN.getHour())
                .withMinute(SALON_OPEN.getMinute())
                .truncatedTo(ChronoUnit.MINUTES)
                .toInstant().atZone(UTC);

        // get end of day in salon's timezone, mark as end of search
        final var salonClose = request.searchDate().toInstant()
                .atZone(SALON_TIME_ZONE)
                .withHour(SALON_CLOSE.getHour())
                .withMinute(SALON_CLOSE.getMinute())
                .truncatedTo(ChronoUnit.MINUTES)
                .toInstant().atZone(UTC);

        // retrieve the current time
        final var currentTime = Instant.now()
                .atZone(UTC);

        // if the current time is within the day to search,
        // use current time as start search
        // else use salon open of day as start of search
        final var startSearch = salonOpen.isAfter(currentTime)?
                salonOpen : currentTime;

        // defn search query
        final var query = """
            select
                appointment_time,
                length
            from appointment
            where employee_id=:employeeId
            and
                appointment_time between :startSearch and :endSearch
            order by appointment_time
            """;

        // inject params
        final var params = new HashMap<String, Object>(){{
            put("employeeId", request.employeeId());
            put("startSearch", startSearch);
            put("endSearch", salonClose);
        }};

        // retrieve all preexisting appointments
        List<AppointmentOpening> preexistingAppointments = database.query(query, params, rs -> {
            var appts = new ArrayList<AppointmentOpening>();

            while (rs.next()) appts.add(new AppointmentOpening(
                // retrieve every appointment's time in current time zone
                rs.getTimestamp("appointment_time")
                        .toInstant().atZone(UTC),
                // convert length to minutes
                rs.getInt("length") * 15
            ));

            return appts;
        });
        // this list should never be null
        assert preexistingAppointments != null;

        // retrieve the length of requested service type
        int appointmentLength = retrieveProvidedService(request.serviceId()).defaultLength();

        // create list to store available appointment times
        var availableTimes = new ArrayList<AppointmentOpening>();

        // record first available appointment time
        // (at least one hour ahead of the present to avoid last minute bookings)
        final var firstAvailableAppointment = Instant.now()
                .atZone(UTC)
                .plusMinutes(61).truncatedTo(ChronoUnit.HOURS);

        // define var to keep track of time scanner is currently at
        ZonedDateTime scanner = null;

        // if there are no preexisting appointments on requested day
        if (preexistingAppointments.isEmpty()) {

            // start scanning for available times at first available appointment
            // or salon open time if search is in the future
            scanner = currentTime.isAfter(salonOpen)?
                    firstAvailableAppointment :
                    salonOpen;

            // find the length between current time and end of day
            int timeTilEndofDay =
                    (int) Duration.between(scanner, salonClose).toMinutes();

            // determine how many slots are available in current day
            int slots = (timeTilEndofDay / 15) - appointmentLength + 1;
            for (int i = 0; i < slots; i++) {

                // add every open slot to available times
                availableTimes.add(new AppointmentOpening(
                        scanner,
                        appointmentLength * 15)
                );

                // and increment by slot length (15 minutes)
                scanner = scanner.plusMinutes(15);
            }
        // if there ARE existing appointments on requested day...
        // iterate through all existing appointments
        } else for (int existingAppointmentIdx = 0;
                    existingAppointmentIdx < preexistingAppointments.size();
                    existingAppointmentIdx++) {

            // if scanner has not been initialized,
            if (scanner == null)
                // start scanning for available times at first available appointment
                // or salon open time if search day is in the future
                scanner = currentTime.isAfter(salonOpen)?
                        firstAvailableAppointment :
                        salonOpen;

            // find the length in minutes between current time and next taken appointment
            Integer timeTilNextAppointment = (int)
                    Duration.between(scanner, preexistingAppointments.get(existingAppointmentIdx).time()).toMinutes();

            // find the length between current time and end of day
            int timeTilEndofDay =
                    (int) Duration.between(scanner, salonClose).toMinutes();

            // if current slot will not be long enough to fit requested appointment type
            if (timeTilNextAppointment < (appointmentLength * 15)) {
                // move scanner to end of next appointment
                scanner = preexistingAppointments.get(existingAppointmentIdx)
                        .time()
                        .plusMinutes(preexistingAppointments
                                .get(existingAppointmentIdx)
                                .length());

            // if current slot will be long enough
            // and there are more appointments happening before the end of day
            } else if (timeTilNextAppointment <= timeTilEndofDay) {

                // find number of open slots
                int slots = (timeTilNextAppointment / 15) - appointmentLength + 1;
                for (int i = 0; i < slots; i++) {

                    // add every open slot to available times
                    availableTimes.add(new AppointmentOpening(
                            scanner,
                            appointmentLength * 15)
                    );

                    // and increment by slot length (15 minutes)
                    scanner = scanner.plusMinutes(15);
                }

                // move scanner to end of next appointment
                scanner = preexistingAppointments.get(existingAppointmentIdx).time()
                        .plusMinutes(preexistingAppointments.get(existingAppointmentIdx).length());
            }

            // if there are no more appointments on the schedule,
            if (existingAppointmentIdx + 1 == preexistingAppointments.size()) {

                // update time til end of day
                timeTilEndofDay =
                        (int) Duration.between(scanner, salonClose).toMinutes();

                // record open slot until days end
                int slots = (timeTilEndofDay / 15) - appointmentLength + 1;
                for (int i = 0; i < slots; i++) {

                    // add every open slot to available times
                    availableTimes.add(new AppointmentOpening(
                            scanner,
                            appointmentLength * 15)
                    );

                    // and increment by slot length (15 minutes)
                    scanner = scanner.plusMinutes(15);
                }
            }

        }

        return availableTimes;

    }

    @Override
    public void bookAppointment(Long clientId, AppointmentRequest request) throws BadRequestException {

        ProvidedService serviceToSchedule = retrieveProvidedService(request.serviceId());

        // get beginning of day in salon's timezone
        final var salonOpen = request.time().toInstant()
                .atZone(SALON_TIME_ZONE)
                .withHour(SALON_OPEN.getHour())
                .withMinute(SALON_OPEN.getMinute())
                .truncatedTo(ChronoUnit.MINUTES)
                .toInstant().atZone(UTC);

        // get end of day in salon's timezone, mark as end of search
        final var salonClose = request.time().toInstant()
                .atZone(SALON_TIME_ZONE)
                .withHour(SALON_CLOSE.getHour())
                .withMinute(SALON_CLOSE.getMinute())
                .truncatedTo(ChronoUnit.MINUTES)
                .toInstant().atZone(UTC);

        if (request.time().plusMinutes(
                serviceToSchedule.defaultLength() * 15L
        ).isAfter(salonClose))
            throw new BadRequestException("Appointments should not end after salon close!");
        else if (request.time().isBefore(salonOpen))
            throw new BadRequestException("Appointments should not start before salon opens!");

        final var query = """
            select
                appointment_time,
                length
            from appointment
            where employee_id=:employeeId
            and
                appointment_time between :startSearch and :endSearch
            """;

        final var startSearch = request.time().truncatedTo(ChronoUnit.MINUTES);
        final var stopSearch = request.time().truncatedTo(ChronoUnit.MINUTES)
                .plusMinutes(serviceToSchedule.defaultLength() * 15L).minusMinutes(1);
        final var params2 = new HashMap<String, Object>(){{
            put("employeeId", request.employeeId());
            put("startSearch", startSearch);
            put("endSearch", stopSearch);
        }};

        boolean allowed = Boolean.TRUE.equals(database.query(query, params2, rs -> !rs.next()));
        if (!allowed) throw new BadRequestException("Time is unavailable!");


        final var addQuery = """
            insert into appointment
                (client_id, employee_id, appointment_time,
                    service_id, actual_amount, date_due, length)
            values
                (:clientId, :employeeId, :appointmentTime,
                    :serviceId, :actualAmount, :due, :length)
            """;

        final var addParams = new HashMap<String, Object>(){{
            put("clientId", clientId);
            put("employeeId", request.employeeId());
            put("appointmentTime", request.time().truncatedTo(ChronoUnit.MINUTES));
            put("serviceId", request.serviceId());
            put("actualAmount", serviceToSchedule.defaultCost());
            put("due", request.time().plusWeeks(2).truncatedTo(ChronoUnit.MINUTES));
            put("length", serviceToSchedule.defaultLength());
        }};

        database.update(addQuery, addParams);
    }

    @Override
    public void employeeReschedulesAppointment(Long employeeId, Long appointmentId, AppointmentRequest request) throws BadRequestException {

        final var newAppointmentTime = request.time().truncatedTo(ChronoUnit.MINUTES);
        final var newAppointmentLength = request.length();

        // get beginning of day in salon's timezone
        final var salonOpen = request.time().toInstant()
                .atZone(SALON_TIME_ZONE)
                .withHour(SALON_OPEN.getHour())
                .withMinute(SALON_OPEN.getMinute())
                .truncatedTo(ChronoUnit.MINUTES);

        // get end of day in salon's timezone, mark as end of search
        final var salonClose = request.time().toInstant()
                .atZone(SALON_TIME_ZONE)
                .withHour(SALON_CLOSE.getHour())
                .withMinute(SALON_CLOSE.getMinute())
                .truncatedTo(ChronoUnit.MINUTES);

        if (newAppointmentTime.plusMinutes(newAppointmentLength * 15L)
                .isAfter(salonClose)) throw new BadRequestException("Appointments should not end after salon close!");
        else if (newAppointmentTime.isBefore(salonOpen))
            throw new BadRequestException("Appointments should not start before salon opens!");

        final var query = """
            select
                appointment_time,
                length
            from appointment
            where employee_id = :employeeId
            and   appointment_id != :appointmentId
            and
                appointment_time between :startSearch and :endSearch
            """;

        final var stopSearch = newAppointmentTime.plusMinutes(newAppointmentLength * 15L).minusMinutes(1);
        final var params2 = new HashMap<String, Object>(){{
            put("employeeId", employeeId);
            put("appointmentId", appointmentId);
            put("startSearch", newAppointmentTime);
            put("endSearch", stopSearch);
        }};

        boolean allowed = Boolean.TRUE.equals(database.query(query, params2, rs -> !rs.next()));
        if (!allowed) throw new BadRequestException("Could not move appointment because time is unavailable!");

        final var moveAptQuery = """
            update appointment set
                appointment_time = :appointmentTime,
                date_due = :dueDate,
                length = :length
            where appointment_id = :appointmentId
            """;

        final var moveAptParams = new HashMap<String, Object>(){{
            put("appointmentTime", newAppointmentTime);
            put("dueDate", newAppointmentTime.plusWeeks(2));
            put("length", newAppointmentLength);
            put("appointmentId", appointmentId);
        }};

        database.update(moveAptQuery, moveAptParams);
    }

    @Override
    public List<Appointment> retrieveEmployeeSchedule(Long employeeId, LocalDate untilDate) {
        final var currentTime = Instant.now();
        final var stopSearch = ZonedDateTime.of(untilDate, LocalTime.MIDNIGHT, SALON_TIME_ZONE);

        final var query = """
            select
                appt.appointment_id ,
                appt.client_id ,
                appt.appointment_time ,
                appt.service_id ,
                ps.provided_service_name ,
                appt.date_created ,
                appt.date_due ,
                appt.actual_amount ,
                appt.tip_amount ,
                appt.status ,
                appt.length ,
                ci.first_name ,
                ci.last_name ,
                ci.phone_num ,
                ci.email ,
                ci.contact_pref

            from appointment appt
            LEFT JOIN contact_info ci ON appt.client_id = ci.user_id
            LEFT JOIN provided_service ps ON appt.service_id = ps.service_id
            where ps.employee_id = :employeeId
            and
                appt.appointment_time between :startSearch and :endSearch

            ORDER BY
                appt.appointment_time
            """;

        final var params = new HashMap<String, Object>(){{
            put("employeeId", employeeId);
            put("startSearch", currentTime);
            put("endSearch", stopSearch);
        }};

        return database.query(query, params, rs->{ 
            List<Appointment> result = new ArrayList<>();

            while (rs.next()) {
                final var client = new Appointment.ClientInfo(
                    rs.getLong("client_id"),
                    rs.getString("first_name"), 
                    rs.getString("last_name"),  
                    rs.getString("phone_num"),
                    rs.getString("email"), 
                    ContactPreference.getEnum(rs.getString("contact_pref")).description);

                final var service_info = new Appointment.ServiceInfo(
                    rs.getLong("service_id"),
                    rs.getString("provided_service_name"));  

                result.add(new Appointment(
                    rs.getLong("appointment_id"),
                    rs.getTimestamp("appointment_time").toInstant().atZone(UTC),
                    rs.getInt("length")*15,
                    rs.getTimestamp("date_created").toInstant().atZone(UTC),
                    rs.getTimestamp("date_due").toInstant().atZone(UTC),
                    rs.getBigDecimal("actual_amount"),
                    rs.getBigDecimal("tip_amount"),
                    AppointmentStatus.getEnum(rs.getString("status")).toString(),
                    service_info,
                    null,
                    client 
                ));
            }
            return result;
        });
    }
}
