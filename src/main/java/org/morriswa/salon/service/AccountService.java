package org.morriswa.salon.service;

import org.morriswa.salon.model.AccountRequest;
import org.morriswa.salon.model.UserAccount;
import org.morriswa.salon.model.UserAccountResponse;
import org.morriswa.salon.model.UserInfo;

/**
 * responsible for validating and maintaining all accounts
 *
 * @author William A. Morris
 * @since 2024-01-22
 */
public interface AccountService {

    /**
     * builds a user account response given an authenticated user
     *
     * @param principal the authenticated user
     * @return requested account data
     */
    UserAccountResponse login(UserAccount principal);

    /**
     * registers a new application user
     *
     * @param request essential account information
     * @throws Exception if the user could not be registered
     */
    void registerUser(AccountRequest request) throws Exception;

    /**
     * creates a user profile for a freshly registered user
     *
     * @param principal of the authenticated user
     * @param createProfileRequest required information to save a user profile
     * @throws Exception if profile could not be created
     */
    void createUserProfile(UserAccount principal, UserInfo createProfileRequest) throws Exception;

    /**
     * updates a user's account name used to log in
     *
     * @param principal the authenticated user
     * @param updateUsernameRequest containing new username
     * @throws Exception if user account could not be updated
     */
    void updateUsername(UserAccount principal, AccountRequest updateUsernameRequest) throws Exception;

    /**
     * updates a user's account password used to log in
     *
     * @param principal the authenticated user
     * @param updatePasswordRequest containing new password
     * @throws Exception if user account could not be updated
     */
    void updatePassword(UserAccount principal, AccountRequest updatePasswordRequest) throws Exception;

    /**
     * creates an employee in the system
     *
     * @param principal of the authenticated user
     * @param code to match with employee access code
     * @throws Exception if the employee could not be created
     */
    void unlockEmployeePortalWithCode(UserAccount principal, String code) throws Exception;

    /**
     * creates a new client in the system
     *
     * @param principal the authenticated user
     * @throws Exception if the client could not be created
     */
    void unlockClientPortal(UserAccount principal) throws Exception;

}
