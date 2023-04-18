package com.musala.soft.controllers;

import com.musala.soft.models.Drone;
import com.musala.soft.models.dto.DroneDto;
import com.musala.soft.services.interfaces.DroneService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/drones")
@Slf4j
public class DroneController {

    @Autowired
    private ModelMapper modelMapper;

    public final DroneService droneService;

    public DroneController(DroneService droneService){
        this.droneService = droneService;
    }

    @PostMapping
    public ResponseEntity<DroneDto> register(@RequestBody @Valid DroneDto droneDto){
        log.info("Register a new Drone {}", droneDto);
        Drone drone = modelMapper.map(droneDto, Drone.class);
        drone = droneService.register(drone);
        log.info("Register Drone Successfully {}", drone);
        return new ResponseEntity<DroneDto>(modelMapper.map(drone, DroneDto.class), HttpStatus.CREATED);
    }
}
