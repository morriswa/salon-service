package org.morriswa.salon.dao;

import org.morriswa.salon.exception.BadRequestException;
import org.morriswa.salon.exception.ValidationException;
import org.morriswa.salon.model.EmployeeInfo;
import org.morriswa.salon.model.UserAccount;
import org.morriswa.salon.model.ContactInfo;


/**
 * AUTHOR: William A. Morris, Kevin Rivers <br>
 * CREATION_DATE: 2024-01-21 <br>
 * PURPOSE: <br>
 * &emsp; provides an interface for interacting with the database to perform essential User actions
 */

public interface UserProfileDao {

    ContactInfo getContactInfo(Long userId) throws Exception;

    void updateUserContactInfo(Long userId, ContactInfo request) throws Exception;

    EmployeeInfo getEmployeeInfo(Long employeeId) throws BadRequestException;

    void updateEmployeeProfile(Long userId, EmployeeInfo request) throws ValidationException;
}
