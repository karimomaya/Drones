package com.musala.soft.services.interfaces;

import com.musala.soft.models.Drone;
import com.musala.soft.models.Product;

import java.util.List;

public interface DroneService {
    Drone register(Drone drone);
    Drone load(Drone drone);
    List<Product> checkDroneItems(Drone drone);
    List<Drone> availableDrones();
    double batteryLevel(Drone drone);
}
