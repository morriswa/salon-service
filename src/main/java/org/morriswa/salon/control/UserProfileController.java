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
     * @return profile and contact information about the user
     */
    @GetMapping("/user")
    public ResponseEntity<?> getUserProfile(@AuthenticationPrincipal UserAccount principal) throws Exception{
        // using the user profile service, retrieve the current users profile
        var profile = userService.getUserProfile(principal);
        // and return it to them in JSON format
        return ResponseEntity.ok(profile);
    }

    @PostMapping("/user")
    public ResponseEntity<?> createUserProfile(
        @AuthenticationPrincipal UserAccount principal,
        @RequestBody ContactInfo createProfileRequest
    ) throws Exception {
        userService.createUserProfile(principal, createProfileRequest);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/user")
    public ResponseEntity<?> updateUserProfile(
        @AuthenticationPrincipal UserAccount principal, 
        @RequestBody ContactInfo updateProfileRequest
    ) throws Exception {
        userService.updateUserProfile(principal, updateProfileRequest);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/user/name")
    public ResponseEntity<?> updateUsername(
        @AuthenticationPrincipal UserAccount principal, 
        @RequestBody AccountRequest updateUsernameRequest) throws Exception 
    {
        userService.updateUsername(principal, updateUsernameRequest);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/user/password")
    public ResponseEntity<?> updatePassword(
        @AuthenticationPrincipal UserAccount principal, 
        @RequestBody AccountRequest updatePasswordRequest) throws Exception 
    {
        userService.updatePassword(principal, updatePasswordRequest);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/admin/promote")
    public ResponseEntity<?> promoteUser(
        @AuthenticationPrincipal UserAccount principal,
        @RequestBody AccountRequest request
    ) throws Exception {
        userService.promoteUser(principal, request);
        return ResponseEntity.noContent().build();
    }
}
