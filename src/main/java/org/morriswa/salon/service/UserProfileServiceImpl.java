package org.morriswa.salon.service;

import org.morriswa.salon.dao.UserProfileDao;
import org.morriswa.salon.enumerated.AccountType;
import org.morriswa.salon.model.*;
import org.morriswa.salon.validation.UserProfileValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileDao userProfileDao;

    @Autowired
    public UserProfileServiceImpl(UserProfileDao userProfileDao) {
        this.userProfileDao = userProfileDao;
    }


    @Override
    public UserAccountResponse login(UserAccount principal) {
        return new UserAccountResponse(
                principal.getUserId(),
                principal.getUsername(),
                principal.getDateCreated(),
                principal.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toSet()));
    }

    @Override
    public String registerUser(AccountRequest request) throws Exception {
        // add user registration rules here...

        // validate username and password fields
        UserProfileValidator.validateUsernameOrThrow(request.username());
        UserProfileValidator.validatePasswordOrThrow(request.password());

        // if all validations were passed, the user may be registered in the database
        userProfileDao.register(request.username(), request.password());

        // if the user was successfully registered, return the username they were registered with
        return request.username();
    }

    @Override
    public UserProfileResponse getUserProfile(UserAccount principal) throws Exception {

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
        UserProfileValidator.validateCreateProfileRequestOrThrow(createProfileRequest);

        userProfileDao.createUserContactInfo(principal.getUserId(), createProfileRequest);
    }

    @Override
    public void updateUserProfile(UserAccount principal, ContactInfo updateProfileRequest) throws Exception {
        // add Contact Info validation rules here
        UserProfileValidator.validateUpdateUserProfileRequestOrThrow(updateProfileRequest);

        userProfileDao.updateUserContactInfo(principal.getUserId(), updateProfileRequest);
    }

    @Override
    public void updateUsername(UserAccount principal, AccountRequest updateUsernameRequest) throws Exception {
        // validate requested username
        UserProfileValidator.validateUsernameOrThrow(updateUsernameRequest.username());

        // initiate change
        userProfileDao.changeUsername(principal.getUserId(), updateUsernameRequest.username());
    }

    @Override
    public void updatePassword(UserAccount principal, AccountRequest updatePasswordRequest) throws Exception {
        // validate requested password
        UserProfileValidator.validatePasswordChangeOrThrow(
                updatePasswordRequest.password(),
                updatePasswordRequest.confirmPassword());

        userProfileDao.updateUserPassword(
                principal.getUserId(),
                principal.getPassword(),
                updatePasswordRequest.currentPassword(),
                updatePasswordRequest.password());
    }

    @Override
    public void promoteUser(UserAccount principal, AccountRequest request) throws Exception {

        UserProfileValidator.validatePromoteRequestOrThrow(request);

        if (request.userId()!=null)
            userProfileDao.promoteUser(principal.getUserId(), request.userId(), AccountType.getEnum(request.role()));
        else userProfileDao.promoteUser(principal.getUserId(), request.username(), AccountType.getEnum(request.role()));
    }

}
