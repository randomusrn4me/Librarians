package ui.login;

import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import database.DatabaseHandler;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import ui.listbooks.ListBooksController;

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
        String username = usernameBox.getText();
        String password = hashing(passwordBox.getText());

        if(!loginFileAccess.mapOfUsers.containsKey(username)
           ||
           !loginFileAccess.mapOfUsers.get(username).equals(password)){
                statusText.setText("Invalid user");
                statusText.setFill(Color.RED);
        } else {
            statusText.setText("You are a valid user");
            statusText.setFill(Color.GREEN);
        }

    }

    public String hashing(String pw){
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] messageDigest = md.digest(pw.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.out.println("Hashing unsuccessful");
            return pw;
        }
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        loginFileAccess = new LoginFileAccess();
        statusText.setText("Please sign in to start using the software");
    }
}
