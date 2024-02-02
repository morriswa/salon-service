package org.morriswa.eecs447.control;

import org.morriswa.eecs447.model.AccountRequest;
import org.morriswa.eecs447.model.UserAccount;
import org.morriswa.eecs447.model.ContactInfo;
import org.morriswa.eecs447.service.UserProfileService;
import org.morriswa.eecs447.utility.HttpResponseFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    private final HttpResponseFactory response;
    private final UserProfileService userService;

    @Autowired
    public UserProfileController(   HttpResponseFactory response,
                                    UserProfileService userService) {
        this.response = response;
        this.userService = userService;
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
        var registeredUsername = userService.registerUser(request);
        // return confirmation in JSON format
        return response.build(HttpStatus.CREATED,
                String.format("Successfully registered user with username: %s", registeredUsername));
    }

    /**
     * Http GET endpoint used to retrieve all stored information about the currently authenticated user
     *
     * @param principal injected by the Spring Security filter,
     *                  this object contains important information about the
     *                  CURRENTLY AUTHENTICATED USER. When using this object,
     *                  no further user authentication or authorization action is needed.
     * @return a nicely formatted Http Response
     */
    @GetMapping("/user")
    public ResponseEntity<?> getUserProfile(@AuthenticationPrincipal UserAccount principal) throws Exception{
        // using the user profile service, retrieve the current users profile
        var profile = userService.getUserProfile(principal);
        // and return it to them in JSON format
        return response.build(HttpStatus.OK, "Successfully retrieved user profile!", profile);
    }

    @PostMapping("/user")
    public ResponseEntity<?> createUserProfile(
        @AuthenticationPrincipal UserAccount principal,
        @RequestBody ContactInfo createProfileRequest
    ) throws Exception {
        userService.createUserProfile(principal, createProfileRequest);
        return response.build(HttpStatus.NO_CONTENT,"You have successfully created a profile!");
    }

    @PatchMapping("/user")
    public ResponseEntity<?> updateUserProfile(
        @AuthenticationPrincipal UserAccount principal, 
        @RequestBody ContactInfo updateProfileRequest
    ) throws Exception {
        userService.updateUserProfile(principal, updateProfileRequest);
        return response.build(HttpStatus.NO_CONTENT,"Successfully updated your contact information!");
    }

    @PatchMapping("/user/name")
    public ResponseEntity<?> updateUsername(
        @AuthenticationPrincipal UserAccount principal, 
        @RequestBody AccountRequest updateUsernameRequest) throws Exception 
    {
        userService.updateUsername(principal, updateUsernameRequest);
        return response.build(HttpStatus.NO_CONTENT,"You have successfully updated your username!");
    }

    @PatchMapping("/user/password")
    public ResponseEntity<?> updatePassword(
        @AuthenticationPrincipal UserAccount principal, 
        @RequestBody AccountRequest updatePasswordRequest) throws Exception 
    {
        userService.updatePassword(principal, updatePasswordRequest);
        return response.build(HttpStatus.NO_CONTENT,"Sucessfully updated password!");
    }

    @PatchMapping("/admin/promote")
    public ResponseEntity<?> promoteUser(
        @AuthenticationPrincipal UserAccount principal,
        @RequestBody AccountRequest request
    ) throws Exception {
        userService.promoteUser(principal, request);
        return response.build(HttpStatus.OK, "Successfully promoted user");
    }
}
