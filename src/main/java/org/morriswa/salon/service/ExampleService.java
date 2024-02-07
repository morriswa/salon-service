package org.morriswa.salon.service;

import org.morriswa.salon.model.UserAccount;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;

public interface ExampleService {
    String helloWorld (UserAccount principal);

    void throwAnError() throws Exception;

    void testImageUpload(MultipartFile file, String path) throws Exception;

    URL testImageDownload(String path);
}
