package ui.login;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import database.DatabaseHandler;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ui.listbooks.ListBooksController;
import ui.mainframe.MainframeLauncher;
import ui.mainframe.MainframeMain;

import javax.swing.*;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    private LoginFileAccess loginFileAccess;

    @FXML
    private TextField usernameBox;

    @FXML
    private TextField passwordBox;

    @FXML
    private Button loginButton;

    @FXML
    private Text statusText;

    @FXML
    void loginButtonPushed() {
        String username = usernameBox.getText().toLowerCase();
        String password = hashing(passwordBox.getText());
        statusText.setStyle("-fx-font-weight:bold");

        if(username.isEmpty() || passwordBox.getText().isEmpty()){
            statusText.setText("Please type in your login details!");
            statusText.setFill(Color.BLACK);
            return;
        }
        if(loginFileAccess.getMapOfUsers().containsKey(username) && loginFileAccess.getMapOfUsers().get(username)[0].equals(password)){
            statusText.setText("You are a valid user");
            statusText.setFill(Color.MEDIUMSEAGREEN);
            if(loginFileAccess.getMapOfUsers().get(username)[1].equals("user")){
                System.out.println("A user (\"" + username +"\") has logged in!");
                closeStage();
                windowLoader("/fxml/ui.userpanel.fxml", "Personal Library Manager");
            } else if(loginFileAccess.getMapOfUsers().get(username)[1].equals("admin")){
                System.out.println("An admin (\"" + username +"\") has logged in!");
                closeStage();
                windowLoader("/fxml/ui.mainframe.fxml", "General Library Manager");
            }
        } else {
            statusText.setText("Invalid user");
            statusText.setFill(Color.RED);
            System.out.println("An invalid (\"" + username + "\") user has attempted login!");
        }
    }

    public void cancelButtonPushed() {
        closeStage();
    }

    public static String hashing(String pw){
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] messageDigest = md.digest(pw.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) hashtext = "0" + hashtext;
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.out.println("Hashing unsuccessful");
            return pw;
        }
    }

    private void closeStage(){
        ((Stage) usernameBox.getScene().getWindow()).close();
    }

    private void windowLoader(String location, String title){
        try {
            Parent parent = FXMLLoader.load(getClass().getResource(location));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle(title);
            stage.setScene(new Scene(parent));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        loginFileAccess = new LoginFileAccess();
        statusText.setText("Please sign in to start using the software!");
        loginFileAccess.addUser("admin2", hashing("administrator"), "admin");
        loginFileAccess.addUser("test", hashing("testing"), "user");

        passwordBox.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER)  {
                    loginButtonPushed();
                }
            }
        });

        usernameBox.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER)  {
                    loginButtonPushed();
                }
            }
        });
    }



}
