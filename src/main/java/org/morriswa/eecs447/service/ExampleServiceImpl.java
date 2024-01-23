package org.morriswa.eecs447.service;

import org.morriswa.eecs447.dao.ExampleDao;
import org.morriswa.eecs447.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExampleServiceImpl implements ExampleService {

    // Business Logic goes in Service Layer

    private final ExampleDao dao;

    @Autowired public ExampleServiceImpl(ExampleDao dao) {
        this.dao = dao;
    }

    @Override
    public String helloWorld() {
        return "Hello World!";
    }

    @Override
    public void throwAnError() throws Exception {
        throw new BadRequestException("This is an expected error!");
    }
}
