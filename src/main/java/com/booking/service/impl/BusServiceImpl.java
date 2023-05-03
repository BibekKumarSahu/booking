package com.booking.service.impl;

import com.booking.entities.Bus;
import com.booking.entities.BusOperator;
import com.booking.payload.BusDTO;
import com.booking.repository.BusOperatorRepository;
import com.booking.repository.BusRepository;

import com.booking.service.BusService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class BusServiceImpl implements BusService {
    private BusRepository busRepository;
    private ModelMapper modelMapper;
    private BusOperatorRepository busOperatorRepository;

    public BusServiceImpl(BusRepository busRepository, ModelMapper modelMapper, BusOperatorRepository busOperatorRepository) {
        this.busRepository = busRepository;
        this.modelMapper = modelMapper;
        this.busOperatorRepository = busOperatorRepository;
    }

    @Override
    public BusDTO createBus(long busOperatorId, BusDTO busDTO) {
        BusOperator busOperatorid = busOperatorRepository.findById(busOperatorId).orElseThrow(() -> new RuntimeException("Bus Operator Id Not found"));
        Bus bus = mapToEntity(busDTO);
        bus.setCreatedAt(new Date());
        bus.setUpdatedAt(new Date());
        bus.setBusOperator(busOperatorid);
        Bus save = busRepository.save(bus);
        return mapToDto(save);
    }

    Bus mapToEntity(BusDTO busDTO) {
        return modelMapper.map(busDTO, Bus.class);
    }

    BusDTO mapToDto(Bus bus) {
        return modelMapper.map(bus, BusDTO.class);
    }
}
