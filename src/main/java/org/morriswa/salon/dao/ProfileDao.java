package org.morriswa.salon.dao;

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

    /**
     * updates a client's stored info in mysql
     *
     * @param userId of the client to update
     * @param request containing information to update
     * @throws Exception if client info could not be updated
     */
    void updateClientInfo(Long userId, ClientInfo request) throws Exception;

    /**
     * retrieves all stored employee info in mysql
     *
     * @param employeeId to retrieve info about
     * @return all stored employee info
     * @throws Exception if employee info could not be retrieved
     */
    EmployeeInfo getEmployeeInfo(Long employeeId) throws Exception;

    /**
     * updates an employee's stored info in mysql
     *
     * @param employeeId of the employee to update
     * @param request containing information to update
     * @throws Exception if employee info could not be updated
     */
    void updateEmployeeProfile(Long employeeId, EmployeeInfo request) throws Exception;
}
