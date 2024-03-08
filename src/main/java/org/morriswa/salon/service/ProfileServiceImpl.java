package org.morriswa.salon.service;

import org.morriswa.salon.dao.ProfileDao;
import org.morriswa.salon.exception.BadRequestException;
import org.morriswa.salon.exception.ValidationException;
import org.morriswa.salon.model.*;
import org.morriswa.salon.utility.AmazonS3Client;
import org.morriswa.salon.utility.ImageScaleUtil;
import org.morriswa.salon.validation.ImageValidator;
import org.morriswa.salon.validation.UserProfileValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProfileServiceImpl implements ProfileService {

    private final ProfileDao profileDao;
    private final String employeeAccessCode;
    private final AmazonS3Client s3;
    private final ImageScaleUtil imageScale;

    @Autowired
    public ProfileServiceImpl(Environment e, ProfileDao profileDao, AmazonS3Client s3, ImageScaleUtil imageScale) {
        this.profileDao = profileDao;
        this.employeeAccessCode = e.getRequiredProperty("salon.employee-code");
        this.s3 = s3;
        this.imageScale = imageScale;
    }

    @Override
    public ClientInfo getClientProfile(UserAccount principal) throws Exception {

        // get client info
        // return complete profile
        return profileDao.getClientInfo(principal.getUserId());
    }

    @Override
    public void updateClientProfile(UserAccount principal, ClientInfo updateProfileRequest) throws Exception {
        // add Contact Info validation rules here
        UserProfileValidator.validateUpdateUserProfileRequestOrThrow(updateProfileRequest);

        profileDao.updateClientInfo(principal.getUserId(), updateProfileRequest);
    }

    @Override
    public EmployeeProfileResponse getEmployeeProfile(UserAccount principal) throws Exception {
        // get user contact info
        var employeeInfo = profileDao.getEmployeeInfo(principal.getUserId());

        var employeeProfileImage = s3.getSignedObjectUrl(String.format("employeeProfile/%d", principal.getUserId()), 30);

        // return complete profile
        return new EmployeeProfileResponse(employeeInfo, employeeProfileImage);
    }

    @Override
    public PublicEmployeeProfileResponse getPublicEmployeeProfile(Long employeeId) throws BadRequestException {
        // get user contact info
        var employeeInfo = profileDao.getEmployeeInfo(employeeId);

        var employeeProfileImage = s3.getSignedObjectUrl(String.format("employeeProfile/%d", employeeId), 30);

        // return complete profile
        return new PublicEmployeeProfileResponse(employeeInfo, employeeProfileImage);
    }

    @Override
    public void updateEmployeeProfile(UserAccount principal, EmployeeInfo request) throws ValidationException {
        profileDao.updateEmployeeProfile(principal.getUserId(), request);
    }

    @Override
    public void changeEmployeeProfileImage(UserAccount principal, MultipartFile image) throws Exception{
        ImageValidator.validateImageFormat(image);

        // scale image by 80%
        final byte[] scaledImage = imageScale.getScaledImage(image, 0.8F);

        // upload content to S3
        s3.uploadToS3(scaledImage, image.getContentType(), String.format("employeeProfile/%d", principal.getUserId()));
    }

}
