module com.codemavriks.aptech {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.codemavriks.aptech to javafx.fxml;
    exports com.codemavriks.aptech;
}