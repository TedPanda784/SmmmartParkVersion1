package com.smmmartparkversion1.model;

public class ParkingSlot {
    private String slotId;
    private String compatibleVehicleType; // "Car", "Motorcycle", "Truck"
    private boolean occupied;

    public ParkingSlot(String slotId, String compatibleVehicleType) {
        this.slotId = slotId;
        this.compatibleVehicleType = compatibleVehicleType;
        this.occupied = false;
    }

    public String getSlotId() {
        return slotId;
    }

    public String getCompatibleVehicleType() {
        return compatibleVehicleType;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    @Override
    public String toString() {
        return slotId + " (" + compatibleVehicleType + ") - " + (occupied ? "Occupied" : "Available");
    }
}