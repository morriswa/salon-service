package org.morriswa.salon.dao;

import org.morriswa.salon.exception.BadRequestException;
import org.morriswa.salon.model.Appointment;
import org.morriswa.salon.model.ServiceDetails;

import java.util.List;

/**
 * AUTHOR: William A. Morris
 * DATE CREATED: 2024-02-12
 * PURPOSE: Provides a Java interface to maintain client information in database
 */
public interface ClientDao {

    List<Appointment> retrieveScheduledAppointments(Long userId);

    List<Appointment> retrieveUnpaidAppointments(Long userId);

    List<ServiceDetails> searchAvailableService(String searchText);

    ServiceDetails retrieveServiceDetails(Long serviceId) throws BadRequestException;
}
