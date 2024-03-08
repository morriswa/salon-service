package org.morriswa.salon.service;

import org.morriswa.salon.model.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * provides an interface for performing essential Client/Employee Profile actions
 *
 * @author William A. Morris
 * @since 2024-01-22
 */
public interface ProfileService {

    /**
     * retrieves a client's profile
     *
     * @param principal of the authenticated client
     * @return all saved client info
     * @throws Exception if requested information could not be retrieved
     */
    ClientInfo getClientProfile(UserAccount principal) throws Exception;

    /**
     * retrieves an employee's profile
     *
     * @param principal the authenticated employee
     * @return all saved employee info
     * @throws Exception if requested information could not be retrieved
     */
    EmployeeProfile getEmployeeProfile(UserAccount principal) throws Exception;

    /**
     * retrieves an employee's publicly available profile
     *
     * @param employeeId of the employee to retrieve
     * @return all publicly available employee info
     * @throws Exception if requested information could not be retrieved
     */
    PublicEmployeeProfile getPublicEmployeeProfile(Long employeeId) throws Exception;

    /**
     * updates a client's profile
     *
     * @param principal of the authenticated client
     * @param updateProfileRequest containing new info to validate and store
     * @throws Exception if client information could not be updated
     */
    void updateClientProfile(UserAccount principal, ClientInfo updateProfileRequest) throws Exception;

    /**
     * updates an employee's profile
     *
     * @param principal the authenticated employee
     * @param request containing new info to validate and store
     * @throws Exception if employee information could not be updated
     */
    void updateEmployeeProfile(UserAccount principal, EmployeeInfo request) throws Exception;

    /**
     * updates an employee's profile image
     *
     * @param principal the authenticated employee
     * @param image new profile image
     * @throws Exception if employee profile image could not be updated
     */
    void updateEmployeeProfileImage(UserAccount principal, MultipartFile image) throws Exception;

}
