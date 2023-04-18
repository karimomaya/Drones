package com.musala.soft.models;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Product extends AbstractProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

}
