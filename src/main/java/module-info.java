module com.example.cashcounterapp2 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.cashcounterapp2 to javafx.fxml;
    exports com.example.cashcounterapp2;
}