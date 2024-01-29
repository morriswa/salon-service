package org.morriswa.eecs447.service;

import org.morriswa.eecs447.dao.UserProfileDao;
import org.morriswa.eecs447.model.ContactInfoRequest;
import org.morriswa.eecs447.model.AccountRequest;
import org.morriswa.eecs447.model.UserProfileResponse;
import org.morriswa.eecs447.validation.ServiceValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;

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
    public UserProfileResponse getUserProfile(Principal principal) {
        return userProfileDao.getUserProfile(principal.getName());
    }

    @Override
    public void createUserProfile(Principal principal, ContactInfoRequest createProfileRequest) {
        // add Contact Info validation rules here

        userProfileDao.createUserContactInfo(principal.getName(), createProfileRequest);
    }

    @Override
    public void updateUserProfile(Principal principal, ContactInfoRequest updateProfileRequest) {
        // add Contact Info validation rules here

        userProfileDao.updateUserContactInfo(principal.getName(), updateProfileRequest);
    }

    @Override
    public void updateUsername(Principal principal, AccountRequest updateUsernameRequest) throws Exception {
        // validate requested username
        ServiceValidator.validateUsernameOrThrow(updateUsernameRequest.username());

        // initiate change
        userProfileDao.changeUsername(principal.getName(), updateUsernameRequest.username());
    }

    @Override
    public void updatePassword(Principal principal, AccountRequest updatePasswordRequest) throws Exception {
        // validate requested password
        ServiceValidator.validatePasswordChangeOrThrow(
                updatePasswordRequest.password(),
                updatePasswordRequest.confirmPassword());

        userProfileDao.updateUserPassword(
                principal.getName(),
                updatePasswordRequest.currentPassword(),
                updatePasswordRequest.password());
    }
}
