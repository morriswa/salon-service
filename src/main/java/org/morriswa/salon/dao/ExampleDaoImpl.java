package org.morriswa.salon.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class ExampleDaoImpl implements ExampleDao {

    // D.A.O === Data Access Object
    // Database logic goes in Data Layer

    private final NamedParameterJdbcTemplate database;

    @Autowired public ExampleDaoImpl(NamedParameterJdbcTemplate database) {
        this.database = database;
    }
}
