package org.morriswa.salon.service;

import org.morriswa.salon.model.*;

/**
 * AUTHOR: William A. Morris <br>
 * CREATION_DATE: 2024-01-22 <br>
 * PURPOSE: <br>
 * &emsp; provides an interface for performing essential User actions
 */
public interface UserProfileService {

    UserAccountResponse login(UserAccount principal);

    String registerUser(AccountRequest request) throws Exception;

    UserProfileResponse getUserProfile(UserAccount principal) throws Exception;

    void createUserProfile(UserAccount principal, ContactInfo createProfileRequest) throws Exception;

    void updateUserProfile(UserAccount principal, ContactInfo updateProfileRequest) throws Exception;

    void updateUsername(UserAccount principal, AccountRequest updateUsernameRequest) throws Exception;

    void updatePassword(UserAccount principal, AccountRequest updatePasswordRequest) throws Exception;

    void promoteUser(UserAccount principal, AccountRequest request) throws Exception;

}
