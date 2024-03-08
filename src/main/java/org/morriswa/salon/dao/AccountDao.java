package org.morriswa.salon.dao;

import org.morriswa.salon.model.UserAccount;
import org.morriswa.salon.model.UserInfo;


/**
 * AUTHOR: William A. Morris, Kevin Rivers <br>
 * CREATION_DATE: 2024-01-21 <br>
 * PURPOSE: <br>
 * &emsp; provides an interface for interacting with the database to perform essential User actions
 */

public interface AccountDao {

    /**
     * FOR USE WITH SPRING AUTHORIZATION MANAGER
     *
     * @param username of the user who is authenticating
     * @return the requested user, formatted for compatibility with Spring Security Filter
     */
    UserAccount findUser(String username);

    /**
     * registerUser takes a username and password string, puts them in the database
     * @param username
     * @param password
     */
    void register(String username, String password) throws Exception;

    void updateUserPassword(Long userId, String currentEncodedPassword, String currentPassword, String newPassword) throws Exception;

    void changeUsername(Long userId, String newUsername) throws Exception;

    void createUserContactInfo(Long userId, UserInfo request) throws Exception;

    void unlockClientPermissions(Long userId);

    void unlockEmployeePermissions(Long userId);

}
