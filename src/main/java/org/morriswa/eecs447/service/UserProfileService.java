package org.morriswa.eecs447.service;

import org.morriswa.eecs447.model.RegistrationRequest;

import java.security.Principal;

public interface UserProfileService {
    void registerUser(RegistrationRequest request) throws Exception;

    Long getUserId(Principal principal);
}
