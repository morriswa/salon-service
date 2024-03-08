package org.morriswa.salon.dao;

import org.morriswa.salon.model.UserAccount;
import org.morriswa.salon.model.UserInfo;


/**
 * provides an interface for interacting with the database to perform essential User Account actions
 *
 * @author William A. Morris, Kevin Rivers
 * @since 2024-01-21
 */
public interface AccountDao {

    /**
     * FOR USE WITH SPRING AUTHORIZATION MANAGER
     *
     * @param username of the user who is attempting to authenticate
     * @return the requested user, formatted for compatibility with Spring Security Filter
     */
    UserAccount findUser(String username);

    /**
     * puts a new user in the database
     *
     * @param username a valid username
     * @param password a valid password
     */
    void register(String username, String password) throws Exception;

    /**
     * updates a user's account password
     *
     * @param userId of the authenticated user
     * @param currentEncodedPassword from the database
     * @param currentPassword provided by the user in request
     * @param newPassword new password to encrypt and store
     * @throws Exception if the user's password could not be updated in the database
     *
     * @author Kevin Rivers
     */
    void updateUserPassword(Long userId,
                            String currentEncodedPassword,
                            String currentPassword,
                            String newPassword) throws Exception;

    /**
     * changes an account's username
     *
     * @param userId of the authenticated user
     * @param newUsername for future authentication
     * @throws Exception if the username could not be updated
     *
     * @author Makenna Loewenherz
     */
    void changeUsername(Long userId, String newUsername) throws Exception;

    /**
     * stores a new user's account information
     *
     * @param userId of the new user
     * @param request valid User Info request to store
     * @throws Exception if the contact information is rejected
     *
     * @author Makenna Loewenherz
     */
    void enterContactInfo(Long userId, UserInfo request) throws Exception;

    /**
     * stores a new client in the database
     *
     * @param userId of the new client
     */
    void completeClientRegistration(Long userId);

    /**
     * stores a new employee in the database
     *
     * @param userId of the new employee
     */
    void completeEmployeeRegistration(Long userId);

}
