package org.morriswa.eecs447.service;

import org.morriswa.eecs447.dao.UserProfileDao;
import org.morriswa.eecs447.enumerated.AccountType;
import org.morriswa.eecs447.exception.BadRequestException;
import org.morriswa.eecs447.model.ContactInfo;
import org.morriswa.eecs447.model.AccountRequest;
import org.morriswa.eecs447.model.UserAccount;
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
    public UserProfileResponse getUserProfile(UserAccount principal) {

        // get user contact info
        var contactInfo = userProfileDao.getContactInfo(principal.getUserId());

        // attach info from authentication principal
        var userProfile = new UserProfileResponse(principal, contactInfo);
        
        // return complete profile
        return userProfile;
    }

    @Override
    public void createUserProfile(UserAccount principal, ContactInfo createProfileRequest) throws Exception {
        // add Contact Info validation rules here

        userProfileDao.createUserContactInfo(principal.getUserId(), createProfileRequest);
    }

    @Override
    public void updateUserProfile(UserAccount principal, ContactInfo updateProfileRequest) throws Exception {
        // add Contact Info validation rules here

        userProfileDao.updateUserContactInfo(principal.getUserId(), updateProfileRequest);
    }

    @Override
    public void updateUsername(UserAccount principal, AccountRequest updateUsernameRequest) throws Exception {
        // validate requested username
        ServiceValidator.validateUsernameOrThrow(updateUsernameRequest.username());

        // initiate change
        userProfileDao.changeUsername(principal.getUserId(), updateUsernameRequest.username());
    }

    @Override
    public void updatePassword(UserAccount principal, AccountRequest updatePasswordRequest) throws Exception {
        // validate requested password
        ServiceValidator.validatePasswordChangeOrThrow(
                updatePasswordRequest.password(),
                updatePasswordRequest.confirmPassword());

        userProfileDao.updateUserPassword(
                principal.getUserId(),
                updatePasswordRequest.currentPassword(),
                updatePasswordRequest.password());
    }

    @Override
    public void promoteUser(UserAccount principal, AccountRequest request) throws Exception {

        ServiceValidator.validatePromoteRequestOrThrow(request);

        if (request.userId()!=null)
            userProfileDao.promoteUser(principal.getUserId(), request.userId(), AccountType.getEnum(request.role()));
        else userProfileDao.promoteUser(principal.getUserId(), request.username(), AccountType.getEnum(request.role()));
    }
}
