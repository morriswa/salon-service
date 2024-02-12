package org.morriswa.salon.dao;

public interface ClientDao {
    void retrieveScheduledAppointments(Long userId);

    void retrieveUnpaidAppointments(Long userId);
}
