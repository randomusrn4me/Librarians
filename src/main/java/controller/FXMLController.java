package controller;

import javafx.scene.control.Label;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.util.Random;
import com.jfoenix.controls.JFXButton;

public class FXMLController implements Initializable {

    @FXML
    private JFXButton addBook;

    @FXML
    private JFXButton cancel;

    @FXML
    void handleAddBookButtonPushed() {
        System.out.println("AddBook button pushed!!");
    }

    @FXML
    void handleCancelButtonPushed() {
        System.out.println("Cancel button pushed!!");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
