package com.musala.soft.models;

import com.musala.soft.models.enums.Model;
import com.musala.soft.models.enums.State;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.validator.constraints.Range;

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
    @NotNull(message = "Weight is mandatory")
    @Range(min = 100, max = 500, message = "Weight must be between 100 and 500")
    Double weight;
    @ColumnDefault("100.0")
    Double batteryCapacity;
    @ColumnDefault("0")
    State state;
}
