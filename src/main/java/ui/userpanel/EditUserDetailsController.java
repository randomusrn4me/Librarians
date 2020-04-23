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
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import ui.listissued.ListIssuedController;
import ui.listissued.ListIssuedController;
import ui.login.LoginController;
import ui.login.LoginFileAccess;
import ui.mainframe.MainframeController;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditUserDetailsController implements Initializable {

    private String receivedUser;

    public void setReceivedUser(String receivedUser) {
        this.receivedUser = receivedUser;
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
    void pwSaveButtonPushed() {
        DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
        if(receivedUser == null){
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

        String qu = "SELECT pass FROM USER WHERE username = '" + receivedUser + "'";
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

        if(!current.equals(passDb)){
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
            infoBox.setText("Your password has special characters in it!");
            stringBuilder.append("• Your password has special characters in it!\n");
            isError = true;;
        }

        if(!isError) {
            databaseHandler.modifyUser(receivedUser, new1);
            infoBox.setFill(Color.MEDIUMSEAGREEN);
            infoBox.setText("Password updated!\nNext time you can log in\nwith your new password.");
            return;
        }
        System.out.println(stringBuilder);
        infoBox.setText(stringBuilder.toString());

    }

    public void initializeByHand(){
        System.out.println("Editing user: " + receivedUser);
        if(receivedUser == null) System.out.println("**Warning! You are editing the general user \"null\"!");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
