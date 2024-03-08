package org.morriswa.salon.service;

import org.morriswa.salon.exception.BadRequestException;
import org.morriswa.salon.exception.ValidationException;
import org.morriswa.salon.model.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * AUTHOR: William A. Morris <br>
 * CREATION_DATE: 2024-01-22 <br>
 * PURPOSE: <br>
 * &emsp; provides an interface for performing essential User actions
 */
public interface ProfileService {

    ClientInfo getClientProfile(UserAccount principal) throws Exception;

    void updateClientProfile(UserAccount principal, ClientInfo updateProfileRequest) throws Exception;

    EmployeeProfileResponse getEmployeeProfile(UserAccount principal) throws Exception;

    PublicEmployeeProfileResponse getPublicEmployeeProfile(Long employeeId) throws BadRequestException;

    void updateEmployeeProfile(UserAccount principal, EmployeeInfo request) throws ValidationException;

    void changeEmployeeProfileImage(UserAccount principal, MultipartFile image) throws Exception;
}
