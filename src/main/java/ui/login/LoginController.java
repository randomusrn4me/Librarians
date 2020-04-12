package ui.login;

import java.net.URL;
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
        String password = passwordBox.getText();
        if(loginFileAccess.mapOfUsers.containsKey(username)){

            statusText.setText("User found");
        }
        //statusText.setText("login attemp");
        statusText.setFill(Color.GREEN);
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        loginFileAccess = new LoginFileAccess();
        statusText.setText("Please sign in to start using the software");
    }
}
