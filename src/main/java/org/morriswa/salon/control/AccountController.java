package org.morriswa.salon.control;

import org.morriswa.salon.model.AccountRequest;
import org.morriswa.salon.model.UserAccount;
import org.morriswa.salon.model.UserInfo;
import org.morriswa.salon.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * AUTHOR: William A. Morris, Kevin Rivers <br>
 * CREATION_DATE: 2024-01-22 <br>
 * PURPOSE: <br>
 * &emsp; provides a REST API for performing essential user functions that can be consumed by other applications
 */

@RestController
public class AccountController {

    private final AccountService accounts;

    @Autowired
    public AccountController(AccountService accounts) {
        this.accounts = accounts;
    }


    /**
     * Http GET endpoint to return account information to the UI
     *
     * @param principal injected by the Spring Security filter,
     *                  this object contains important information about the
     *                  CURRENTLY AUTHENTICATED USER. When using this object,
     *                  no further user authentication or authorization action is needed.
     * @return JSON response with account information, notably user's granted authorities
     */
    @GetMapping("/login")
    public ResponseEntity<?> login(@AuthenticationPrincipal UserAccount principal) {
        // use the user profile service to register a new user, and retrieve the username they were registered with
        var userInfo = accounts.login(principal);
        // return confirmation in JSON format
        return ResponseEntity.ok(userInfo);
    }

    /**
     * Http POST endpoint used to register a new User
     *
     * @param request the body of the http request, containing user registration info
     * @return a nicely formatted Http Response confirming that the user was successfully registered
     * @throws Exception if the user could not be registered
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody AccountRequest request) throws Exception {
        // use the user profile service to register a new user, and retrieve the username they were registered with
        accounts.registerUser(request);
        // return confirmation in JSON format
        return ResponseEntity.noContent().build();
    }

    /**
     * HTTP Post used to finish account creation
     * after successful account creation, the user will be able to access the client portal
     *
     * @param principal authenticated user
     * @param createProfileRequest containing all contact info required by the app
     * @return blank response
     * @throws Exception return error response if the user's profile cannot be created
     */
    @PostMapping("/r2/profile")
    public ResponseEntity<?> createUserProfile(
        @AuthenticationPrincipal UserAccount principal,
        @RequestBody UserInfo createProfileRequest
    ) throws Exception {
        accounts.createUserProfile(principal, createProfileRequest);
        return ResponseEntity.noContent().build();
    }

    /**
     * HTTP Patch endpoint for users to enter an access code to gain access to the employee portal
     *
     * @param principal of the authenticated user
     * @param accessCode employee access code
     * @return blank response
     * @throws Exception return error response if access code is incorrect
     */
    @PatchMapping("/r2/access/employee")
    public ResponseEntity<?> unlockEmployeePortal(
            @AuthenticationPrincipal UserAccount principal,
            @RequestParam String accessCode
    ) throws Exception {
        accounts.unlockEmployeePortalWithCode(principal, accessCode);
        return ResponseEntity.noContent().build();
    }

    /**
     * HTTP Patch endpoint for legacy users
     * and users experiencing registration errors
     * to access the client portal
     *
     * @param principal of the authenticated user
     * @return blank response
     * @throws Exception return error response if client access cannot be granted
     */
    @PatchMapping("/r2/access/client")
    public ResponseEntity<?> unlockClientPortal(
            @AuthenticationPrincipal UserAccount principal
    ) throws Exception {
        accounts.unlockClientPortal(principal);
        return ResponseEntity.noContent().build();
    }

    /**
     * HTTP Patch endpoint to change an existing user's account username
     *
     * @param principal authenticated user
     * @param updateUsernameRequest containing all information required to process username change
     * @return blank response
     * @throws Exception return error response if user's name could not be changed
     */
    @PatchMapping("/user/name")
    public ResponseEntity<?> updateUsername(
        @AuthenticationPrincipal UserAccount principal, 
        @RequestBody AccountRequest updateUsernameRequest) throws Exception 
    {
        accounts.updateUsername(principal, updateUsernameRequest);
        return ResponseEntity.noContent().build();
    }

    /**
     * HTTP Patch endpoint to change an existing user's account password
     *
     * @param principal authenticated user
     * @param updatePasswordRequest containing all information required to process password change
     * @return blank response
     * @throws Exception return error response if the user's password could not be changed
     */
    @PatchMapping("/user/password")
    public ResponseEntity<?> updatePassword(
        @AuthenticationPrincipal UserAccount principal, 
        @RequestBody AccountRequest updatePasswordRequest) throws Exception 
    {
        accounts.updatePassword(principal, updatePasswordRequest);
        return ResponseEntity.noContent().build();
    }


}
