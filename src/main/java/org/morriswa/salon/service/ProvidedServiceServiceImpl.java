package org.morriswa.salon.service;

import org.morriswa.salon.dao.ProvidedServiceDao;
import org.morriswa.salon.exception.BadRequestException;
import org.morriswa.salon.model.ProvidedService;
import org.morriswa.salon.model.ProvidedServiceDetails;
import org.morriswa.salon.model.ProvidedServiceProfile;
import org.morriswa.salon.model.UserAccount;
import org.morriswa.salon.utility.AmazonS3Client;
import org.morriswa.salon.utility.ImageScaleUtil;
import org.morriswa.salon.validation.ImageValidator;
import org.morriswa.salon.validation.ProvidedServiceValidator;
import org.morriswa.salon.validation.StrTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ProvidedServiceServiceImpl implements ProvidedServiceService {

    private final AmazonS3Client s3;
    private final ImageScaleUtil imageScale;
    private final ProvidedServiceDao providedServiceDao;

    @Autowired
    public ProvidedServiceServiceImpl(AmazonS3Client s3, ImageScaleUtil imageScale, ProvidedServiceDao providedServiceDao) {
        this.s3 = s3;
        this.imageScale = imageScale;
        this.providedServiceDao = providedServiceDao;
    }
    @Override
    public void createProvidedService(UserAccount principal, ProvidedService createProvidedServiceRequest) throws Exception {

        // validate new provided service
        ProvidedServiceValidator.validateCreateProvidedServiceRequestOrThrow(createProvidedServiceRequest);

        // and execute database operation to save new provided service
        providedServiceDao.createProvidedService(principal.getUserId(), createProvidedServiceRequest);
    }

    @Override
    public void deleteProvidedService(UserAccount principal, Long serviceId) {

        throw new UnsupportedOperationException("Unimplemented method 'deleteProvidedService'");

        // execute db operation to delete an employees provided service
//        employeeDao.deleteProvidedService(principal.getUserId(), serviceId);
    }

    @Override
    public List<ProvidedService> retrieveEmployeesServices(Long employeeId) {

        // return all stored services from db
        return providedServiceDao.retrieveEmployeesServices(employeeId);
    }

    @Override
    public ProvidedServiceProfile retrieveServiceProfile(Long serviceId) throws Exception {

        // retrieve all stored details about a service from db
        var providedService = providedServiceDao.retrieveServiceDetails(serviceId);

        // retrieve all stored content ids
        var contentIds = providedServiceDao.retrieveServiceContent(serviceId);

        // use s3 client to generate content URLs for provided content ids
        List<URL> contentUrls = new ArrayList<>();
        for (var id : contentIds) {
            final var url = s3.getSignedObjectUrl(id, 30);
            contentUrls.add(url);
        }

        // build and return complete provided service profile
        return new ProvidedServiceProfile(providedService, contentUrls);
    }

    @Override
    public void uploadProvidedServiceImage(UserAccount principal, Long serviceId, MultipartFile image) throws Exception {

        // make sure image file is correctly formatted
        ImageValidator.validateUploadedImage(image);

        // ensure service belongs to authenticated user
        if (!providedServiceDao.serviceBelongsTo(serviceId, principal.getUserId()))
            throw new BadRequestException("You are not allowed to edit this service!");

        // scale image by 80%
        final var scaledImage = imageScale.getScaledImage(image, 0.8F);

        // create resource ID for uploaded content
        final UUID newResourceId = UUID.randomUUID();

        // upload content to S3
        s3.uploadToS3(scaledImage, image.getContentType(), newResourceId.toString());

        // store
        providedServiceDao.addContentToProvidedService(serviceId, newResourceId.toString());
    }


    @Override
    public List<ProvidedServiceDetails> searchAvailableService(String searchText) {

        // if search text is valid return all matching services in db
        if (StrTools.hasValue(searchText)) return providedServiceDao.searchAvailableServices(searchText);

        // else return an empty list
        return new ArrayList<>();
    }

}
