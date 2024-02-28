package org.morriswa.salon.control;

import org.morriswa.salon.model.AccountRequest;
import org.morriswa.salon.model.ContactInfo;
import org.morriswa.salon.model.UserAccount;
import org.morriswa.salon.service.UserProfileService;
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
public class UserProfileController {

    private final UserProfileService userService;

    @Autowired
    public UserProfileController(UserProfileService userService) {
        this.userService = userService;
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
        var userInfo = userService.login(principal);
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
        userService.registerUser(request);
        // return confirmation in JSON format
        return ResponseEntity.noContent().build();
    }

    /**
     * Http GET endpoint used to retrieve all stored information about the currently authenticated user
     *
     * @param principal currently authenticated User Account
     * @return profile and contact information about the user if operation was successful, else error response
     */
    @GetMapping("/user")
    public ResponseEntity<?> getUserProfile(@AuthenticationPrincipal UserAccount principal) throws Exception{
        // using the user profile service, retrieve the current users profile
        var profile = userService.getUserProfile(principal);
        // and return it to them in JSON format
        return ResponseEntity.ok(profile);
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
    @PostMapping("/user")
    public ResponseEntity<?> createUserProfile(
        @AuthenticationPrincipal UserAccount principal,
        @RequestBody ContactInfo createProfileRequest
    ) throws Exception {
        userService.createUserProfile(principal, createProfileRequest);
        return ResponseEntity.noContent().build();
    }

    /**
     * HTTP Patch endpoint to update an existing user's contact info
     *
     * @param principal authenticated user
     * @param updateProfileRequest containing contact info to be updated
     * @return blank response
     * @throws Exception return error response if user's profile cannot be updated
     */
    @PatchMapping("/user")
    public ResponseEntity<?> updateUserProfile(
        @AuthenticationPrincipal UserAccount principal, 
        @RequestBody ContactInfo updateProfileRequest
    ) throws Exception {
        userService.updateUserProfile(principal, updateProfileRequest);
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
        userService.updateUsername(principal, updateUsernameRequest);
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
        userService.updatePassword(principal, updatePasswordRequest);
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
    @PatchMapping("/user/access/employee")
    public ResponseEntity<?> unlockEmployeePortal(
            @AuthenticationPrincipal UserAccount principal,
            @RequestParam String accessCode
    ) throws Exception {
        userService.unlockEmployeePortalWithCode(principal, accessCode);
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
    @PatchMapping("/user/access/client")
    public ResponseEntity<?> unlockClientPortal(
            @AuthenticationPrincipal UserAccount principal
    ) throws Exception {
        userService.unlockClientPortal(principal);
        return ResponseEntity.noContent().build();
    }

}
