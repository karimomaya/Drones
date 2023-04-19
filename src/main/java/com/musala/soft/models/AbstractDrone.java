package com.musala.soft.models;

import com.musala.soft.models.enums.Model;
import com.musala.soft.models.enums.State;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.MappedSuperclass;
import javax.validation.constraints.*;

@Data
@MappedSuperclass
public abstract class AbstractDrone {

    @NotBlank(message = "Serial Number is mandatory")
    @Size(min = 2, message = "Serial Number is too short it accept at least 2 character length")
    @Size(max = 100,message = "Serial Number is too long it accept only 100 character length")
    String serialNumber;
    @NotNull(message = "Model is mandatory")
    Model model;
    @DecimalMax(value = "500.0",  message = "Weight must be {value} gr max")
    @DecimalMin(value = "100.0",  message = "Weight must be at least {value} gr")
    Double weight;
    @ColumnDefault("100.0")
    Double batteryCapacity;
    @ColumnDefault("0")
    State state;
}
