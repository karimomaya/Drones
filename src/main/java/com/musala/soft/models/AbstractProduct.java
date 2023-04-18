package com.musala.soft.models;

import lombok.Data;

import javax.persistence.MappedSuperclass;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
@MappedSuperclass
public abstract class AbstractProduct {
    @NotEmpty(message = "Name is mandatory")
    @Pattern(regexp = "([A-Za-z0-9_-]+)", message = "Name should only contain numbers, letters, underscore and dashes")
    String name;
    @DecimalMin(value = "1.0",  message = "Weight must be at least {value} gr")
    double weight;
    @NotEmpty(message = "Code is mandatory")
    @Pattern(regexp = "([A-Z0-9_]+)", message = "Could should only contain numbers, Upper Case letters and underscore")
    String code;
    String image;
}
