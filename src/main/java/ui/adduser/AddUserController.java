package ui.adduser;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import database.DatabaseHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.commons.lang3.RandomStringUtils;
import ui.login.LoginController;
import ui.login.LoginFileAccess;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddUserController implements Initializable {

    private DatabaseHandler handler;

    private LoginFileAccess loginFileAccess;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private JFXTextField fullname;

    @FXML
    private JFXTextField username;

    @FXML
    private JFXTextField email;

    @FXML
    private JFXTextField address;

    @FXML
    private JFXTextField phonenumber;

    @FXML
    private JFXButton addUser;

    @FXML
    private JFXButton cancel;

    @FXML
    void handleAddUserButtonPushed() {
        String nName = fullname.getText();
        String nUser = username.getText().toLowerCase();
        String nEmail = email.getText();
        String nAddress = address.getText();
        String nPhone = phonenumber.getText();
        if(nName.isEmpty() || nUser.isEmpty() || nEmail.isEmpty() || nAddress.isEmpty() || nPhone.isEmpty()){
            Alert emptyAlert = new Alert(Alert.AlertType.ERROR);
            emptyAlert.setHeaderText(null);
            emptyAlert.setContentText("Please fill out all fields.");
            emptyAlert.showAndWait();
            return;
        }

        //
        //Add correct email + phone number entry check
        //

        String st = "INSERT INTO USER VALUES ("
                + "'" + nUser + "',"
                + "'" + nName + "',"
                + "'" + nEmail + "',"
                + "'" + nAddress + "',"
                + "'" + nPhone +  "'"
                + ")";
        System.out.println(st);

        if(handler.execAction(st)){
            Alert emptyAlert = new Alert(Alert.AlertType.INFORMATION);
            emptyAlert.setHeaderText("User added");
            emptyAlert.setContentText("Successfully added user " + "'" + nUser + "'" + " to the database.");
            emptyAlert.showAndWait();
            String pw = RandomStringUtils.randomAlphanumeric(8);
            emptyAlert.setHeaderText("User password");
            emptyAlert.setContentText("The user's password is: \"" + pw + "\"\nPlease ask the user to make a note of it\nand change it as soon as possible!");
            loginFileAccess.addUser(nUser, LoginController.hashing(pw), "user");
            emptyAlert.showAndWait();

        }
        else{
            Alert emptyAlert = new Alert(Alert.AlertType.ERROR);
            emptyAlert.setHeaderText(null);
            emptyAlert.setContentText("Failed to add user to the database.");
            emptyAlert.showAndWait();
        }
    }

    @FXML
    void handleCancelButtonPushed() {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loginFileAccess = new LoginFileAccess();
        handler = DatabaseHandler.getInstance();
    }
}
