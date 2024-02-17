package org.morriswa.salon.dao;

import org.joda.time.Instant;
import org.morriswa.salon.enumerated.AppointmentStatus;
import org.morriswa.salon.enumerated.ContactPreference;
import org.morriswa.salon.model.Appointment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class ClientDaoImpl implements ClientDao {
    private final NamedParameterJdbcTemplate database;

    private final ZoneId SALON_TIME_ZONE = ZoneId.of("-06:00");

    @Autowired
    public ClientDaoImpl(NamedParameterJdbcTemplate database) {
        this.database = database;
    }

    @Override
    public List<Appointment> retrieveScheduledAppointments(Long userId) {
        final var currentTime = Instant.now();
        final var query = """
            select 
                appt.appointment_id,
                appt.appointment_time,
                appt.length,
                appt.date_created,
                appt.date_due,
                appt.actual_amount,
                appt.tip_amount,
                appt.status,
                appt.employee_id,
                emply_info.first_name,
                emply_info.last_name,
                emply_info.phone_num,
                emply_info.email,
                emply_info.contact_pref,
                service.service_id,
                service.provided_service_name
            from appointment appt
            left join contact_info emply_info on appt.employee_id = emply_info.user_id
            left join provided_service service on appt.service_id = service.service_id
            where appt.client_id = :clientId
            and appt.appointment_time > NOW()
            order by appt.appointment_time""";

        final var param = Map.of("clientId", userId);
        
        return database.query(query, param, resultSet->{
            List<Appointment> schedule = new ArrayList<>();
            while(resultSet.next()){
                final var emply_info = new Appointment.EmployeeInfo(
                    resultSet.getLong("employee_id"),
                    resultSet.getString("first_name"),
                    resultSet.getString("last_name"),
                    resultSet.getString("phone_num"),
                    resultSet.getString("email"),
                    ContactPreference.getEnum(resultSet.getString("contact_pref")).description);

                final var serviceInfo = new Appointment.ServiceInfo(
                    resultSet.getLong("service_id"),
                    resultSet.getString("provided_service_name")
                );

                schedule.add(new Appointment(
                    resultSet.getLong("appointment_id"),
                    resultSet.getTimestamp("appointment_time").toInstant().atZone(SALON_TIME_ZONE),
                    resultSet.getInt("length")*15,
                    resultSet.getTimestamp("date_created").toInstant().atZone(SALON_TIME_ZONE),
                    resultSet.getTimestamp("date_due").toInstant().atZone(SALON_TIME_ZONE),
                    resultSet.getBigDecimal("actual_amount"),
                    resultSet.getBigDecimal("tip_amount"),
                    AppointmentStatus.getEnum(resultSet.getString("status")).toString(),
                    serviceInfo,
                    emply_info,
                    null
                ));
            }

            return schedule;
        });
    }

    @Override
    public List<Appointment> retrieveUnpaidAppointments(Long userId) {
        return null;
    }
}
