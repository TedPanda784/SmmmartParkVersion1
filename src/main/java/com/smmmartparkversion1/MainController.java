package com.smmmartparkversion1;

import com.smmmartparkversion1.db.ParkingSessionRepository;
import com.smmmartparkversion1.db.ParkingSlotRepository;
import com.smmmartparkversion1.model.ParkingSession;
import com.smmmartparkversion1.model.ParkingSlot;
import com.smmmartparkversion1.model.User;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class MainController {

    @FXML private TextField plateField;
    @FXML private TextField ownerField;
    @FXML private ComboBox<String> vehicleTypeCombo;
    @FXML private ComboBox<ParkingSlot> slotCombo;
    @FXML private Label errorLabel;

    @FXML private TableView<ParkingSession> sessionTable;
    @FXML private TableColumn<ParkingSession, String> plateColumn;
    @FXML private TableColumn<ParkingSession, String> typeColumn;
    @FXML private TableColumn<ParkingSession, String> slotColumn;
    @FXML private TableColumn<ParkingSession, String> timeInColumn;
    @FXML private TableColumn<ParkingSession, String> statusColumn;

    private final ParkingSlotRepository slotRepository = new ParkingSlotRepository();
    private final ParkingSessionRepository sessionRepository = new ParkingSessionRepository();

    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("MMM d, HH:mm");

    // Set by MainApplication right after loading this screen, so we know who's logged in
    private User currentUser;

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    @FXML
    public void initialize() {
        vehicleTypeCombo.setItems(FXCollections.observableArrayList("Car", "Motorcycle", "Truck"));

        vehicleTypeCombo.valueProperty().addListener((obs, oldVal, newVal) -> refreshAvailableSlots(newVal));

        plateColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getPlateNumber()));
        typeColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getVehicleType()));
        slotColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getSlotId()));
        timeInColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getTimeIn().format(TIME_FORMAT)));
        statusColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().isActive() ? "Parked" : "Completed"));

        refreshSessionTable();
    }

    private void refreshSessionTable() {
        List<ParkingSession> sessions = sessionRepository.getAllSessions();
        sessionTable.setItems(FXCollections.observableArrayList(sessions));
    }

    private void refreshAvailableSlots(String vehicleType) {
        if (vehicleType == null) {
            slotCombo.setItems(FXCollections.observableArrayList());
            return;
        }
        List<ParkingSlot> available = slotRepository.getAvailableSlotsByType(vehicleType);
        slotCombo.setItems(FXCollections.observableArrayList(available));
    }

    @FXML
    private void handleParkVehicle() {
        String plate = plateField.getText().trim();
        String owner = ownerField.getText().trim();
        String type = vehicleTypeCombo.getValue();
        ParkingSlot slot = slotCombo.getValue();

        if (plate.isEmpty() || owner.isEmpty() || type == null || slot == null) {
            errorLabel.setText("Please fill in all fields and select a slot.");
            return;
        }

        boolean success = sessionRepository.parkVehicle(plate, owner, type, slot.getSlotId(),
                currentUser != null ? currentUser.getUserId() : null);

        if (success) {
            errorLabel.setText("");
            handleClear();
            refreshSessionTable();
            refreshAvailableSlots(vehicleTypeCombo.getValue());
        } else {
            errorLabel.setText("Failed to park vehicle. Try again.");
        }
    }

    @FXML
    private void handleEndSession() {
        ParkingSession selected = sessionTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            errorLabel.setText("Select a parked vehicle to end its session.");
            return;
        }
        if (!selected.isActive()) {
            errorLabel.setText("This session has already ended.");
            return;
        }
        sessionRepository.endSession(selected.getSessionId(), selected.getSlotId());
        errorLabel.setText("");
        refreshSessionTable();
        refreshAvailableSlots(vehicleTypeCombo.getValue());
    }

    @FXML
    private void handleClear() {
        plateField.clear();
        ownerField.clear();
        vehicleTypeCombo.setValue(null);
        slotCombo.setValue(null);
        errorLabel.setText("");
    }

    @FXML
    private void handleLogout() {
        SessionManager.clearSession();
        MainApplication.showLoginView();
    }
}
