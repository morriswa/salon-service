package org.morriswa.salon.dao;

import org.morriswa.salon.enumerated.ContactPreference;
import org.morriswa.salon.enumerated.Pronouns;
import org.morriswa.salon.exception.BadRequestException;
import org.morriswa.salon.model.AppointmentRequest;
import org.morriswa.salon.model.ServiceDetails;
import org.morriswa.salon.model.ProvidedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.*;

/**
 * AUTHOR: William A. Morris
 * DATE CREATED: 2024-02-01
 * PURPOSE: Implements Employee DAO to CRUD employee info
 */
@Component
public class EmployeeDaoImpl implements EmployeeDao {
     
    private final NamedParameterJdbcTemplate database;

    @Autowired
    public EmployeeDaoImpl(NamedParameterJdbcTemplate database) {
        this.database = database;
    }

    @Override
    public void createProvidedService(Long employeeId, ProvidedService createProvidedServiceRequest) {
        final var query = """
            insert into provided_service
                (employee_id, provided_service_name, default_cost, default_length)
            values
                (:employeeId, :serviceName, :cost, IFNULL(:length, DEFAULT(default_length)))""";

        final var params = new HashMap<String, Object>() {{
            put("employeeId", employeeId);
            put("serviceName", createProvidedServiceRequest.name());
            put("cost", createProvidedServiceRequest.defaultCost());
            put("length", createProvidedServiceRequest.defaultLength());
        }};

        database.update(query, params);
    }

    @Override
    public void deleteProvidedService(Long employeeId, Long serviceId) {

    }

    @Override
    public void cancelAppointment(Long employeeId, Long appointmentId) {

    }

    @Override
    public void addContentToProvidedService(Long serviceId, String contentId) {
        final var query = """
            insert into provided_service_content (service_id, content_id)
            values (:serviceId, :contentId)
            """;

        final var params = new HashMap<String, Object>(){{
            put("serviceId", serviceId);
            put("contentId", contentId);
        }};

        database.update(query, params);
    }

    @Override
    public boolean serviceBelongsTo(Long serviceId, Long userId) {
        final var query = """
            select 1 from provided_service
            where service_id = :serviceId
            and employee_id = :employeeId""";

        final var params = Map.of("serviceId", serviceId, "employeeId", userId);

        return Boolean.TRUE.equals(database.query(query, params, ResultSet::next));
    }

    @Override
    public List<String> retrieveProvidedServiceContent(Long userId, Long serviceId) {

        final var query = """
            select content_id from provided_service_content
            where service_id = :serviceId""";

        final var params = Map.of("serviceId", serviceId);

        return database.query(query, params, rs -> {
            List<String> response = new ArrayList<>();
            while(rs.next())
                response.add(rs.getString("content_id"));
            return response;
        });
    }

    @Override
    public ServiceDetails retrieveProvidedServiceDetails(Long serviceId) throws Exception {
        final var query = """
            select *
            from provided_service ps
            left join contact_info emp on ps.employee_id=emp.user_id
            where service_id=:serviceId""";

        final var params = new HashMap<String, Object>(){{
            put("serviceId", serviceId);
        }};

        final Optional<ServiceDetails> providedService = database.query(query, params, rs -> {
             if (rs.next()) {
                 final var employeeInfo = new ServiceDetails.EmployeeInfo(
                         rs.getLong("employee_id"),
                         rs.getString("first_name"),
                         rs.getString("last_name"),
                         Pronouns.getPronounStr(rs.getString("pronouns")),
                         rs.getString("phone_num"),
                         rs.getString("email"),
                         ContactPreference.getEnum(rs.getString("contact_pref")).description
                 );

                 return Optional.of(new ServiceDetails(
                         rs.getLong("service_id"),
                         rs.getString("provided_service_name"),
                         rs.getBigDecimal("default_cost"),
                         rs.getInt("default_length") * 15,
                         employeeInfo
                 ));
             }

            return Optional.empty();
        });

        return providedService.orElseThrow(()->new BadRequestException("Could not find provided service!"));
    }

    @Override
    public List<ProvidedService> retrieveAllProvidedServices(Long employeeId) {
        final var query = """
            select *
            from provided_service
            where employee_id=:employeeId
            order by offered desc""";

        final var params = new HashMap<String, Object>(){{
            put("employeeId", employeeId);
        }};

        return database.query(query, params, rs -> {
            var services = new ArrayList<ProvidedService>();

            while(rs.next()) services.add(new ProvidedService(
                    rs.getLong("service_id"),
                    rs.getBigDecimal("default_cost"),
                    rs.getInt("default_length") * 15,
                    rs.getString("provided_service_name")
            ));

            return services;
        });
    }

    @Override
    public void updateAppointmentDetails(Long employeeId, Long appointmentId, AppointmentRequest request) {
        final var query = """
            update appointment
                set actual_amount = IFNULL(:newAmount, actual_amount)
            where
                    employee_id = :employeeId
                and
                    appointment_id = :appointmentId""";

        final var params = new HashMap<String, Object>(){{
            put("newAmount", request.cost());
            put("employeeId", employeeId);
            put("appointmentId", appointmentId);
        }};

        database.update(query, params);
    }
}
