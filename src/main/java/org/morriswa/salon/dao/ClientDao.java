package org.morriswa.salon.dao;

import org.morriswa.salon.model.Appointment;
import org.morriswa.salon.model.AvailableService;

import java.util.List;

public interface ClientDao {

    List<Appointment> retrieveScheduledAppointments(Long userId);

    List<Appointment> retrieveUnpaidAppointments(Long userId);

    List<AvailableService> searchAvailableService(String searchText);
}
