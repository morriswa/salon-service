package org.morriswa.eecs447.service;

import org.morriswa.eecs447.model.ContactInfoRequest;
import org.morriswa.eecs447.model.AccountRequest;
import org.morriswa.eecs447.model.ApplicationUser;
import org.morriswa.eecs447.model.UserProfileResponse;

/**
 * AUTHOR: William A. Morris <br>
 * CREATION_DATE: 2024-01-22 <br>
 * PURPOSE: <br>
 * &emsp; provides an interface for performing essential User actions
 */
public interface UserProfileService {

    String registerUser(AccountRequest request) throws Exception;

    UserProfileResponse getUserProfile(ApplicationUser principal);

    void createUserProfile(ApplicationUser principal, ContactInfoRequest createProfileRequest);

    void updateUserProfile(ApplicationUser principal, ContactInfoRequest updateProfileRequest);

    void updateUsername(ApplicationUser principal, AccountRequest updateUsernameRequest) throws Exception;

    void updatePassword(ApplicationUser principal, AccountRequest updatePasswordRequest) throws Exception;
}
