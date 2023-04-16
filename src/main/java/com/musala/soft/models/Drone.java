package com.musala.soft.models;

import com.musala.soft.models.enums.Model;
import com.musala.soft.models.enums.State;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;

@Data
@Entity
public class Drone {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    @NotBlank(message = "Serial Number is mandatory")
    @Max(value=100, message = "There must be at max {value} for Serial Number")
    String serialNumber;
    @NotBlank(message = "Model is mandatory")
    Model model;
    @Max(value=500, message = "Weight must be {value} gr max")
    double weight;
    double batteryCapacity;
    State state;

}
