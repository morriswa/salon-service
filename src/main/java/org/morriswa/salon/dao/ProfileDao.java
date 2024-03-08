package org.morriswa.salon.dao;

import org.morriswa.salon.exception.BadRequestException;
import org.morriswa.salon.exception.ValidationException;
import org.morriswa.salon.model.ClientInfo;
import org.morriswa.salon.model.EmployeeInfo;


/**
 * provides an interface for interacting with the database to perform essential Client/Employee Profile actions
 *
 * @author William A. Morris, Kevin Rivers
 * @since 2024-01-21
 */
public interface ProfileDao {

    /**
     * @param userId to retrieve client information from db
     * @return all stored client info
     * @throws Exception if requested info could not be retrieved from database
     *
     * @author Kevin Rivers
     */
    ClientInfo getClientInfo(Long userId) throws Exception;

    void updateClientInfo(Long userId, ClientInfo request) throws Exception;

    EmployeeInfo getEmployeeInfo(Long employeeId) throws BadRequestException;

    void updateEmployeeProfile(Long userId, EmployeeInfo request) throws ValidationException;
}
