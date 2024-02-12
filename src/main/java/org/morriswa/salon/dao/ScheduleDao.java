package org.morriswa.salon.dao;

import org.morriswa.salon.model.AppointmentLength;
import org.morriswa.salon.model.AppointmentRequest;

import java.util.List;

public interface ScheduleDao {
    List<AppointmentLength> checkAvailableTimes(AppointmentRequest request);
}
