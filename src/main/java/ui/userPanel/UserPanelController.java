package ui.userPanel;

import javafx.scene.image.Image;
import javafx.scene.layout.*;
import utils.*;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ui.editUserPassword.EditUserPasswordController;
import ui.listBooks.ListBooksController;
import ui.listIssued.ListIssuedController;
import ui.listUsers.ListUsersController;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UserPanelController implements Initializable {

    @FXML
    public void logoutButtonPushed() {
        ((Stage) rootPane.getScene().getWindow()).close();
        windowLoader("/fxml/ui.login.fxml", "Login");
    }

    private User receivedUserClass;

    public void setReceivedUser(User receivedUserClass) {
        this.receivedUserClass = receivedUserClass;
        loggedInUser.setText("User: " + receivedUserClass.getUsername());
    }

    @FXML
    private Menu loggedInUser;

    @FXML
    private VBox welcomeBox;

    @FXML
    private StackPane rootPane;


    @FXML
    void loadListBooksWindow() {
        windowLoader("/fxml/ui.list_books.fxml", "All Books");
    }

    @FXML
    void loadSearchWindow() {
        windowLoader("/fxml/ui.search.fxml", "Search Books");
    }

    @FXML
    void loadIssueList() {
        windowLoader("/fxml/ui.list_issued.fxml", "My Issued Books");
        System.out.println(receivedUserClass.getUsername() + "opened Issue List!");
    }

    @FXML
    void loadUserGuide() {
        windowLoader("/fxml/ui.userguide.fxml", "User Guide");
    }

    void windowLoader(String location, String title){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(location));
            Parent parent = loader.load();
            boolean resizeable = false;

            if(location.contains("list_issued")){
                ListIssuedController controller = loader.getController();
                controller.setReceivedUser(receivedUserClass);
                controller.initByHand();
                resizeable = true;
            } else if(location.contains("list_books")){
                System.out.println("contains");
                ListBooksController controller = loader.getController();
                controller.setReceivedUser(receivedUserClass);
                controller.initByHand();
                resizeable = true;
            } else if(location.contains("search")){
                resizeable = true;
            }
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle(title);
            stage.setScene(new Scene(parent));
            if(!resizeable){
                stage.setResizable(false);
            }
            stage.getIcons().add(new Image("icons/library.png"));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        welcomeBox.setStyle("-fx-background-image: url('/fxml/books.jpg');" +
                "-fx-background-repeat: no-repeat;" +
                "-fx-background-size: 600 450;" +
                "-fx-background-position: center center;");

        rootPane.setFocusTraversable(true);
        rootPane.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ESCAPE)  {
                    logoutButtonPushed();
                }
            }
        });
    }

    @FXML
    void editUserInfoPushed() {
        userDetailsButtonPushed();
    }

    @FXML
    void handleAboutPushed() {
        windowLoader("/fxml/about.fxml", "About");
    }


    public void userDetailsButtonPushed() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ui.edit_user_password.fxml"));
            Parent parent = loader.load();
            EditUserPasswordController controller = loader.getController();
            controller.setReceivedUser(receivedUserClass, receivedUserClass);

            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Editing User Information");
            stage.setScene(new Scene(parent));
            stage.setResizable(false);
            stage.getIcons().add(new Image("icons/library.png"));
            stage.show();

            controller.initByHand();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
