package com.smmmartparkversion1.db;

import com.smmmartparkversion1.model.ParkingSlot;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ParkingSlotRepository {

    public List<ParkingSlot> getAllSlots() {
        List<ParkingSlot> slots = new ArrayList<>();
        String sql = "SELECT * FROM parking_slot";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                slots.add(new ParkingSlot(
                        rs.getString("slot_id"),
                        rs.getString("compatible_type"),
                        rs.getBoolean("occupied")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return slots;
    }

    public List<ParkingSlot> getAvailableSlotsByType(String type) {
        List<ParkingSlot> slots = new ArrayList<>();
        String sql = "SELECT * FROM parking_slot WHERE compatible_type = ? AND occupied = FALSE";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, type);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                slots.add(new ParkingSlot(
                        rs.getString("slot_id"),
                        rs.getString("compatible_type"),
                        rs.getBoolean("occupied")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return slots;
    }

    public boolean addSlot(ParkingSlot slot) {
        String sql = "INSERT INTO parking_slot (slot_id, compatible_type, occupied) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, slot.getSlotId());
            stmt.setString(2, slot.getCompatibleVehicleType());
            stmt.setBoolean(3, slot.isOccupied());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean setOccupied(String slotId, boolean occupied) {
        String sql = "UPDATE parking_slot SET occupied = ? WHERE slot_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBoolean(1, occupied);
            stmt.setString(2, slotId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteSlot(String slotId) {
        String sql = "DELETE FROM parking_slot WHERE slot_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, slotId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}