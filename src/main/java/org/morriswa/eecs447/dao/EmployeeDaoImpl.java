package org.morriswa.eecs447.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class EmployeeDaoImpl implements EmployeeDao {
     
    private final NamedParameterJdbcTemplate database;

    @Autowired
    public EmployeeDaoImpl(NamedParameterJdbcTemplate database) {
        this.database = database;
    }
}
