package com.musala.soft.models;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class Drone extends AbstractDrone{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

}
