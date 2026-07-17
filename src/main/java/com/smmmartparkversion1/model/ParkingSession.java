package com.smmmartparkversion1.model;

import java.time.LocalDateTime;

public class ParkingSession {
    private Vehicle vehicle;
    private ParkingSlot slot;
    private LocalDateTime timeIn;
    private LocalDateTime timeOut; // null while still parked

    public ParkingSession(Vehicle vehicle, ParkingSlot slot, LocalDateTime timeIn) {
        this.vehicle = vehicle;
        this.slot = slot;
        this.timeIn = timeIn;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public ParkingSlot getSlot() {
        return slot;
    }

    public LocalDateTime getTimeIn() {
        return timeIn;
    }

    public LocalDateTime getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(LocalDateTime timeOut) {
        this.timeOut = timeOut;
    }

    public boolean isActive() {
        return timeOut == null;
    }
}
