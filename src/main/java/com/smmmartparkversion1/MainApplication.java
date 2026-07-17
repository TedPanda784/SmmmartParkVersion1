package com.smmmartparkversion1;

import com.smmmartparkversion1.model.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        showLoginView();
    }

    public static void showLoginView() {
        loadScene("login-view.fxml", "Smart Parking - Login");
    }

    public static void showRegisterView() {
        loadScene("register-view.fxml", "Smart Parking - Register");
    }

    public static void showAdminView(User user) {
        // Temporary: reuses the existing dashboard for now.
        // We'll pass the logged-in user into MainController in Step 10.
        loadScene("main-view.fxml", "Smart Parking - Admin Dashboard");
    }

    public static void showCustomerView(User user) {
        // Placeholder until we build a dedicated customer screen in Step 10.
        loadScene("main-view.fxml", "Smart Parking - Customer");
    }

    private static void loadScene(String fxmlFile, String title) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(fxmlFile));
            Scene scene = new Scene(fxmlLoader.load());
            primaryStage.setTitle(title);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
