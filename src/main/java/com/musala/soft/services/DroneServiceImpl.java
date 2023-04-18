package com.musala.soft.services;

import com.musala.soft.models.Drone;
import com.musala.soft.models.Product;
import com.musala.soft.services.interfaces.DroneService;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;

@Service
public class DroneServiceImpl implements DroneService {

    @Override
    public Drone register(@Valid Drone drone) {
        return null;
    }

    @Override
    public Drone load(List<Product> products) {
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
