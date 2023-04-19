package com.musala.soft.models;

import lombok.Data;

import javax.persistence.*;
import java.util.Comparator;


@Data
@Entity
public class Drone extends AbstractDrone  {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    String transactionId;

    public static class MinWeightDrone implements Comparator<Drone> {
        @Override
        public int compare(Drone drone1, Drone drone2) {
            if (drone1.getBatteryCapacity() == null || drone1.getBatteryCapacity() < 20 ) return -1;
            else {
                int val = Double.compare(drone1.weight, drone2.weight);
                if (drone2.getBatteryCapacity() != null &&  drone1.getBatteryCapacity() != null)
                    return val == 0 ? Double.compare(drone1.getBatteryCapacity(), drone2.getBatteryCapacity()) : val;
                return 0;
            }
        }
    }

    public static class MaxWeightDrone implements Comparator<Drone> {
        @Override
        public int compare(Drone drone1, Drone drone2) {
            if (drone1.getBatteryCapacity() == null || drone1.getBatteryCapacity() < 20 ) return -1;
            else {
                int val = Double.compare(drone2.weight, drone1.weight);
                if (drone2.getBatteryCapacity() != null &&  drone1.getBatteryCapacity() != null)
                    return val == 0 ? Double.compare(drone2.getBatteryCapacity(), drone1.getBatteryCapacity()) : val;
                return val;
            }
        }
    }

}
