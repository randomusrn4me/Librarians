package ui.userpanel;

import database.DatabaseHandler;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import database.DatabaseHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.InputEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import ui.listissued.ListIssuedController;
import ui.listissued.ListIssuedController;
import ui.mainframe.MainframeController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EditUserDetailsController implements Initializable {

    private String receivedUser;

    public void setReceivedUser(String receivedUser) {
        this.receivedUser = receivedUser;
    }

    @FXML
    private JFXTextField usernameBox;

    @FXML
    private JFXPasswordField curPw;

    @FXML
    private JFXPasswordField npw1;

    @FXML
    private JFXPasswordField npw2;

    @FXML
    void pwSaveButtonPushed() {

    }

    @FXML
    void usernameSaveButtonPushed() {

    }

    public void initByHand(String ur){
        usernameBox.setText(ur);
        System.out.println(":" + receivedUser);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
