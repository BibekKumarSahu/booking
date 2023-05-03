package com.booking.service;

import com.booking.entities.Bus;
import com.booking.payload.BusDTO;

public interface BusService {

    BusDTO createBus(long busOperatorId, BusDTO busDTO);

}
