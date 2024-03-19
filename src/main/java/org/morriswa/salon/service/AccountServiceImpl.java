package org.morriswa.salon.service;

import org.morriswa.salon.dao.AccountDao;
import org.morriswa.salon.exception.BadRequestException;
import org.morriswa.salon.model.AccountRequest;
import org.morriswa.salon.model.UserAccount;
import org.morriswa.salon.model.UserAccountResponse;
import org.morriswa.salon.model.UserInfo;
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

    @Autowired
    public AccountServiceImpl(Environment e, AccountDao accountDao) {
        this.accountDao = accountDao;
        this.employeeAccessCode = e.getRequiredProperty("salon.employee-code");
    }


    @Override
    public UserAccountResponse login(UserAccount principal) {
        // build response using information about the current Authentication Principal
        return new UserAccountResponse(
                principal.getUserId(),
                principal.getUsername(),
                principal.getDateCreated(),
                principal.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toSet()));
    }

    @Override
    public void registerUser(AccountRequest request) throws Exception {

        // validate username and password fields
        UserProfileValidator.validateUsernameOrThrow(request.username());
        UserProfileValidator.validatePasswordOrThrow(request.password());

        // if all validations were passed, the user may be registered in the database
        accountDao.register(request.username(), request.password());
    }

    @Override
    public void createUserProfile(UserAccount principal, UserInfo createProfileRequest) throws Exception {

        // validate create user profile request
        UserProfileValidator.validateCreateProfileRequestOrThrow(createProfileRequest);

        // attempt to store provided contact information
        accountDao.enterContactInfo(principal.getUserId(), createProfileRequest);
    }

    @Override
    public void updateUsername(UserAccount principal, AccountRequest updateUsernameRequest) throws Exception {

        // validate new requested username
        UserProfileValidator.validateUsernameOrThrow(updateUsernameRequest.username());

        // store change in db
        accountDao.changeUsername(principal.getUserId(), updateUsernameRequest.username());
    }

    @Override
    public void updatePassword(UserAccount principal, AccountRequest updatePasswordRequest) throws Exception {

        // validate new requested password
        UserProfileValidator.validatePasswordChangeOrThrow(
                updatePasswordRequest.password(),
                updatePasswordRequest.confirmPassword());

        // store change in db
        accountDao.updateUserPassword(
                principal.getUserId(),
                principal.getPassword(),
                updatePasswordRequest.currentPassword(),
                updatePasswordRequest.password());
    }

    @Override
    public void unlockEmployeePortalWithCode(UserAccount principal, String code) throws Exception {

        // if access code is blank or invalid, throw appropriate error
        if (!StrTools.hasValue(code) || !code.equals(employeeAccessCode))
            throw new BadRequestException("Bad access code!");

        // todo verify employee criteria before promoting

        // register employee in database
        accountDao.completeEmployeeRegistration(principal.getUserId());
    }

    @Override
    public void unlockClientPortal(UserAccount principal) throws Exception {
        // todo verify client criteria before promoting

        // register client in database
        accountDao.completeClientRegistration(principal.getUserId());
    }

}
