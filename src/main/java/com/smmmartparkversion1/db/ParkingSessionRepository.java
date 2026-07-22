package com.smmmartparkversion1.db;

import com.smmmartparkversion1.model.ParkingSession;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ParkingSessionRepository {

    private final ParkingSlotRepository slotRepository = new ParkingSlotRepository();

    public boolean parkVehicle(String plate, String owner, String vehicleType, String slotId, Integer userId) {
        String sql = "INSERT INTO parking_session (plate_number, owner_name, vehicle_type, slot_id, time_in, user_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, plate);
            stmt.setString(2, owner);
            stmt.setString(3, vehicleType);
            stmt.setString(4, slotId);
            stmt.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            if (userId != null) stmt.setInt(6, userId); else stmt.setNull(6, Types.INTEGER);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                slotRepository.setOccupied(slotId, true);
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<ParkingSession> getAllSessions() {
        return querySessions("SELECT * FROM parking_session ORDER BY time_in DESC", null);
    }

    public List<ParkingSession> getSessionsByUser(int userId) {
        return querySessions("SELECT * FROM parking_session WHERE user_id = ? ORDER BY time_in DESC", userId);
    }

    private List<ParkingSession> querySessions(String sql, Integer userId) {
        List<ParkingSession> sessions = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (userId != null) stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Timestamp timeOutTs = rs.getTimestamp("time_out");
                sessions.add(new ParkingSession(
                        rs.getInt("session_id"),
                        rs.getString("plate_number"),
                        rs.getString("owner_name"),
                        rs.getString("vehicle_type"),
                        rs.getString("slot_id"),
                        rs.getTimestamp("time_in").toLocalDateTime(),
                        timeOutTs != null ? timeOutTs.toLocalDateTime() : null,
                        rs.getObject("user_id") != null ? rs.getInt("user_id") : null
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sessions;
    }

    public boolean endSession(int sessionId, String slotId) {
        String sql = "UPDATE parking_session SET time_out = ? WHERE session_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setInt(2, sessionId);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                slotRepository.setOccupied(slotId, false);
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteSession(int sessionId) {
        String sql = "DELETE FROM parking_session WHERE session_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, sessionId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}