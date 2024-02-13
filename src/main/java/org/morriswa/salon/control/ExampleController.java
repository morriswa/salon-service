package org.morriswa.salon.control;

import org.morriswa.salon.model.UserAccount;
import org.morriswa.salon.service.ExampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController //Indicates that the class is code for a REST controller ( Provides endpoints to user )
public class ExampleController {

    // Web logic goes in Control layer

    private final ExampleService exampleService;

    @Autowired // Automatically injects dependencies into the constructor as params
    public ExampleController(ExampleService exampleService) {
        // assign dependencies to class attributes
        this.exampleService = exampleService;
    }

    @GetMapping("hello-world") //Maps and endpoint to a method, GET method in postman
    public ResponseEntity<?> helloWorldEndpoint(@AuthenticationPrincipal UserAccount principal) {
        var payload = exampleService.helloWorld(principal);
        //Returns formatted JSON response
        return ResponseEntity.ok(payload);
    }

    @GetMapping("expect-error")
    public ResponseEntity<?> exampleErrorEndpoint() throws Exception {
        exampleService.throwAnError();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("test-image-upload")
    public ResponseEntity<?> testImageUpload(@RequestPart MultipartFile file, @RequestParam String path) throws Exception {
        exampleService.testImageUpload(file, path);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("test-image-upload")
    public ResponseEntity<?> testImageDownload(@RequestParam String path) throws Exception {
        var url = exampleService.testImageDownload(path);
        return ResponseEntity.ok(url);
    }
}
