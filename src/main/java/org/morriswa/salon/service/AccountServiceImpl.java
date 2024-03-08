package org.morriswa.salon.service;

import org.morriswa.salon.dao.AccountDao;
import org.morriswa.salon.exception.BadRequestException;
import org.morriswa.salon.model.*;
import org.morriswa.salon.utility.AmazonS3Client;
import org.morriswa.salon.utility.ImageScaleUtil;
import org.morriswa.salon.validation.StrTools;
import org.morriswa.salon.validation.UserProfileValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountDao accountDao;
    private final String employeeAccessCode;
    private final AmazonS3Client s3;
    private final ImageScaleUtil imageScale;

    @Autowired
    public AccountServiceImpl(Environment e, AccountDao accountDao, AmazonS3Client s3, ImageScaleUtil imageScale) {
        this.accountDao = accountDao;
        this.employeeAccessCode = e.getRequiredProperty("salon.employee-code");
        this.s3 = s3;
        this.imageScale = imageScale;
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
        accountDao.register(request.username(), request.password());

        // if the user was successfully registered, return the username they were registered with
        return request.username();
    }

    @Override
    public void createUserProfile(UserAccount principal, UserInfo createProfileRequest) throws Exception {

        // add Contact Info validation rules here
        UserProfileValidator.validateCreateProfileRequestOrThrow(createProfileRequest);

        // attempt to store provided contact information
        accountDao.createUserContactInfo(principal.getUserId(), createProfileRequest);
    }

    @Override
    public void updateUsername(UserAccount principal, AccountRequest updateUsernameRequest) throws Exception {
        // validate requested username
        UserProfileValidator.validateUsernameOrThrow(updateUsernameRequest.username());

        // initiate change
        accountDao.changeUsername(principal.getUserId(), updateUsernameRequest.username());
    }

    @Override
    public void updatePassword(UserAccount principal, AccountRequest updatePasswordRequest) throws Exception {
        // validate requested password
        UserProfileValidator.validatePasswordChangeOrThrow(
                updatePasswordRequest.password(),
                updatePasswordRequest.confirmPassword());

        accountDao.updateUserPassword(
                principal.getUserId(),
                principal.getPassword(),
                updatePasswordRequest.currentPassword(),
                updatePasswordRequest.password());
    }

    @Override
    public void unlockEmployeePortalWithCode(UserAccount principal, String code) throws BadRequestException {
        if (!StrTools.hasValue(code) || !code.equals(employeeAccessCode))
            throw new BadRequestException("Bad access code!");
        // todo verify employee criteria before promoting


        accountDao.unlockEmployeePermissions(principal.getUserId());
    }

    @Override
    public void unlockClientPortal(UserAccount principal) throws Exception {
        // todo verify client criteria before promoting

        accountDao.unlockClientPermissions(principal.getUserId());
    }

}
