package org.morriswa.eecs447.control;

import org.morriswa.eecs447.model.UserAccount;
import org.morriswa.eecs447.service.ExampleService;
import org.morriswa.eecs447.utility.HttpResponseFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<?> helloWorldEndpoint(@AuthenticationPrincipal UserAccount principal) {
        var payload = exampleService.helloWorld(principal);
        //Returns formatted JSON response
        return response.build(HttpStatus.OK, "Successfully hit Hello World endpoint!", payload);
    }

    @GetMapping("expect-error")
    public ResponseEntity<?> exampleErrorEndpoint() throws Exception {
        exampleService.throwAnError();
        return response.build(HttpStatus.OK, "Successfully hit Hello World endpoint!");
    }

    @PostMapping("test-image-upload")
    public ResponseEntity<?> testImageUpload(@RequestPart MultipartFile file, @RequestParam String path) throws Exception {
        exampleService.testImageUpload(file, path);
        return response.build(HttpStatus.CREATED, "Successfully uploaded requested file!");
    }

    @GetMapping("test-image-upload")
    public ResponseEntity<?> testImageDownload(@RequestParam String path) throws Exception {
        var url = exampleService.testImageDownload(path);
        return response.build(HttpStatus.OK, "Successfully retrieved requested file!", url);
    }
}
