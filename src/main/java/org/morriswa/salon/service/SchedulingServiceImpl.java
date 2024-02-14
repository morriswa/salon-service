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
    public List<AppointmentOpening> seeAvailableTimes(AppointmentRequest request) throws Exception {

        ScheduleRequestValidator.validateAvailableTimesRequest(request);

        return scheduleDao.checkAvailableTimes(request);
    }

    @Override
    public void scheduleAppointment(Long clientId, AppointmentRequest request) throws Exception {

        ScheduleRequestValidator.validateCreateAppointmentRequest(request);

        scheduleDao.registerAppointment(clientId, request);
    }

    @Override
    public void employeeCancelsAppointment(Long employeeId, Long appointmentId) {

    }

    @Override
    public void clientCancelsAppointment(Long clientId, Long appointmentId) {

    }

    @Override
    public void employeeReschedulesAppointment(Long employeeId, Long appointmentId, AppointmentRequest request) throws Exception {

        ScheduleRequestValidator.validateMoveAppointmentRequest(request);

        scheduleDao.employeeMovesAppointment(employeeId, appointmentId, request);
    }
}
