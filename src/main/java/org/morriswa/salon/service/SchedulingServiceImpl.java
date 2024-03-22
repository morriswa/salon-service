package org.morriswa.salon.service;

import org.morriswa.salon.dao.ScheduleDao;
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
    public List<AppointmentOpening> retrieveAppointmentOpenings(UserAccount principal, AppointmentRequest request) throws Exception {

        // validate params to view appointment openings
        ScheduleRequestValidator.validateAppointmentOpeningsRequest(request);

        // return all generated appointment openings
        return scheduleDao.retrieveAppointmentOpenings(principal.getUserId(), request);
    }

    @Override
    public void bookAppointment(UserAccount principal, AppointmentRequest request) throws Exception {

        // validate params to book appointment
        ScheduleRequestValidator.validateBookAppointmentRequest(request);

        // store appointment in db
        scheduleDao.bookAppointment(principal.getUserId(), request);
    }

    @Override
    public void employeeReschedulesAppointment(UserAccount principal, Long appointmentId, AppointmentRequest request) throws Exception {

        // validate params to reschedule appointment
        ScheduleRequestValidator.validateRescheduleAppointmentRequest(request);

        // move appointment in db
        scheduleDao.employeeReschedulesAppointment(principal.getUserId(), appointmentId, request);
    }

    @Override
    public List<Appointment> retrieveScheduledAppointments(UserAccount principal) {

        // return all a client's appointments stored in db
        return scheduleDao.retrieveScheduledAppointments(principal.getUserId());
    }


    @Override
    public List<Appointment> retrieveEmployeeSchedule(UserAccount principal, LocalDate untilDate) {

        // return all an employee's appointments stored in db
        return scheduleDao.retrieveEmployeeSchedule(principal.getUserId(), untilDate);
    }

    @Override
    public void updateAppointmentDetails(UserAccount principal, Long appointmentId, AppointmentRequest request) throws Exception {

        // ensure that requester has edit access to appointment
        scheduleDao.checkEditAccessOrThrow(principal.getUserId(), appointmentId);

        // and update details
        scheduleDao.updateAppointmentDetails(appointmentId, request);
    }

}
