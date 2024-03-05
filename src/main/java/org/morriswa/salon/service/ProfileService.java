package org.morriswa.salon.service;

import org.morriswa.salon.exception.BadRequestException;
import org.morriswa.salon.model.*;

/**
 * AUTHOR: William A. Morris <br>
 * CREATION_DATE: 2024-01-22 <br>
 * PURPOSE: <br>
 * &emsp; provides an interface for performing essential User actions
 */
public interface ProfileService {

    UserAccountResponse login(UserAccount principal);

    String registerUser(AccountRequest request) throws Exception;

    UserProfileResponse getClientProfile(UserAccount principal) throws Exception;

    void createUserProfile(UserAccount principal, ContactInfo createProfileRequest) throws Exception;

    void updateClientProfile(UserAccount principal, ContactInfo updateProfileRequest) throws Exception;

    void updateUsername(UserAccount principal, AccountRequest updateUsernameRequest) throws Exception;

    void updatePassword(UserAccount principal, AccountRequest updatePasswordRequest) throws Exception;

    void unlockEmployeePortalWithCode(UserAccount principal, String code) throws BadRequestException;

    void unlockClientPortal(UserAccount principal) throws Exception;

    EmployeeProfileResponse getEmployeeProfile(UserAccount principal) throws Exception;

    PublicEmployeeProfileResponse getPublicEmployeeProfile(Long employeeId) throws BadRequestException;
}
