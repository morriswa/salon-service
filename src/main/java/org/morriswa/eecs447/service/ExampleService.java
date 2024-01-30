package org.morriswa.eecs447.service;

import org.morriswa.eecs447.model.ApplicationUser;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;

public interface ExampleService {
    String helloWorld (ApplicationUser principal);

    void throwAnError() throws Exception;

    void testImageUpload(MultipartFile file, String path) throws Exception;

    URL testImageDownload(String path);
}
