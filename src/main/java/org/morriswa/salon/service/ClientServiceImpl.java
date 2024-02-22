package org.morriswa.salon.service;

import org.morriswa.salon.dao.ClientDao;
import org.morriswa.salon.exception.BadRequestException;
import org.morriswa.salon.model.*;
import org.morriswa.salon.validation.StrTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {


    private final ClientDao clientDao;
    private final SchedulingService schedule;

    private final Logger log = LoggerFactory.getLogger(ClientServiceImpl.class);

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

    @Override
    public List<AvailableService> searchAvailableService(UserAccount principal, String searchText) {

        if (StrTools.hasValue(searchText)) return clientDao.searchAvailableService(searchText);

        return new ArrayList<>();
    }

    @Override
    public AvailableService retrieveServiceDetails(UserAccount principal, Long serviceId) throws BadRequestException {
        return clientDao.retrieveServiceDetails(serviceId);
    }
}
