package com.musala.soft.services;

import com.musala.soft.exceptions.RecordNotFoundException;
import com.musala.soft.exceptions.WeightExceedException;
import com.musala.soft.models.Drone;
import com.musala.soft.models.Product;
import com.musala.soft.models.enums.Model;
import com.musala.soft.models.enums.State;
import com.musala.soft.repositories.DroneRepository;
import com.musala.soft.services.interfaces.DroneService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.*;
import java.util.concurrent.*;

@Service
@Slf4j
public class DroneServiceImpl implements DroneService {
    CopyOnWriteArrayList<Drone> availableDrones = new CopyOnWriteArrayList<>();
    PriorityQueue<Drone> workingQueueDrone = new PriorityQueue<Drone>(new Drone.MaxWeightDrone());

    public final DroneRepository droneRepository;

    public DroneServiceImpl(DroneRepository droneRepository){
        this.droneRepository = droneRepository;
    }

    @Override
    public Drone findById(Long id){
        Optional<Drone> droneOptional = droneRepository.findById(id);
        if (!droneOptional.isPresent()) throw new RecordNotFoundException("Drone Not Found");
        return droneOptional.get();
    }

    @Override
    public Drone register(@Valid Drone drone) {
        if (drone.getBatteryCapacity() == null) drone.setBatteryCapacity(100.0);
        if (drone.getState() == null) drone.setState(State.IDLE);
        if(drone.getModel() == null) drone.setModel(Model.Lightweight);
        drone = droneRepository.save(drone);
        if (drone.getState() != State.IDLE) workingQueueDrone.add(drone);
        else if (drone.getBatteryCapacity() > 20 ) availableDrones.add(drone);
        return drone;
    }

    @Override
    public Drone load(List<Product> products) {
        Collections.sort(products, (Product a1, Product a2) -> a1.getWeight() < a2.getWeight() ? -1 : a1.getWeight() > a2.getWeight() ? 1  : 0);
        log.info("Products list after sorting {}", products);
        if (availableDrones.size() == 0) throw new RecordNotFoundException("No available drones can serve your list");
        Drone drone = selectBestMatch(products);
        drone.setTransactionId(products.get(0).getTransactionId());
        droneRepository.save(drone);
        workingQueueDrone.add(drone);
        return drone;
    }


    @Override
    public List<Drone> availableDrones() {
        return new ArrayList<>(availableDrones);
    }

    @Override
    public double batteryLevel(Long id) {
        if (id == 0) throw new RecordNotFoundException("Cannot find drone with id 0");
        Drone drone = findById(id);
        return drone.getBatteryCapacity();
    }


    private Drone selectBestMatch(List<Product> products){
        Queue<Drone> minQueue = new PriorityQueue(new Drone.MinWeightDrone());
        double weight = products.stream().mapToDouble(product -> product.getWeight()).sum();

        availableDrones.stream().forEach(drone -> {
            if (drone.getWeight()>= weight && drone.getBatteryCapacity() > 20) {
                minQueue.add(drone);
                availableDrones.remove(drone);
            }
        });
        //@TODO handle break down list of products to multiple list
        if (minQueue.isEmpty() ) throw new WeightExceedException("No available drones can load all of this products; you can break down your list or adding other drones that can load all of this items");
        Drone drone = minQueue.remove();
        while (!minQueue.isEmpty()) availableDrones.add(minQueue.remove());

        return drone;
    }

    private void loadingDrone(Drone drone){
        drone.setState(State.LOADING);
        droneRepository.save(drone);
        log.info("Loading Products start {}", drone);
        ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);
        exec.schedule(()-> {
            drone.setState(State.LOADED);
            droneRepository.save(drone);
            log.info("Drone is loaded Successfully {}", drone);
            deliveringDrone(drone);
        }, 1000, TimeUnit.SECONDS);
    }

    private void deliveringDrone(Drone drone){
        drone.setState(State.DELIVERING);
        droneRepository.save(drone);
        log.info("Delivering product start {}", drone);
        ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);
        exec.schedule(()-> {
            drone.setState(State.DELIVERED);
            drone.setTransactionId("");
            droneRepository.save(drone);
            log.info("Products are delivered Successfully {}", drone);
            returningDrone(drone);
        }, 1000, TimeUnit.SECONDS);
    }

    private void returningDrone(Drone drone){
        drone.setState(State.RETURNING);
        droneRepository.save(drone);
        log.info("Drone is returning {}", drone);
        ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);
        exec.schedule(()-> {
            drone.setState(State.IDLE);
            drone.setBatteryCapacity(drone.getBatteryCapacity()-20);
            droneRepository.save(drone);
            log.info("Drone is returned Successfully {}", drone);
            if (drone.getBatteryCapacity()>20) availableDrones.add(drone);
        }, 1000, TimeUnit.SECONDS);
    }

    @Scheduled(fixedDelayString = "${interval}")
    public void workingWithDroneQueue() throws InterruptedException {

       while (!workingQueueDrone.isEmpty()){
           Drone drone = workingQueueDrone.remove();
           log.info("Time to work with drone {}", drone);
           loadingDrone(drone);
       }
    }

    @EventListener(ApplicationReadyEvent.class)
    private void loadData(){
        droneRepository.findAll().forEach(drone -> {
            if (drone.getState()== State.IDLE && drone.getBatteryCapacity() > 20) availableDrones.add(drone);
        });
    }
}
