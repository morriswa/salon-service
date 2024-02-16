package org.morriswa.salon.dao;

import org.morriswa.salon.model.Appointment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ClientDaoImpl implements ClientDao {
    private final NamedParameterJdbcTemplate database;

    @Autowired
    public ClientDaoImpl(NamedParameterJdbcTemplate database) {
        this.database = database;
    }

    @Override
    public List<Appointment> retrieveScheduledAppointments(Long userId) {
        return null;
    }

    @Override
    public List<Appointment> retrieveUnpaidAppointments(Long userId) {
        return null;
    }
}
