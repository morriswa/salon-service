package org.morriswa.salon.service;

import org.morriswa.salon.dao.ScheduleDao;
import org.morriswa.salon.exception.BadRequestException;
import org.morriswa.salon.model.AppointmentLength;
import org.morriswa.salon.model.AppointmentRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SchedulingServiceImpl implements SchedulingService{

    private final ScheduleDao scheduleDao;

    public SchedulingServiceImpl(ScheduleDao scheduleDao) {
        this.scheduleDao = scheduleDao;
    }


    @Override
    public List<AppointmentLength> seeAvailableTimes(Long clientId, AppointmentRequest request) throws BadRequestException {
        return scheduleDao.checkAvailableTimes(request);
    }

    @Override
    public void scheduleAppointment(Long clientId, AppointmentRequest request) throws BadRequestException {
        scheduleDao.registerAppointment(clientId, request);
    }

    @Override
    public void employeeCancelsAppointment(Long employeeId, Long appointmentId) {

    }

    @Override
    public void clientCancelsAppointment(Long clientId, Long appointmentId) {

    }
}
