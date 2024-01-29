package org.morriswa.eecs447.service;

import org.morriswa.eecs447.model.ContactInfoRequest;
import org.morriswa.eecs447.model.AccountRequest;
import org.morriswa.eecs447.model.UserProfileResponse;

import java.security.Principal;

/**
 * AUTHOR: William A. Morris <br>
 * CREATION_DATE: 2024-01-22 <br>
 * PURPOSE: <br>
 * &emsp; provides an interface for performing essential User actions
 */
public interface UserProfileService {

    String registerUser(AccountRequest request) throws Exception;

    UserProfileResponse getUserProfile(Principal principal);

    void createUserProfile(Principal principal, ContactInfoRequest createProfileRequest);

    void updateUserProfile(Principal principal, ContactInfoRequest updateProfileRequest);

    void updateUsername(Principal principal, AccountRequest updateUsernameRequest) throws Exception;

    void updatePassword(Principal principal, AccountRequest updatePasswordRequest) throws Exception;
}
