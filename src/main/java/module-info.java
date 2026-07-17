module com.example.smmmartparkversion1 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.smmmartparkversion1 to javafx.fxml;
    exports com.example.smmmartparkversion1;
}