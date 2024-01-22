package org.morriswa.service;

import org.morriswa.dao.UserProfileDao;
import org.morriswa.model.RegistrationRequest;
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
    public void registerUser(RegistrationRequest request) throws Exception {
        // add user registration rules here

        userProfileDao.register(request.username(), request.password());
    }

    @Override
    public Long getUserId(Principal principal) {
        return userProfileDao.getUserId(principal.getName());
    }
}
