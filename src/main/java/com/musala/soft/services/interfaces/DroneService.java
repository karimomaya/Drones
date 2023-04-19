package com.musala.soft.services.interfaces;

import com.musala.soft.models.Drone;
import com.musala.soft.models.Product;

import java.util.List;

public interface DroneService {
    Drone findById(Long id);
    Drone register(Drone drone);
    Drone load(List<Product> products);
    List<Drone> availableDrones();
    double batteryLevel(Long id);
}
