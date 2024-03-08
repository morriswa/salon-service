package org.morriswa.salon.service;

import org.morriswa.salon.exception.BadRequestException;
import org.morriswa.salon.exception.ValidationException;
import org.morriswa.salon.model.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * AUTHOR: William A. Morris <br>
 * CREATION_DATE: 2024-01-22 <br>
 * PURPOSE: <br>
 * &emsp; provides an interface for performing essential account actions
 */
public interface AccountService {

    UserAccountResponse login(UserAccount principal);

    String registerUser(AccountRequest request) throws Exception;

    void createUserProfile(UserAccount principal, UserInfo createProfileRequest) throws Exception;

    void updateUsername(UserAccount principal, AccountRequest updateUsernameRequest) throws Exception;

    void updatePassword(UserAccount principal, AccountRequest updatePasswordRequest) throws Exception;

    void unlockEmployeePortalWithCode(UserAccount principal, String code) throws BadRequestException;

    void unlockClientPortal(UserAccount principal) throws Exception;

}
