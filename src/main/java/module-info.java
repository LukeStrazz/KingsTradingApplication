module com.example.kingstradingapplication {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens com.example.kingstradingapplication to javafx.fxml;
    exports com.example.kingstradingapplication;
}