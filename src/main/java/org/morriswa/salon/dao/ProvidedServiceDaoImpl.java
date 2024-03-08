package org.morriswa.salon.dao;

import org.morriswa.salon.enumerated.ContactPreference;
import org.morriswa.salon.enumerated.Pronouns;
import org.morriswa.salon.exception.BadRequestException;
import org.morriswa.salon.model.ProvidedService;
import org.morriswa.salon.model.ProvidedServiceDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.*;

@Component
public class ProvidedServiceDaoImpl implements ProvidedServiceDao{
    private final NamedParameterJdbcTemplate database;


    private final Logger log = LoggerFactory.getLogger(ProvidedServiceDaoImpl.class);
    @Autowired
    public ProvidedServiceDaoImpl(NamedParameterJdbcTemplate database) {
        this.database = database;
    }

    @Override
    public List<ProvidedServiceDetails> searchAvailableServices(String searchText) {

        var tokens = searchText.split(" ");
        var sqlTokens = String.join(" ", Arrays.stream(tokens).map(token->String.format(
                "%%%s%% *%s*", token, token
        )).toList());

        final var query = """
                SELECT *
                FROM provided_service ps
                JOIN contact_info ci ON ps.employee_id = ci.user_id
                WHERE
                    ps.offered = 'Y' AND
                    MATCH (ps.provided_service_name)
                    AGAINST ((:searchText) IN BOOLEAN MODE)
            """;

        final var params = new HashMap<String, Object>(){{
            put("searchText", sqlTokens);
        }};

        return database.query(query, params, rs -> {
            var services = new ArrayList<ProvidedServiceDetails>();

            while (rs.next()) {
                final var employeeInfo = new ProvidedServiceDetails.EmployeeInfo(
                        rs.getLong("employee_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        Pronouns.getPronounStr(rs.getString("pronouns")),
                        rs.getString("phone_num"),
                        rs.getString("email"),
                        ContactPreference.getEnum(rs.getString("contact_pref")).description
                );

                services.add(new ProvidedServiceDetails(
                        rs.getLong("service_id"),
                        rs.getBigDecimal("default_cost"),
                        rs.getInt("default_length")*15,
                        rs.getString("provided_service_name"),
                        employeeInfo
                ));
            }

            return services;
        });
    }

    @Override
    public ProvidedServiceDetails retrieveServiceDetails(Long serviceId) throws BadRequestException {

        final var query = """
                SELECT *
                FROM provided_service ps
                JOIN contact_info ci ON ps.employee_id = ci.user_id
                WHERE
                    ps.offered = 'Y' AND
                    ps.service_id = :serviceId
            """;

        final var params = new HashMap<String, Object>(){{
            put("serviceId", serviceId);
        }};

        Optional<ProvidedServiceDetails> service = database.query(query, params, rs -> {

            if (rs.next()) {
                final var employeeInfo = new ProvidedServiceDetails.EmployeeInfo(
                        rs.getLong("employee_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        Pronouns.getPronounStr(rs.getString("pronouns")),
                        rs.getString("phone_num"),
                        rs.getString("email"),
                        ContactPreference.getEnum(rs.getString("contact_pref")).description
                );

                return Optional.of(new ProvidedServiceDetails(
                        rs.getLong("service_id"),
                        rs.getBigDecimal("default_cost"),
                        rs.getInt("default_length")*15,
                        rs.getString("provided_service_name"),
                        employeeInfo
                ));
            }

            return Optional.empty();
        });

        return service.orElseThrow(()->new BadRequestException("Could not find requested service!"));
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
            put("serviceName", createProvidedServiceRequest.getName());
            put("cost", createProvidedServiceRequest.getCost());
            put("length", createProvidedServiceRequest.getLength());
        }};

        database.update(query, params);
    }

    @Override
    public void deleteProvidedService(Long employeeId, Long serviceId) {

    }

    @Override
    public List<String> retrieveServiceContent(Long serviceId) {

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
    public List<ProvidedService> retrieveEmployeesServices(Long employeeId) {
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

}
