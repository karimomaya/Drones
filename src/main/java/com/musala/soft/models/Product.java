package com.musala.soft.models;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    @NotBlank(message = "Name is mandatory")
    @Pattern(regexp = "([A-Za-z0-9_-]+)", message = "Name should only contain numbers, letters, underscore and dashes")
    String name;
    double weight;
    @NotBlank(message = "Code is mandatory")
    @Pattern(regexp = "([A-Z0-9_]+)", message = "Could should only contain numbers, Upper Case letters and underscore")
    String code;
    String image;
}
