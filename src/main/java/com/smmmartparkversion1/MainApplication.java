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

        User existingUser = SessionManager.loadSession();
        if (existingUser != null) {
            if (existingUser.isAdmin()) {
                showAdminView(existingUser);
            } else {
                showCustomerView(existingUser);
            }
        } else {
            showLoginView();
        }
    }

    public static void showLoginView() {
        loadScene("login-view.fxml", "Smart Parking - Login");
    }

    public static void showRegisterView() {
        loadScene("register-view.fxml", "Smart Parking - Register");
    }

    public static void showAdminView(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("main-view.fxml"));
            Scene scene = new Scene(loader.load());
            MainController controller = loader.getController();
            controller.setCurrentUser(user);
            primaryStage.setTitle("Smart Parking - Admin Dashboard");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void showCustomerView(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("customer-view.fxml"));
            Scene scene = new Scene(loader.load());
            CustomerController controller = loader.getController();
            controller.setCurrentUser(user);
            primaryStage.setTitle("Smart Parking - Customer");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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