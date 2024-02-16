package org.morriswa.salon.service;

import org.morriswa.salon.dao.ClientDao;
import org.morriswa.salon.model.Appointment;
import org.morriswa.salon.model.AppointmentOpening;
import org.morriswa.salon.model.AppointmentRequest;
import org.morriswa.salon.model.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {


    private final ClientDao clientDao;
    private final SchedulingService schedule;

    @Autowired
    public ClientServiceImpl(ClientDao clientDao, SchedulingService schedule) {
        this.clientDao = clientDao;
        this.schedule = schedule;
    }

    @Override
    public void bookAppointment(UserAccount principal, AppointmentRequest request) throws Exception {
        schedule.bookAppointment(principal.getUserId(), request);
    }

    @Override
    public void cancelAppointment(UserAccount principal, Long appointmentId) {
        schedule.clientCancelsAppointment(principal.getUserId(), appointmentId);
    }

    @Override
    public List<Appointment> retrieveScheduledAppointments(UserAccount principal) {
        return clientDao.retrieveScheduledAppointments(principal.getUserId());
    }

    @Override
    public List<Appointment> retrieveUnpaidAppointments(UserAccount principal) {
        return clientDao.retrieveUnpaidAppointments(principal.getUserId());
    }

    @Override
    public List<AppointmentOpening> retrieveAppointmentOpenings(UserAccount principal, AppointmentRequest request) throws Exception {
        return schedule.retrieveAppointmentOpenings(request);
    }
}
