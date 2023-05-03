package com.booking.service;

import com.booking.entities.Schedule;
import com.booking.payload.ScheduleDTO;

public interface ScheduleService {

    ScheduleDTO createSchedule(long busId, long routeId, ScheduleDTO schedulesDTO);

}
