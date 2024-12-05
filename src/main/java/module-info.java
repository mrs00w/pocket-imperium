module com.example.pocketimperium {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    opens com.example.pocketimperium to javafx.fxml;
    exports com.example.pocketimperium;
}