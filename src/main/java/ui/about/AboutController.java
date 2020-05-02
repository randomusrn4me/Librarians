package ui.about;

import utils.*;
import com.jfoenix.controls.JFXButton;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AboutController implements Initializable {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private JFXButton okButton;

    @FXML
    void handleOK() {
        ((Stage) rootPane.getScene().getWindow()).close();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rootPane.setFocusTraversable(true);
        rootPane.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ESCAPE || keyEvent.getCode() == KeyCode.ENTER)  {
                    handleOK();
                }
            }
        });
    }

}
