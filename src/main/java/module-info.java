module com.example.concessionaria_3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.concessionaria_3 to javafx.fxml;
    exports com.example.concessionaria_3;
}