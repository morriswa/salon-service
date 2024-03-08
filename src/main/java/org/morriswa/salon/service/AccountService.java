package org.morriswa.salon.service;

import org.morriswa.salon.exception.BadRequestException;
import org.morriswa.salon.model.AccountRequest;
import org.morriswa.salon.model.UserAccount;
import org.morriswa.salon.model.UserAccountResponse;
import org.morriswa.salon.model.UserInfo;

/**
 * responsible for validating and maintaining all accounts
 *
 * @author William A. Morris
 * @since 2024-01-22
 */
public interface AccountService {

    UserAccountResponse login(UserAccount principal);

    void registerUser(AccountRequest request) throws Exception;

    void createUserProfile(UserAccount principal, UserInfo createProfileRequest) throws Exception;

    void updateUsername(UserAccount principal, AccountRequest updateUsernameRequest) throws Exception;

    void updatePassword(UserAccount principal, AccountRequest updatePasswordRequest) throws Exception;

    void unlockEmployeePortalWithCode(UserAccount principal, String code) throws BadRequestException;

    void unlockClientPortal(UserAccount principal) throws Exception;

}
