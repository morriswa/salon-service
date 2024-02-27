package org.morriswa.salon.service;

import org.morriswa.salon.dao.ExampleDao;
import org.morriswa.salon.exception.BadRequestException;
import org.morriswa.salon.model.UserAccount;
import org.morriswa.salon.utility.AmazonS3Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;

@Service
public class ExampleServiceImpl implements ExampleService {

    // Business Logic goes in Service Layer

    private final ExampleDao dao;
    private final AmazonS3Client store;

    @Autowired public ExampleServiceImpl(ExampleDao dao, AmazonS3Client store) {
        this.dao = dao;
        this.store = store;
    }

    @Override
    public String helloWorld(UserAccount principal) {
        return String.format("Hello %s! Your user id is %d", principal.getUsername(), principal.getUserId());
    }

    @Override
    public void throwAnError() throws Exception {
        throw new BadRequestException("This is an expected error!");
    }

    @Override
    public void testImageUpload(MultipartFile file, String path) throws Exception {
//        store.uploadToS3(file.getBytes(), path);
    }

    @Override
    public URL testImageDownload(String path) {
        return store.getSignedObjectUrl(path, 60);
    }
}
