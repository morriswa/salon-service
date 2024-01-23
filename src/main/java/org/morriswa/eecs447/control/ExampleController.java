package org.morriswa.eecs447.control;

import org.morriswa.eecs447.service.ExampleService;
import org.morriswa.eecs447.utility.HttpResponseFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController //Indicates that the class is code for a REST controller ( Provides endpoints to user )
public class ExampleController {

    // Web logic goes in Control layer

    private final HttpResponseFactory response;
    private final ExampleService exampleService;

    @Autowired // Automatically injects dependencies into the constructor as params
    public ExampleController(HttpResponseFactory response,
                                        ExampleService exampleService) {
        // assign dependencies to class attributes
        this.response = response;
        this.exampleService = exampleService;
    }

    @GetMapping("hello-world") //Maps and endpoint to a method, GET method in postman
    public ResponseEntity<?> helloWorldEndpoint() {
        var payload = exampleService.helloWorld();
        //Returns formatted JSON response
        return response.build(HttpStatus.OK, "Successfully hit Hello World endpoint!", payload);
    }

    @GetMapping("expect-error")
    public ResponseEntity<?> exampleErrorEndpoint() throws Exception {
        exampleService.throwAnError();
        return response.build(HttpStatus.OK, "Successfully hit Hello World endpoint!");
    }

}
