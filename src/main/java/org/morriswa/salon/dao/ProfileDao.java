package org.morriswa.salon.dao;

import org.morriswa.salon.exception.BadRequestException;
import org.morriswa.salon.exception.ValidationException;
import org.morriswa.salon.model.ClientInfo;
import org.morriswa.salon.model.EmployeeInfo;


/**
 * AUTHOR: William A. Morris, Kevin Rivers <br>
 * CREATION_DATE: 2024-01-21 <br>
 * PURPOSE: <br>
 * &emsp; provides an interface for interacting with the database to perform essential User actions
 */

public interface ProfileDao {

    ClientInfo getClientInfo(Long userId) throws Exception;

    void updateClientInfo(Long userId, ClientInfo request) throws Exception;

    EmployeeInfo getEmployeeInfo(Long employeeId) throws BadRequestException;

    void updateEmployeeProfile(Long userId, EmployeeInfo request) throws ValidationException;
}
