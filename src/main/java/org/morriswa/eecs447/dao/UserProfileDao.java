package org.morriswa.eecs447.dao;

import org.morriswa.eecs447.model.UserProfileResponse;
import org.springframework.security.core.userdetails.User;

public interface UserProfileDao {
    /** 
     * registerUser takes a username and password string, puts them in the database
     * @param username 
     * @param password
     */
    void registerUser(String username, String password);

    /**
     * authenticateUser takes a username string, finds the user in db, 
     * and returns it in a security filter friendly format.
     * @param username
     * @return
     */
    User authenticateUser(String username);

    /**
     * Retrieves the full user profile from the database by username
     * @param username
     * @return
     */
    UserProfileResponse getUserProfile(String username);
}
