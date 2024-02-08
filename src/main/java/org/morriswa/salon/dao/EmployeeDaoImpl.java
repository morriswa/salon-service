package org.morriswa.salon.dao;

import org.morriswa.salon.model.Appointment;
import org.morriswa.salon.model.ProvidedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class EmployeeDaoImpl implements EmployeeDao {
     
    private final NamedParameterJdbcTemplate database;

    @Autowired
    public EmployeeDaoImpl(NamedParameterJdbcTemplate database) {
        this.database = database;
    }

    @Override
    public void createProvidedService(Long userId, ProvidedService createProvidedServiceRequest) {

    }

    @Override
    public void deleteProvidedService(Long userId, Long serviceId) {

    }

    @Override
    public List<Appointment> retrieveSchedule(Long userId, LocalDateTime untilDate) {
        return null;
    }

    @Override
    public void cancelAppointment(Long userId, Long appointmentId) {

    }
}
