package com.musala.soft.repositories;

import com.musala.soft.models.Drone;
import org.springframework.stereotype.Repository;

@Repository
public interface DroneRepository  extends GenericRepository<Drone, Long>{
}
