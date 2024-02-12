package org.morriswa.salon.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class ClientDaoImpl implements ClientDao {
    private final NamedParameterJdbcTemplate database;

    @Autowired
    public ClientDaoImpl(NamedParameterJdbcTemplate database) {
        this.database = database;
    }

    @Override
    public void retrieveScheduledAppointments(Long userId) {

    }

    @Override
    public void retrieveUnpaidAppointments(Long userId) {

    }
}
