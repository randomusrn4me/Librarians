package ui.editUserPassword;

import javafx.scene.image.Image;
import utils.*;
import com.jfoenix.controls.JFXButton;
import database.DatabaseHandler;
import com.jfoenix.controls.JFXPasswordField;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ui.addUser.AddUserController;
import ui.listUsers.ListUsersController;
import ui.login.LoginController;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditUserPasswordController implements Initializable {

    private User receivedUserClass;

    private User editedUserClass;

    public void setReceivedUser(User receivedUserClass, User editedUserClass) {
        this.receivedUserClass = receivedUserClass;
        this.editedUserClass = editedUserClass;
    }

    @FXML
    private JFXPasswordField curPw;

    @FXML
    private JFXPasswordField npw1;

    @FXML
    private JFXPasswordField npw2;

    @FXML
    private Text infoBox;

    @FXML
    private Text instructionsBox;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private JFXButton detailsButton;

    @FXML
    void detailsButtonPushed() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ui.add_user.fxml"));
            Parent parent = loader.load();
            AddUserController controller = loader.getController();
            controller.inflateAddUserUI(receivedUserClass);
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Edit Own User Details");
            stage.setScene(new Scene(parent));
            stage.initOwner(rootPane.getScene().getWindow());
            stage.setResizable(false);
            stage.getIcons().add(new Image("icons/library.png"));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void pwSaveButtonPushed() {
        DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
        if(receivedUserClass == null || editedUserClass == null){
            infoBox.setText("Error! The general user\ndoes not have a password!");
            return;
        }

        StringBuilder stringBuilder = new StringBuilder();
        boolean isError = false;
        String current = LoginController.hashing(curPw.getText());
        String check1 = npw1.getText();
        String check2 = npw2.getText();
        String new1 = LoginController.hashing(npw1.getText());
        String new2 = LoginController.hashing(npw2.getText());

        String qu = "SELECT pass FROM USER WHERE username = '" + editedUserClass.getUsername() + "'";
        String passDb = null;
        ResultSet rs = databaseHandler.execQuery(qu);
        while(true){
            try {
                assert rs != null;
                if (!rs.next()) break;
                passDb = rs.getString("pass");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if(!current.equals(passDb) && receivedUserClass.getIsUser()){
            infoBox.setText("Current password is incorrect!");
            stringBuilder.append("• Current password is incorrect!\n");
            isError = true;
        }

        if (!new1.equals(new2)) {
            infoBox.setText("Passwords don't match!");
            stringBuilder.append("• Passwords don't match!\n");
            isError = true;
        }
        if (new1.equals(current)) {
            infoBox.setText("Please pick a new password!");
            stringBuilder.append("• Please pick a new password!\n");
            isError = true;
        }
        if(check1.length() < 4 || check2.length() < 4){
            infoBox.setText("Please pick a password\nthat is at least 4 characters long!");
            stringBuilder.append("• Please pick a password that is at least 4 characters long!\n");
            isError = true;
        }
        Pattern pattern = Pattern.compile("[a-zA-Z0-9]*");
        Matcher matcher1 = pattern.matcher(check1);
        Matcher matcher2 = pattern.matcher(check2);
        if(!matcher1.matches() || !matcher2.matches()){
            infoBox.setText("The new password has special characters in it!");
            stringBuilder.append("• The new password has special characters in it!\n");
            isError = true;;
        }

        if(!isError) {
            databaseHandler.modifyUser(editedUserClass.getUsername(), new1);
            infoBox.setFill(Color.MEDIUMSEAGREEN);
            if(editedUserClass.equals(receivedUserClass)){
                infoBox.setText("Password updated!\nNext time you can log in\nwith your new password.");
            } else {
                infoBox.setText("Password updated!\nNext time the user can log in\nwith their new password.");
            }
            return;
        }
        System.out.println(stringBuilder);
        infoBox.setText(stringBuilder.toString());

    }

    public void initByHand(){
        if(receivedUserClass == null || editedUserClass == null){
            System.out.println("**Warning! You are editing the general user \"null\"!");
            curPw.setDisable(true);
            npw1.setDisable(true);
            npw2.setDisable(true);
            return;
        }
        System.out.println(receivedUserClass.getUsername() + " is editing user: " + editedUserClass.getUsername());
        if(!receivedUserClass.getIsUser()){
            instructionsBox.setText("• Passwords should be at least 4 characters long!\n" +
                    "• They cannot contain special characters!");
            curPw.setDisable(true);
            detailsButton.setDisable(true);
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
