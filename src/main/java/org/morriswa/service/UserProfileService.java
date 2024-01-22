package org.morriswa.service;

import org.morriswa.model.RegistrationRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.security.Principal;

public interface UserProfileService {
    void registerUser(RegistrationRequest request) throws Exception;

    Long getUserId(Principal principal);
}
