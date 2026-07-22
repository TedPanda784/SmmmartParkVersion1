package com.smmmartparkversion1.model;

import java.time.LocalDateTime;

public class ParkingSession {
    private int sessionId;
    private String plateNumber;
    private String ownerName;
    private String vehicleType;
    private String slotId;
    private LocalDateTime timeIn;
    private LocalDateTime timeOut;
    private Integer userId;

    public ParkingSession(int sessionId, String plateNumber, String ownerName, String vehicleType,
                          String slotId, LocalDateTime timeIn, LocalDateTime timeOut, Integer userId) {
        this.sessionId = sessionId;
        this.plateNumber = plateNumber;
        this.ownerName = ownerName;
        this.vehicleType = vehicleType;
        this.slotId = slotId;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
        this.userId = userId;
    }

    public int getSessionId() { return sessionId; }
    public String getPlateNumber() { return plateNumber; }
    public String getOwnerName() { return ownerName; }
    public String getVehicleType() { return vehicleType; }
    public String getSlotId() { return slotId; }
    public LocalDateTime getTimeIn() { return timeIn; }
    public LocalDateTime getTimeOut() { return timeOut; }
    public void setTimeOut(LocalDateTime timeOut) { this.timeOut = timeOut; }
    public Integer getUserId() { return userId; }

    public boolean isActive() {
        return timeOut == null;
    }
}