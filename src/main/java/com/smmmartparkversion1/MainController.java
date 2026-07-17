package com.smmmartparkversion1;

import com.smmmartparkversion1.model.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.time.format.DateTimeFormatter;

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

    // Shared in-memory repository for this run of the app
    private final ParkingRepository repository = new ParkingRepository();

    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("MMM d, HH:mm");

    @FXML
    public void initialize() {
        // Populate vehicle type choices
        vehicleTypeCombo.setItems(FXCollections.observableArrayList("Car", "Motorcycle", "Truck"));

        // Seed a few example slots (no DB yet, so this is in-memory)
        repository.addSlot(new ParkingSlot("A1", "Car"));
        repository.addSlot(new ParkingSlot("A2", "Car"));
        repository.addSlot(new ParkingSlot("B1", "Motorcycle"));
        repository.addSlot(new ParkingSlot("B2", "Motorcycle"));
        repository.addSlot(new ParkingSlot("C1", "Truck"));

        slotCombo.setItems(repository.getSlots());

        // Only offer slots that are available, refreshed whenever vehicle type changes
        vehicleTypeCombo.valueProperty().addListener((obs, oldVal, newVal) -> refreshAvailableSlots(newVal));

        // Table column bindings
        plateColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getVehicle().getPlateNumber()));
        typeColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getVehicle().getVehicleType()));
        slotColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getSlot().getSlotId()));
        timeInColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getTimeIn().format(TIME_FORMAT)));
        statusColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().isActive() ? "Parked" : "Completed"));

        sessionTable.setItems(repository.getSessions());
    }

    private void refreshAvailableSlots(String vehicleType) {
        if (vehicleType == null) {
            slotCombo.setItems(repository.getSlots());
            return;
        }
        slotCombo.setItems(FXCollections.observableArrayList(
                repository.getSlots().stream()
                        .filter(s -> s.getCompatibleVehicleType().equals(vehicleType) && !s.isOccupied())
                        .toList()
        ));
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

        Vehicle vehicle = switch (type) {
            case "Car" -> new Car(plate, owner);
            case "Motorcycle" -> new Motorcycle(plate, owner);
            case "Truck" -> new Truck(plate, owner);
            default -> null;
        };

        if (vehicle == null) {
            errorLabel.setText("Unrecognized vehicle type.");
            return;
        }

        repository.parkVehicle(vehicle, slot);
        errorLabel.setText("");
        handleClear();
        refreshAvailableSlots(vehicleTypeCombo.getValue());
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
        repository.endSession(selected);
        sessionTable.refresh();
        errorLabel.setText("");
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

}
