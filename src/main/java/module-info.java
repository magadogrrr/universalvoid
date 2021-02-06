module org.torrenzo {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.torrenzo to javafx.fxml;
    exports org.torrenzo;
}