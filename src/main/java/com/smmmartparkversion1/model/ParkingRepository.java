package com.smmmartparkversion1.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ParkingRepository {
    private final ObservableList<ParkingSlot> slots = FXCollections.observableArrayList();
    private final ObservableList<ParkingSession> sessions = FXCollections.observableArrayList();

    public ObservableList<ParkingSlot> getSlots() {
        return slots;
    }

    public ObservableList<ParkingSession> getSessions() {
        return sessions;
    }

    public void addSlot(ParkingSlot slot) {
        slots.add(slot);
    }

    public ParkingSession parkVehicle(Vehicle vehicle, ParkingSlot slot) {
        slot.setOccupied(true);
        ParkingSession session = new ParkingSession(vehicle, slot, java.time.LocalDateTime.now());
        sessions.add(session);
        return session;
    }

    public void endSession(ParkingSession session) {
        session.setTimeOut(java.time.LocalDateTime.now());
        session.getSlot().setOccupied(false);
    }
}
