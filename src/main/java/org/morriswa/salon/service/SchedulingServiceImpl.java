package org.morriswa.salon.service;

import org.morriswa.salon.dao.ScheduleDao;
import org.morriswa.salon.exception.BadRequestException;
import org.morriswa.salon.model.Appointment;
import org.morriswa.salon.model.AppointmentOpening;
import org.morriswa.salon.model.AppointmentRequest;
import org.morriswa.salon.model.UserAccount;
import org.morriswa.salon.validation.ScheduleRequestValidator;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SchedulingServiceImpl implements SchedulingService{

    private final ScheduleDao scheduleDao;

    public SchedulingServiceImpl(ScheduleDao scheduleDao) {
        this.scheduleDao = scheduleDao;
    }


    @Override
    public List<AppointmentOpening> retrieveAppointmentOpenings(AppointmentRequest request) throws Exception {

        ScheduleRequestValidator.validateAppointmentOpeningsRequest(request);

        return scheduleDao.retrieveAppointmentOpenings(request);
    }

    @Override
    public void bookAppointment(UserAccount principal, AppointmentRequest request) throws Exception {

        ScheduleRequestValidator.validateBookAppointmentRequest(request);

        scheduleDao.bookAppointment(principal.getUserId(), request);
    }

    @Override
    public void employeeCancelsAppointment(Long employeeId, Long appointmentId) {

    }

    @Override
    public void clientCancelsAppointment(Long clientId, Long appointmentId) {

    }

    @Override
    public void employeeReschedulesAppointment(UserAccount principal, Long appointmentId, AppointmentRequest request) throws Exception {

        ScheduleRequestValidator.validateRescheduleAppointmentRequest(request);

        scheduleDao.employeeReschedulesAppointment(principal.getUserId(), appointmentId, request);
    }

    @Override
    public List<Appointment> retrieveScheduledAppointments(UserAccount principal) {
        return scheduleDao.retrieveScheduledAppointments(principal.getUserId());
    }


    @Override
    public List<Appointment> retrieveEmployeeSchedule(UserAccount principal, LocalDate untilDate) {
        return scheduleDao.retrieveEmployeeSchedule(principal.getUserId(), untilDate);
    }

    @Override
    public void updateAppointmentDetails(UserAccount principal, Long appointmentId, AppointmentRequest request) throws BadRequestException {

        scheduleDao.checkEditAccessOrThrow(principal.getUserId(), appointmentId);

        scheduleDao.updateAppointmentDetails(appointmentId, request);
    }

}
