package com.booking.service;

import com.booking.entities.Route;
import com.booking.payload.RouteDTO;

public interface RouteService {

    RouteDTO createRoute(RouteDTO routeDTO);
}
