package org.morriswa.eecs447.service;

import org.morriswa.eecs447.model.ContactInfo;
import org.morriswa.eecs447.model.AccountRequest;
import org.morriswa.eecs447.model.UserAccount;
import org.morriswa.eecs447.model.UserProfileResponse;

/**
 * AUTHOR: William A. Morris <br>
 * CREATION_DATE: 2024-01-22 <br>
 * PURPOSE: <br>
 * &emsp; provides an interface for performing essential User actions
 */
public interface UserProfileService {

    String registerUser(AccountRequest request) throws Exception;

    UserProfileResponse getUserProfile(UserAccount principal) throws Exception;

    void createUserProfile(UserAccount principal, ContactInfo createProfileRequest) throws Exception;

    void updateUserProfile(UserAccount principal, ContactInfo updateProfileRequest) throws Exception;

    void updateUsername(UserAccount principal, AccountRequest updateUsernameRequest) throws Exception;

    void updatePassword(UserAccount principal, AccountRequest updatePasswordRequest) throws Exception;

    void promoteUser(UserAccount principal, AccountRequest request) throws Exception;
}
