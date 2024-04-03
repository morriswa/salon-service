package org.morriswa.salon.service;

import org.morriswa.salon.dao.ProfileDao;
import org.morriswa.salon.model.*;
import org.morriswa.salon.utility.AmazonS3Client;
import org.morriswa.salon.utility.ImageScaleUtil;
import org.morriswa.salon.validation.ImageValidator;
import org.morriswa.salon.validation.UserProfileValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ProfileServiceImpl implements ProfileService {

    private final Environment e;
    private final ProfileDao profileDao;
    private final AmazonS3Client s3;
    private final ImageScaleUtil imageScale;

    @Autowired
    public ProfileServiceImpl(Environment e, ProfileDao profileDao, AmazonS3Client s3, ImageScaleUtil imageScale) {
        this.e = e;
        this.profileDao = profileDao;
        this.s3 = s3;
        this.imageScale = imageScale;
    }

    @Override
    public ClientInfo getClientProfile(UserAccount principal) throws Exception {

        // return all client info from db
        return profileDao.getClientInfo(principal.getUserId());
    }

    @Override
    public void updateClientProfile(UserAccount principal, ClientInfo updateProfileRequest) throws Exception {

        // validate update profile request
        UserProfileValidator.validateUpdateUserProfileRequestOrThrow(updateProfileRequest);

        // store updates in db
        profileDao.updateClientInfo(principal.getUserId(), updateProfileRequest);
    }

    @Override
    public EmployeeProfile getEmployeeProfile(UserAccount principal) throws Exception {

        // get employee info stored in db
        var employeeInfo = profileDao.getEmployeeInfo(principal.getUserId());

        // retrieve employee profile image
        var employeeProfileImage = s3.getSignedObjectUrl(String.format("employeeProfile/%d", principal.getUserId()), 30);

        // build and return complete employee profile
        return new EmployeeProfile(employeeInfo, employeeProfileImage);
    }

    @Override
    public PublicEmployeeProfile getPublicEmployeeProfile(Long employeeId) throws Exception {

        // get stored employee info
        var employeeInfo = profileDao.getEmployeeInfo(employeeId);

        // get profile image
        var employeeProfileImage = s3.getSignedObjectUrl(String.format("employeeProfile/%d", employeeId), 30);

        // build and return public profile
        return new PublicEmployeeProfile(employeeId, employeeInfo, employeeProfileImage);
    }

    @Override
    public List<PublicEmployeeProfile> retrieveFeaturedEmployees() throws Exception {

        final List<Long> featuredEmployeeIds = new ArrayList<>(){{
            final var ids = Arrays.stream(e.getRequiredProperty("salon.featured-employees")
                .split(",")).map(String::trim).map(Long::parseLong).toList();
            addAll(ids);
        }};

        List<PublicEmployeeProfile> results = new ArrayList<>();

        for (var employeeId : featuredEmployeeIds) {
            // get stored employee info
            var employeeInfo = profileDao.getEmployeeInfo(employeeId);

            // get profile image
            var employeeProfileImage = s3.getSignedObjectUrl(String.format("employeeProfile/%d", employeeId), 30);

            // build and return public profile
            results.add(new PublicEmployeeProfile(employeeId, employeeInfo, employeeProfileImage));
        }

        return results;
    }

    @Override
    public void updateEmployeeProfile(UserAccount principal, EmployeeInfo request) throws Exception {

        // validate update profile request
        UserProfileValidator.validateUpdateUserProfileRequestOrThrow(request);

        // store changes
        profileDao.updateEmployeeProfile(principal.getUserId(), request);
    }

    @Override
    public void updateEmployeeProfileImage(UserAccount principal, MultipartFile image) throws Exception {

        // validate uploaded image
        ImageValidator.validateUploadedImage(image);

        // scale image by 80%
        final byte[] scaledImage = imageScale.getScaledImage(image, 0.8F);

        // upload content to S3
        s3.uploadToS3(scaledImage, image.getContentType(), String.format("employeeProfile/%d", principal.getUserId()));
    }

}
