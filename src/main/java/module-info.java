module com.example.smmmartparkversion1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.smmmartparkversion1 to javafx.fxml;
    exports com.smmmartparkversion1;
}