package com.booking.service.impl;

import com.booking.entities.Bus;
import com.booking.entities.Route;
import com.booking.entities.Schedule;
import com.booking.payload.ScheduleDTO;
import com.booking.repository.BusRepository;
import com.booking.repository.RouteRepository;
import com.booking.repository.ScheduleRepository;
import com.booking.service.ScheduleService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.Date;
@Service
public class ScheduleServiceImpl implements ScheduleService {
    private ScheduleRepository scheduleRepository;
    private BusRepository busRepository;
    private RouteRepository routeRepository;
    private ModelMapper modelMapper;
    public ScheduleServiceImpl(ScheduleRepository scheduleRepository,
                               BusRepository busRepository, RouteRepository routeRepository, ModelMapper
                                       modelMapper) {
        this.scheduleRepository = scheduleRepository;
        this.busRepository = busRepository;
        this.routeRepository = routeRepository;
        this.modelMapper = modelMapper;

    }
    @Override
    public ScheduleDTO createSchedule(long busId, long routeId, ScheduleDTO schedulesDTO) {
        Bus busid = busRepository.findById(busId).orElseThrow(() -> new
                RuntimeException("Bus Id NotFound"));
        Route routeid = routeRepository.findById(routeId).orElseThrow(() ->
                new RuntimeException("Route Id not found"));
        Schedule schedule = mapToEntity(schedulesDTO);
        schedule.setCreatedAt(new Date());
        schedule.setUpdatedAt(new Date());
        schedule.setBus(busid);
        schedule.setRoute(routeid);
        Schedule save = scheduleRepository.save(schedule);
        return mapToDto(save);
    }
    Schedule mapToEntity(ScheduleDTO schedulesDTO){
        return modelMapper.map(schedulesDTO, Schedule.class);
    }
    ScheduleDTO mapToDto(Schedule schedule){
        return modelMapper.map(schedule,ScheduleDTO.class);
    }
}

