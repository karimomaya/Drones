package com.musala.soft.exceptions;

public class BatteryLowException extends RuntimeException {
    public BatteryLowException(String exception) {
        super(exception);
    }
}
