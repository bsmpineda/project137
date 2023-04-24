module proj137 {
    requires javafx.controls;
    requires javafx.fxml;

    opens proj137 to javafx.fxml;
    exports proj137;
}
