package org.morriswa.eecs447.service;

import org.morriswa.eecs447.model.UserAccount;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;

public interface ExampleService {
    String helloWorld (UserAccount principal);

    void throwAnError() throws Exception;

    void testImageUpload(MultipartFile file, String path) throws Exception;

    URL testImageDownload(String path);
}
