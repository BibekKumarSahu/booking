package com.booking.service.impl;

import com.booking.entities.Route;
import com.booking.payload.RouteDTO;
import com.booking.repository.RouteRepository;
import com.booking.service.RouteService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.Date;
@Service
public class RouteServiceImpl implements RouteService {
    private RouteRepository routeRepository;
    private ModelMapper modelMapper;

    public RouteServiceImpl(RouteRepository routeRepository, ModelMapper modelMapper) {
        this.routeRepository = routeRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public RouteDTO createRoute(RouteDTO routeDTO) {
            Route route = mapToEntity(routeDTO);
            route.setCreatedAt(new Date());
            route.setUpdatedAt(new Date());
            Route save = routeRepository.save(route);
            return mapToDto(save);
        }
        Route mapToEntity(RouteDTO routeDTO){
            return modelMapper.map(routeDTO,Route.class);
        }
        RouteDTO mapToDto(Route route){
            return modelMapper.map(route,RouteDTO.class);
        }
    }
