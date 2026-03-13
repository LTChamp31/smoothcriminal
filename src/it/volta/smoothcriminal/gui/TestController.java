package it.volta.smoothcriminal.gui;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
public class TestController {

    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("HELLO!");
    }
}
