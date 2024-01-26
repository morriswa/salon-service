package org.morriswa.eecs447.service;

import org.springframework.web.multipart.MultipartFile;

import java.net.URL;

public interface ExampleService {
    String helloWorld ();

    void throwAnError() throws Exception;

    void testImageUpload(MultipartFile file, String path) throws Exception;

    URL testImageDownload(String path);
}
