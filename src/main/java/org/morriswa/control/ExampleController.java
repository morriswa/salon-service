package org.morriswa.control;

import org.morriswa.model.RegistrationRequest;
import org.morriswa.service.ExampleService;
import org.morriswa.service.UserDetailsServiceImpl;
import org.morriswa.utility.HttpResponseFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ExampleController {

    // Web logic goes in Control layer

    private final HttpResponseFactory response;
    private final ExampleService exampleService;
    private final UserDetailsServiceImpl userService;

    @Autowired public ExampleController(HttpResponseFactory response,
                                        ExampleService exampleService, UserDetailsServiceImpl userService) {
        this.response = response;
        this.exampleService = exampleService;
        this.userService = userService;
    }

    @GetMapping("hello-world")
    public ResponseEntity<?> helloWorldEndpoint() {
        var payload = exampleService.helloWorld();
        return response.build(HttpStatus.OK, "Successfully hit Hello World endpoint!", payload);
    }

    @GetMapping("expect-error")
    public ResponseEntity<?> exampleErrorEndpoint() throws Exception {
        exampleService.throwAnError();
        return response.build(HttpStatus.OK, "Successfully hit Hello World endpoint!");
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegistrationRequest body) {
        userService.register(body.username(), body.password());
        return response.build(HttpStatus.OK, "Got registered");
    }

}
