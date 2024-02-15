package org.morriswa.salon.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.morriswa.salon.dao.EmployeeDao;
import org.morriswa.salon.model.Appointment;
import org.morriswa.salon.model.AppointmentRequest;
import org.morriswa.salon.model.ProvidedService;
import org.morriswa.salon.model.UserAccount;
import org.morriswa.salon.utility.AmazonS3Client;
import org.morriswa.salon.validation.ProvidedServiceValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final AmazonS3Client s3;
    private final EmployeeDao employeeDao;
    private final SchedulingService schedule;

    @Autowired
    public EmployeeServiceImpl(AmazonS3Client s3, EmployeeDao employeeDao, SchedulingService schedule) {
        this.s3 = s3;
        this.employeeDao = employeeDao;
        this.schedule = schedule;
    }

    @Override
    public void createProvidedService(UserAccount principal, ProvidedService createProvidedServiceRequest) throws Exception {

        // validate new provided service
        ProvidedServiceValidator.validateCreateProvidedServiceRequestOrThrow(createProvidedServiceRequest);

        // and execute database operation to save new provided service
        employeeDao.createProvidedService(principal.getUserId(), createProvidedServiceRequest);
    }

    @Override
    public void deleteProvidedService(UserAccount principal, Long serviceId) {

        throw new UnsupportedOperationException("Unimplemented method 'deleteProvidedService'");

        // execute db operation to delete an employees provided service
//        employeeDao.deleteProvidedService(principal.getUserId(), serviceId);
    }

    @Override
    public List<Appointment> retrieveSchedule(UserAccount principal, LocalDate untilDate) {
        // retrieve employees schedule from the database
        return employeeDao.retrieveSchedule(principal.getUserId(), untilDate);
    }

    @Override
    public void cancelAppointment(UserAccount principal, Long appointmentId) {
        // confirm appointment is able to be canceled

        // and cancel appointment
        schedule.employeeCancelsAppointment(principal.getUserId(), appointmentId);
    }

    @Override
    public void uploadProvidedServiceImage(UserAccount principal, Long serviceId, MultipartFile file) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'uploadProvidedServiceImage'");
    }

    @Override
    public void getProvidedServiceDetails(UserAccount principal, Long serviceId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getProvidedServiceDetails'");
    }

    @Override
    public List<ProvidedService> retrieveAllProvidedServices(UserAccount principal) {
        return employeeDao.retrieveAllProvidedServices(principal.getUserId());
    }

    @Override
    public void rescheduleAppointment(UserAccount principal, Long appointmentId, AppointmentRequest request) throws Exception {
         schedule.employeeReschedulesAppointment(principal.getUserId(), appointmentId, request);
    }

    @Override
    public void updateAppointmentDetails(UserAccount principal, Long appointmentId, AppointmentRequest request) {
        employeeDao.updateAppointmentDetails(principal.getUserId(), appointmentId, request);
    }

}
