package org.morriswa.salon.service;

import org.morriswa.salon.dao.ScheduleDao;
import org.morriswa.salon.model.AppointmentOpening;
import org.morriswa.salon.model.AppointmentRequest;
import org.morriswa.salon.validation.ScheduleRequestValidator;
import org.springframework.stereotype.Service;

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
    public void bookAppointment(Long clientId, AppointmentRequest request) throws Exception {

        ScheduleRequestValidator.validateBookAppointmentRequest(request);

        scheduleDao.bookAppointment(clientId, request);
    }

    @Override
    public void employeeCancelsAppointment(Long employeeId, Long appointmentId) {

    }

    @Override
    public void clientCancelsAppointment(Long clientId, Long appointmentId) {

    }

    @Override
    public void employeeReschedulesAppointment(Long employeeId, Long appointmentId, AppointmentRequest request) throws Exception {

        ScheduleRequestValidator.validateRescheduleAppointmentRequest(request);

        scheduleDao.employeeReschedulesAppointment(employeeId, appointmentId, request);
    }
}
