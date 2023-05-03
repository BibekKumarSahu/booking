package com.booking.service.impl;

import com.booking.entities.BusOperator;
import com.booking.payload.BusOperatorDTO;
import com.booking.repository.BusOperatorRepository;
import com.booking.service.BusOperatorService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class BusOperatorServiceImpl implements BusOperatorService {
    private BusOperatorRepository busOperatorRepository;
    private ModelMapper modelMapper;

    public BusOperatorServiceImpl(BusOperatorRepository busOperatorRepository, ModelMapper modelMapper) {
        this.busOperatorRepository = busOperatorRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public BusOperatorDTO createBusOperator(BusOperatorDTO busOperatorDTO) {
        BusOperator busOperator = mapToEntity(busOperatorDTO);
        busOperator.setCreatedAt(new Date());
        busOperator.setUpdatedAt(new Date());
        busOperatorRepository.save(busOperator);
        return mapToDto(busOperator);
    }

    BusOperator mapToEntity(BusOperatorDTO busOperatorDTO) {
        return modelMapper.map(busOperatorDTO, BusOperator.class);
    }

    BusOperatorDTO mapToDto(BusOperator busOperator) {
        return modelMapper.map(busOperator, BusOperatorDTO.class);
    }
}

