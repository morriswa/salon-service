package org.morriswa.eecs447.dao;

import org.morriswa.eecs447.model.UserAccount;
import org.morriswa.eecs447.enumerated.AccountType;
import org.morriswa.eecs447.model.ContactInfo;


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
    UserAccount findUser(String username);

    /**
     * registerUser takes a username and password string, puts them in the database
     * @param username
     * @param password
     */
    void register(String username, String password) throws Exception;

    ContactInfo getContactInfo(Long userId);

    void updateUserPassword(Long userId, String currentPassword, String newPassword) throws Exception;

    void changeUsername(Long userId, String newUsername) throws Exception;

    void createUserContactInfo(Long userId, ContactInfo request);

    void updateUserContactInfo(Long userId, ContactInfo request);

    void promoteUser(Long promoterId, Long userId, AccountType role);

    void promoteUser(Long promoterId, String username, AccountType role);
}
