package ui.userPanel;

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
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ui.editUserPassword.EditUserPasswordController;
import ui.listBooks.ListBooksController;
import ui.listIssued.ListIssuedController;
import ui.listUsers.ListUsersController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UserPanelController implements Initializable {

    @FXML
    public void logoutButtonPushed(/*ActionEvent event*/) {
        //((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
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
    private Text userInfoBox;

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

    void windowLoader(String location, String title){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(location));
            Parent parent = loader.load();

            if(location.contains("issued")){
                ListIssuedController controller = loader.getController();
                controller.setReceivedUser(receivedUserClass);
                controller.initByHand();
            } else if(location.contains("list_books")){
                System.out.println("contains");
                ListBooksController controller = loader.getController();
                controller.setReceivedUser(receivedUserClass);
                controller.initByHand();
            }
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle(title);
            stage.setScene(new Scene(parent));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
            stage.setTitle("Change password");
            stage.setScene(new Scene(parent));
            stage.show();

            controller.initByHand();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
