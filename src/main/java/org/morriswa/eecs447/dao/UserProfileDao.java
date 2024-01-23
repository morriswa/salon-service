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

    /**
     * registerUser takes a username and password string, puts them in the database
     * @param username
     * @param password
     */
    void register(String username, String password) throws Exception;

    /**
     * Retrieves the full user profile from the database by username
     * @param username
     * @return
     */
    UserProfileResponse getUserProfile(String username);
}
