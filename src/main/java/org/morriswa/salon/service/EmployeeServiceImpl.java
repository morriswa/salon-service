package org.morriswa.salon.service;

import java.util.List;

import org.morriswa.salon.dao.EmployeeDao;
import org.morriswa.salon.model.Appointment;
import org.morriswa.salon.model.ProvidedService;
import org.morriswa.salon.model.UserAccount;
import org.morriswa.salon.utility.AmazonS3Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final AmazonS3Client s3;
    private final EmployeeDao employeeDao;

    @Autowired
    public EmployeeServiceImpl(AmazonS3Client s3, EmployeeDao employeeDao) {
        this.s3 = s3;
        this.employeeDao = employeeDao;
    }

    @Override
    public void createProvidedService(UserAccount principal, ProvidedService createProvidedServiceRequest) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createProvidedService'");
    }

    @Override
    public void deleteProvidedService(UserAccount principal, ProvidedService deleteProvidedServiceRequest) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteProvidedService'");
    }

    @Override
    public List<Appointment> retrieveSchedule(UserAccount principal) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'retrieveSchedule'");
    }

    @Override
    public void cancelAppointment(Appointment deleteRequest) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'cancelAppointment'");
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
    
}
