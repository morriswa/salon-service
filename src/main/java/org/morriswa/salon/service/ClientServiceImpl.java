package org.morriswa.salon.service;

import org.morriswa.salon.dao.ClientDao;
import org.morriswa.salon.model.AppointmentLength;
import org.morriswa.salon.model.AppointmentRequest;
import org.morriswa.salon.model.UserAccount;
import org.morriswa.salon.validation.ClientValidator;
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
    public void requestAppointment(UserAccount principal, AppointmentRequest request) throws Exception {
        schedule.scheduleAppointment(principal.getUserId(), request);
    }

    @Override
    public void cancelAppointment(UserAccount principal, Long appointmentId) {
        schedule.clientCancelsAppointment(principal.getUserId(), appointmentId);
    }

    @Override
    public void retrieveScheduledAppointments(UserAccount principal) {
        clientDao.retrieveScheduledAppointments(principal.getUserId());
    }

    @Override
    public void retrieveUnpaidAppointments(UserAccount principal) {
        clientDao.retrieveUnpaidAppointments(principal.getUserId());
    }

    @Override
    public List<AppointmentLength> seeTimes(UserAccount principal, AppointmentRequest request) throws Exception {
        return schedule.seeAvailableTimes(request);
    }
}
