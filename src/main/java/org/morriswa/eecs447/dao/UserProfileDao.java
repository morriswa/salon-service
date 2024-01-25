package org.morriswa.eecs447.dao;

import org.morriswa.eecs447.enumurated.UserContactPreferences;
import org.morriswa.eecs447.model.ContactInfoRequest;
import org.morriswa.eecs447.model.UserProfileResponse;
import org.springframework.security.core.userdetails.User;

/**
 * AUTHOR: William A. Morris, Kevin Rivers <br>
 * CREATION_DATE: 2024-01-21 <br>
 * PURPOSE: <br>
 * &emsp; provides an interface for interacting with the database to perform essential User actions
 */

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

    void updateUserPassword(String username, String currentPassword, String newPassword);

    void changeUsername(String currentUsername, String newUsername);

    void updateUserContactInfo(String username, ContactInfoRequest request);

    void updateUserContactPreference(String username, UserContactPreferences preference);
}
