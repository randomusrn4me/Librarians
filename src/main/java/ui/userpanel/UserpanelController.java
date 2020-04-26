package ui.userpanel;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ui.listbooks.ListBooksController;
import ui.listissued.ListIssuedController;
import ui.listusers.ListUsersController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UserpanelController implements Initializable {

    @FXML
    public void logoutButtonPushed(ActionEvent event) {
        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
        windowLoader("/fxml/ui.login.fxml", "Login");
    }

    private ListUsersController.User receivedUserClass;

    public void setReceivedUser(ListUsersController.User receivedUserClass) {
        this.receivedUserClass = receivedUserClass;
        userInfoBox.setText("Current user: " + receivedUserClass.getUsername());
    }

    @FXML
    private Text userInfoBox;

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
