package org.morriswa.eecs447.dao;

import org.morriswa.eecs447.model.UserProfileResponse;
import org.springframework.security.core.userdetails.User;

public interface UserProfileDao {

    /**
     * FOR USE WITH SPRING AUTHORIZATION MANAGER
     *
     * @param username of the user who is authenticating
     * @return the requested user, formatted for compatibility with Spring Security Filter
     */
    User findUser(String username);

    void register(String username, String password) throws Exception;

    UserProfileResponse getUserProfile(String username);
}
