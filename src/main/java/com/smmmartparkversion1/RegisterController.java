package com.smmmartparkversion1;

import com.smmmartparkversion1.db.UserRepository;
import com.smmmartparkversion1.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController {

    @FXML private TextField fullNameField;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label registerErrorLabel;
    @FXML private Button registerButton;
    @FXML private Hyperlink backToLoginLink;

    private final UserRepository userRepository = new UserRepository();

    @FXML
    private void handleRegister() {
        String fullName = fullNameField.getText().trim();
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        String confirm = confirmPasswordField.getText();

        if (fullName.isEmpty() || username.isEmpty() || password.isEmpty()) {
            registerErrorLabel.setText("Please fill in all fields.");
            return;
        }

        if (!password.equals(confirm)) {
            registerErrorLabel.setText("Passwords do not match.");
            return;
        }

        if (userRepository.findByUsername(username) != null) {
            registerErrorLabel.setText("That username is already taken.");
            return;
        }

        User newUser = new User(username, password, "CUSTOMER", fullName);
        boolean success = userRepository.createUser(newUser);

        if (success) {
            registerErrorLabel.setText("");
            MainApplication.showLoginView();
        } else {
            registerErrorLabel.setText("Registration failed. Try again.");
        }
    }

    @FXML
    private void handleBackToLogin() {
        MainApplication.showLoginView();
    }
}
