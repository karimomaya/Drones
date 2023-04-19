package com.musala.soft.controllers;

import com.musala.soft.exceptions.RecordNotFoundException;
import com.musala.soft.models.Drone;
import com.musala.soft.models.Product;
import com.musala.soft.models.dto.DroneDto;
import com.musala.soft.models.dto.ProductDto;
import com.musala.soft.services.interfaces.DroneService;
import com.musala.soft.services.interfaces.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/drones")
@Slf4j
public class DroneController {

    @Autowired
    private ModelMapper modelMapper;

    private final DroneService droneService;
    private final ProductService productService;

    public DroneController(DroneService droneService, ProductService productService){
        this.droneService = droneService;
        this.productService = productService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<DroneDto> findById(@PathVariable Long id){
        Drone drone = droneService.findById(id);
        return new ResponseEntity<DroneDto>(modelMapper.map(drone, DroneDto.class), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<DroneDto> register(@RequestBody @Valid DroneDto droneDto){
        log.info("Register a new Drone {}", droneDto);
        Drone drone = modelMapper.map(droneDto, Drone.class);
        drone = droneService.register(drone);
        log.info("Register Drone Successfully {}", drone);
        return new ResponseEntity<DroneDto>(modelMapper.map(drone, DroneDto.class), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<DroneDto> loadDrone(@RequestBody List<@Valid ProductDto> productsDto){
        log.info("Load Product to Drone {}", productsDto);
        if (productsDto.size() == 0) throw new RecordNotFoundException("Cannot deliver empty List");

        List<Product> products = productsDto
                .stream()
                .map(productDto -> modelMapper.map(productDto, Product.class))
                .collect(Collectors.toList());
        productService.saveAll(products);
        Drone drone = droneService.load(products);
        log.info("Find the best drone that can load all of this Products {}", drone);
        return new ResponseEntity<DroneDto>(modelMapper.map(drone, DroneDto.class), HttpStatus.OK);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<List> checkDroneItems(@PathVariable Long id){
        Drone drone = droneService.findById(id);

        if (drone.getTransactionId().isEmpty()) return new ResponseEntity<List>(new ArrayList(), HttpStatus.OK);
        List<Product> products = productService.findByTransactionId(drone.getTransactionId());
        return new ResponseEntity<>(products
                .stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .collect(Collectors.toList()) , HttpStatus.OK);
    }

    @GetMapping("/battery/{id}")
    public ResponseEntity<Double> findDroneBattery(@PathVariable Long id){
        return new ResponseEntity<Double>(droneService.batteryLevel(id), HttpStatus.OK);
    }


}
