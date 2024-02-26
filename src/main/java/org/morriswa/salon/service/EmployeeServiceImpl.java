package org.morriswa.salon.service;

import org.morriswa.salon.dao.EmployeeDao;
import org.morriswa.salon.exception.BadRequestException;
import org.morriswa.salon.model.Appointment;
import org.morriswa.salon.model.AppointmentRequest;
import org.morriswa.salon.model.ProvidedService;
import org.morriswa.salon.model.UserAccount;
import org.morriswa.salon.utility.AmazonS3Client;
import org.morriswa.salon.utility.ImageScaleUtil;
import org.morriswa.salon.validation.ImageValidator;
import org.morriswa.salon.validation.ProvidedServiceValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final AmazonS3Client s3;
    private final ImageScaleUtil imageScale;
    private final EmployeeDao employeeDao;
    private final SchedulingService schedule;

    @Autowired
    public EmployeeServiceImpl(AmazonS3Client s3, ImageScaleUtil imageScale, EmployeeDao employeeDao, SchedulingService schedule) {
        this.s3 = s3;
        this.imageScale = imageScale;
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
        return schedule.retrieveEmployeeSchedule(principal.getUserId(), untilDate);
    }

    @Override
    public void cancelAppointment(UserAccount principal, Long appointmentId) {
        // confirm appointment is able to be canceled

        // and cancel appointment
        schedule.employeeCancelsAppointment(principal.getUserId(), appointmentId);
    }

    @Override
    public void uploadProvidedServiceImage(UserAccount principal, Long serviceId, MultipartFile image) throws Exception {

        // make sure image file is correctly formatted
        ImageValidator.validateImageFormat(image);

        // ensure service belongs to authenticated user
        if (!employeeDao.serviceBelongsTo(serviceId, principal.getUserId()))
            throw new BadRequestException("You are not allowed to edit this service!");

        // scale image by 80%
        final byte[] scaledImage = imageScale.getScaledImage(image, 0.8F);

        // create resource ID for uploaded content
        final UUID newResourceId = UUID.randomUUID();

        // upload content to S3
        s3.uploadToS3(scaledImage, newResourceId.toString());

        // and add to db
        employeeDao.addContentToProvidedService(serviceId, newResourceId.toString());
    }

    @Override
    public void getProvidedServiceDetails(UserAccount principal, Long serviceId) throws Exception {

        // ensure service belongs to authenticated user
        if (!employeeDao.serviceBelongsTo(serviceId, principal.getUserId()))
            throw new BadRequestException("You are not allowed to edit this service!");

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
