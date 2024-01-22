package org.morriswa.eecs447.control;

import org.morriswa.eecs447.model.RegistrationRequest;
import org.morriswa.eecs447.service.UserProfileService;
import org.morriswa.eecs447.utility.HttpResponseFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class UserProfileController {

    // Web logic goes in Control layer

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
        userService.registerUser(request);
        return response.build(HttpStatus.OK, "Got registered");
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserId(Principal principal) {
        var id = userService.getUserId(principal);
        return response.build(HttpStatus.OK, "Successfully retrieved user id", id);
    }
}
