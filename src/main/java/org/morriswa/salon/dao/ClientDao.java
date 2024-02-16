package org.morriswa.salon.dao;

import org.morriswa.salon.model.Appointment;

import java.util.List;

public interface ClientDao {

    List<Appointment> retrieveScheduledAppointments(Long userId);

    List<Appointment> retrieveUnpaidAppointments(Long userId);
}
