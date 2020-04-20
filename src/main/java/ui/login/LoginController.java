package ui.login;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.commons.lang3.RandomStringUtils;
import ui.mainframe.MainframeController;
import ui.userpanel.UserpanelController;

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
                windowLoader("/fxml/ui.userpanel.fxml", "Personal Library Manager", username);
            } else if(loginFileAccess.getMapOfUsers().get(username)[1].equals("admin")){
                System.out.println("An admin (\"" + username +"\") has logged in!");
                closeStage();
                windowLoader("/fxml/ui.mainframe.fxml", "General Library Manager", username);
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

    private void windowLoader(String location, String title, String username){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(location));
            Parent parent = loader.load();

            if(loginFileAccess.getMapOfUsers().get(username)[1].equals("user")){
                UserpanelController controller = loader.getController();
                controller.setReceivedUser(username);
            }
            if(loginFileAccess.getMapOfUsers().get(username)[1].equals("admin")){
                MainframeController controller = loader.getController();
                controller.setReceivedUser(username);
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
        loginFileAccess = new LoginFileAccess();
        statusText.setText("Please sign in to start using the software!");
        //System.out.println(RandomStringUtils.randomAlphanumeric(8));
        //loginFileAccess.addUser("admin2", hashing("administrator"), "admin");
        //loginFileAccess.addUser("test", hashing("testing"), "user");
        //loginFileAccess.modifyUser("usr", hashing("usr"), "user");

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
