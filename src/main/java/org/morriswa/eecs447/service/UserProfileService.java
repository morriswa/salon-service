package org.morriswa.eecs447.service;

import org.morriswa.eecs447.model.RegistrationRequest;
import org.morriswa.eecs447.model.UserProfileResponse;

import java.security.Principal;

/**
 * AUTHOR: William A. Morris <br>
 * CREATION_DATE: 2024-01-22 <br>
 * PURPOSE: <br>
 * &emsp; provides an interface for performing essential User actions
 */
public interface UserProfileService {
    String registerUser(RegistrationRequest request) throws Exception;

    UserProfileResponse getUserProfile(Principal principal);
}
