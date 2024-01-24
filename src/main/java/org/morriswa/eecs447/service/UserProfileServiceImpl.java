package org.morriswa.eecs447.service;

import org.morriswa.eecs447.dao.UserProfileDao;
import org.morriswa.eecs447.model.RegistrationRequest;
import org.morriswa.eecs447.model.UserProfileResponse;
import org.morriswa.eecs447.validation.ServiceValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileDao userProfileDao;

    @Autowired
    public UserProfileServiceImpl(UserProfileDao userProfileDao) {
        this.userProfileDao = userProfileDao;
    }

    @Override
    public String registerUser(RegistrationRequest request) throws Exception {
        // add user registration rules here...

        // validate username and password fields
        ServiceValidator.validateUsernameOrThrow(request.username());
        ServiceValidator.validatePasswordOrThrow(request.password());

        // if all validations were passed, the user may be registered in the database
        userProfileDao.register(request.username(), request.password());

        // if the user was successfully registered, return the username they were registered with
        return request.username();
    }

    @Override
    public UserProfileResponse getUserProfile(Principal principal) {
        return userProfileDao.getUserProfile(principal.getName());
    }
}
