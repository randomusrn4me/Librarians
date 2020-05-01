package about;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import database.DatabaseHandler;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import ui.listbooks.ListBooksController;

import java.net.URL;
import java.util.Calendar;
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
