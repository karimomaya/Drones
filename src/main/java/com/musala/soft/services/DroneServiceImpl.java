package com.musala.soft.services;

import com.musala.soft.models.Drone;
import com.musala.soft.models.Product;
import com.musala.soft.services.interfaces.DroneService;

import java.util.List;

public class DroneServiceImpl implements DroneService {
    @Override
    public Drone register(Drone drone) {
        return null;
    }

    @Override
    public Drone load(Drone drone) {
        return null;
    }

    @Override
    public List<Product> checkDroneItems(Drone drone) {
        return null;
    }

    @Override
    public List<Drone> availableDrones() {
        return null;
    }

    @Override
    public double batteryLevel(Drone drone) {
        return 0;
    }
}
