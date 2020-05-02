package ui.login;

import utils.*;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import database.DatabaseHandler;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ui.mainframe.MainframeController;
import ui.userPanel.UserPanelController;

public class LoginController implements Initializable {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private TextField usernameBox;

    @FXML
    private TextField passwordBox;

    @FXML
    private Button loginButton;

    @FXML
    private Text statusText;

    private User userToBeSent;

    @FXML
    void loginButtonPushed() {
        DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
        String username = usernameBox.getText().toLowerCase();
        String password = hashing(passwordBox.getText());
        statusText.setStyle("-fx-font-weight:bold");

        if(username.isEmpty() || passwordBox.getText().isEmpty()){
            statusText.setText("Please type in your login details!");
            statusText.setFill(Color.BLACK);
            return;
        }

        String qu = "SELECT * FROM USER WHERE username = '" + username + "'";
        String pw = null;
        boolean firstLog = false;
        boolean isUser = true;
        ResultSet rs = databaseHandler.execQuery(qu);
        if(rs == null){
            statusText.setText("Invalid username");
            statusText.setFill(Color.RED);
            System.out.println("An invalid (\"" + username + "\") user has attempted login!");
            return;
        }
        else{
            while (true) {
                try {
                    if (!rs.next()) break;
                    String fullname = rs.getString("fullname");
                    String email = rs.getString("email");
                    String address = rs.getString("address");
                    String phone = rs.getString("phone");
                    isUser = rs.getBoolean("isUser");
                    firstLog = rs.getBoolean("firstLog");
                    pw = rs.getString("pass");
                    userToBeSent = new User(username, fullname, email, address, phone, isUser, firstLog);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        assert pw != null;
        if(isUser && pw.equals(password)){
            closeStage();
            windowLoader("/fxml/ui.userpanel.fxml", "Personal Library Manager", username, true);
            if(firstLog){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("First login password warning");
                alert.setHeaderText(null);
                alert.setContentText("We highly recommend you to change the generated password you received from us.");
                alert.showAndWait();
                String act = "UPDATE USER SET firstLog = false WHERE username = '" + username + "'";
                databaseHandler.execAction(act);
            }

        }
        else if(!isUser && pw.equals(password)){
            System.out.println("An admin (\"" + username +"\") has logged in!");
            closeStage();
            windowLoader("/fxml/ui.mainframe.fxml", "General Library Manager", username, false);
            if(firstLog) {
                String act = "UPDATE USER SET firstLog = false WHERE username = '" + username + "'";
                databaseHandler.execAction(act);
            }
        }
        else{
            statusText.setText("Incorrect password.");
            statusText.setFill(Color.RED);
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

    private void windowLoader(String location, String title, String username, boolean isUser){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(location));
            Parent parent = loader.load();

            if(isUser){
                UserPanelController controller = loader.getController();
                controller.setReceivedUser(userToBeSent);
            }
            else{
                MainframeController controller = loader.getController();
                controller.setReceivedUser(username, userToBeSent);
            }

            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle(title);
            stage.setScene(new Scene(parent));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        statusText.setText("Please sign in to start using the software!");

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
