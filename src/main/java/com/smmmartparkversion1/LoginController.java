package com.smmmartparkversion1;

import com.smmmartparkversion1.db.UserRepository;
import com.smmmartparkversion1.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label loginErrorLabel;
    @FXML private Button loginButton;
    @FXML private Hyperlink registerLink;

    private final UserRepository userRepository = new UserRepository();

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            loginErrorLabel.setText("Please enter both username and password.");
            return;
        }

        User user = userRepository.findByUsername(username);

        if (user == null) {
            loginErrorLabel.setText("No account found with that username.");
            return;
        }

        if (!user.getPassword().equals(password)) {
            loginErrorLabel.setText("Incorrect password.");
            return;
        }

        loginErrorLabel.setText("");

        if (user.isAdmin()) {
            MainApplication.showAdminView(user);
        } else {
            MainApplication.showCustomerView(user);
        }
    }

    @FXML
    private void handleGoToRegister() {
        MainApplication.showRegisterView();
    }
}