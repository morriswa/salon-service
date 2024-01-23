package org.morriswa.eecs447.control;

import org.morriswa.eecs447.model.RegistrationRequest;
import org.morriswa.eecs447.service.UserProfileService;
import org.morriswa.eecs447.utility.HttpResponseFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

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

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegistrationRequest request) throws Exception {
        var registeredUsername = userService.registerUser(request);
        return response.build(HttpStatus.OK,
                String.format("Successfully registered user with username: %s", registeredUsername));
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserProfile(Principal principal) {
        var id = userService.getUserProfile(principal);
        return response.build(HttpStatus.OK, "Successfully retrieved user profile!", id);
    }
}
