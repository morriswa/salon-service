package org.morriswa.eecs447.service;

import org.morriswa.eecs447.model.RegistrationRequest;
import org.morriswa.eecs447.model.UserProfileResponse;

import java.security.Principal;

public interface UserProfileService {
    String registerUser(RegistrationRequest request) throws Exception;

    UserProfileResponse getUserProfile(Principal principal);
}
