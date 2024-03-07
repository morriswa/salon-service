package org.morriswa.salon.service;

import org.morriswa.salon.dao.UserProfileDao;
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

    private final UserProfileDao userProfileDao;
    private final String employeeAccessCode;
    private final AmazonS3Client s3;
    private final ImageScaleUtil imageScale;

    @Autowired
    public ProfileServiceImpl(Environment e, UserProfileDao userProfileDao, AmazonS3Client s3, ImageScaleUtil imageScale) {
        this.userProfileDao = userProfileDao;
        this.employeeAccessCode = e.getRequiredProperty("salon.employee-code");
        this.s3 = s3;
        this.imageScale = imageScale;
    }

    @Override
    public UserProfileResponse getClientProfile(UserAccount principal) throws Exception {

        // get user contact info
        var contactInfo = userProfileDao.getContactInfo(principal.getUserId());

        // attach info from authentication principal
        var userProfile = new UserProfileResponse(principal, contactInfo);
        
        // return complete profile
        return userProfile;
    }

    @Override
    public void updateClientProfile(UserAccount principal, ContactInfo updateProfileRequest) throws Exception {
        // add Contact Info validation rules here
        UserProfileValidator.validateUpdateUserProfileRequestOrThrow(updateProfileRequest);

        userProfileDao.updateUserContactInfo(principal.getUserId(), updateProfileRequest);
    }

    @Override
    public EmployeeProfileResponse getEmployeeProfile(UserAccount principal) throws Exception {
        // get user contact info
        var employeeInfo = userProfileDao.getEmployeeInfo(principal.getUserId());

        var employeeProfileImage = s3.getSignedObjectUrl(String.format("employeeProfile/%d", principal.getUserId()), 30);

        // return complete profile
        return new EmployeeProfileResponse(employeeInfo, employeeProfileImage);
    }

    @Override
    public PublicEmployeeProfileResponse getPublicEmployeeProfile(Long employeeId) throws BadRequestException {
        // get user contact info
        var employeeInfo = userProfileDao.getEmployeeInfo(employeeId);

        var employeeProfileImage = s3.getSignedObjectUrl(String.format("employeeProfile/%d", employeeId), 30);

        // return complete profile
        return new PublicEmployeeProfileResponse(employeeInfo, employeeProfileImage);
    }

    @Override
    public void updateEmployeeProfile(UserAccount principal, EmployeeInfo request) throws ValidationException {
        userProfileDao.updateEmployeeProfile(principal.getUserId(), request);
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
