package ui.adduser;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import database.DatabaseHandler;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.commons.lang3.RandomStringUtils;
import ui.listusers.ListUsersController;
import ui.login.LoginController;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddUserController implements Initializable {

    private DatabaseHandler handler;

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
    private CheckBox userCheckBox;

    private boolean isInEditMode = false;

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

        Pattern pattern = Pattern.compile("[a-zA-Z0-9]*");
        Matcher matcher1 = pattern.matcher(nUser);
        if (nUser.length() < 3 || !matcher1.matches()){
            Alert emptyAlert = new Alert(Alert.AlertType.ERROR);
            emptyAlert.setHeaderText(null);
            emptyAlert.setContentText("Please enter a correct username.");
            emptyAlert.showAndWait();
            return;
        }

        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern emailPat = Pattern.compile(emailRegex);
        if (!emailPat.matcher(nEmail).matches()){
            Alert emptyAlert = new Alert(Alert.AlertType.ERROR);
            emptyAlert.setHeaderText(null);
            emptyAlert.setContentText("Please enter a correct email address.");
            emptyAlert.showAndWait();
            return;
        }

        if(!Character.isDigit(nPhone.charAt(1)) || nPhone.length() < 11){
            Alert emptyAlert = new Alert(Alert.AlertType.ERROR);
            emptyAlert.setHeaderText(null);
            emptyAlert.setContentText("Please enter a correct phone number.");
            emptyAlert.showAndWait();
            return;
        }

        if(isInEditMode){
            handleEditUser();
            handleCancelButtonPushed();
            return;
        }

        String pw = RandomStringUtils.randomAlphanumeric(8);
        String pwDB = LoginController.hashing(pw);
        String st = null;
        if(!userCheckBox.isSelected()){ //If admin
            st = "INSERT INTO USER VALUES ("
                    + "'" + nUser + "',"
                    + "'" + pwDB + "',"
                    + "'" + false + "',"
                    + "'" + true + "',"
                    + "'" + nName + "',"
                    + "'" + nEmail + "',"
                    + "'" + nAddress + "',"
                    + "'" + nPhone +  "'"
                    + ")";
        }
        else{ //If user
            st = "INSERT INTO USER VALUES ("
                    + "'" + nUser + "',"
                    + "'" + pwDB + "',"
                    + "'" + true + "',"
                    + "'" + true + "',"
                    + "'" + nName + "',"
                    + "'" + nEmail + "',"
                    + "'" + nAddress + "',"
                    + "'" + nPhone +  "'"
                    + ")";
        }

        if(handler.execAction(st)){
            Alert emptyAlert = new Alert(Alert.AlertType.INFORMATION);
            emptyAlert.setHeaderText("User added");
            emptyAlert.setContentText("Successfully added user " + "'" + nUser + "'" + " to the database.");
            emptyAlert.showAndWait();
            emptyAlert.setHeaderText("User password");
            emptyAlert.setContentText("The user's password is: \"" + pw + "\"\nPlease ask the user to make a note of it\nand change it as soon as possible!");
            emptyAlert.showAndWait();
            clear();
        }
        else{
            Alert emptyAlert = new Alert(Alert.AlertType.ERROR);
            emptyAlert.setHeaderText(null);
            emptyAlert.setContentText("Failed to add user to the database.");
            emptyAlert.showAndWait();
        }
    }

    private void handleEditUser() {
        String act = "UPDATE USER SET fullname = '" + fullname.getText() + "', email = '" + email.getText() + "',"
                + " address = '" + address.getText() + "', phone = '" + phonenumber.getText() + "'"
                + " WHERE username = '" + username.getText() + "'";
        if(handler.execAction(act)){
            Alert emptyAlert = new Alert(Alert.AlertType.INFORMATION);
            emptyAlert.setHeaderText(null);
            emptyAlert.setContentText("Successfully updated the user details.");
            emptyAlert.showAndWait();
        }
        else{
            Alert emptyAlert = new Alert(Alert.AlertType.ERROR);
            emptyAlert.setHeaderText(null);
            emptyAlert.setContentText("Failed to update the user details.");
            emptyAlert.showAndWait();
        }
    }

    public void inflateAddUserUI(ListUsersController.User user){
        fullname.setText(user.getFullname());
        username.setText(user.getUsername());
        address.setText(user.getAddress());
        phonenumber.setText(user.getPhone());
        email.setText(user.getEmail());
        username.setEditable(false);
        userCheckBox.setDisable(true);
        addUser.setText("Save");
        isInEditMode = true;
    }

    @FXML
    void handleCancelButtonPushed() {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    private void clear(){
        fullname.setText("");
        username.setText("");
        address.setText("");
        phonenumber.setText("");
        email.setText("");
        userCheckBox.setSelected(true);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        handler = DatabaseHandler.getInstance();
        userCheckBox.setSelected(true);

        rootPane.setFocusTraversable(true);
        rootPane.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ESCAPE)  {
                    ((Stage) rootPane.getScene().getWindow()).close();
                }
            }
        });
    }
}
