package org.morriswa.eecs447.service;

import org.morriswa.eecs447.dao.UserProfileDao;
import org.morriswa.eecs447.model.ContactInfoRequest;
import org.morriswa.eecs447.model.AccountRequest;
import org.morriswa.eecs447.model.ApplicationUser;
import org.morriswa.eecs447.model.UserProfileResponse;
import org.morriswa.eecs447.validation.ServiceValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileDao userProfileDao;

    @Autowired
    public UserProfileServiceImpl(UserProfileDao userProfileDao) {
        this.userProfileDao = userProfileDao;
    }

    @Override
    public String registerUser(AccountRequest request) throws Exception {
        // add user registration rules here...

        // validate username and password fields
        ServiceValidator.validateUsernameOrThrow(request.username());
        ServiceValidator.validatePasswordOrThrow(request.password());

        // if all validations were passed, the user may be registered in the database
        userProfileDao.register(request.username(), request.password());

        // if the user was successfully registered, return the username they were registered with
        return request.username();
    }

    @Override
    public UserProfileResponse getUserProfile(ApplicationUser principal) {
        return userProfileDao.getUserProfile(principal.getUserId());
    }

    @Override
    public void createUserProfile(ApplicationUser principal, ContactInfoRequest createProfileRequest) {
        // add Contact Info validation rules here

        userProfileDao.createUserContactInfo(principal.getUserId(), createProfileRequest);
    }

    @Override
    public void updateUserProfile(ApplicationUser principal, ContactInfoRequest updateProfileRequest) {
        // add Contact Info validation rules here

        userProfileDao.updateUserContactInfo(principal.getUserId(), updateProfileRequest);
    }

    @Override
    public void updateUsername(ApplicationUser principal, AccountRequest updateUsernameRequest) throws Exception {
        // validate requested username
        ServiceValidator.validateUsernameOrThrow(updateUsernameRequest.username());

        // initiate change
        userProfileDao.changeUsername(principal.getUserId(), updateUsernameRequest.username());
    }

    @Override
    public void updatePassword(ApplicationUser principal, AccountRequest updatePasswordRequest) throws Exception {
        // validate requested password
        ServiceValidator.validatePasswordChangeOrThrow(
                updatePasswordRequest.password(),
                updatePasswordRequest.confirmPassword());

        userProfileDao.updateUserPassword(
                principal.getUserId(),
                updatePasswordRequest.currentPassword(),
                updatePasswordRequest.password());
    }
}
