package ui.userpanel;

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
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import ui.listissued.ListIssuedController;
import ui.listissued.ListIssuedController;
import ui.mainframe.MainframeController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UserpanelController implements Initializable {

    private DatabaseHandler databaseHandler;

    @FXML
    public void logoutButtonPushed(ActionEvent event) {
        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
        windowLoader("/fxml/ui.login.fxml", "Login");
    }

    public String receivedUser;

    public void setReceivedUser(String receivedUser) {
        this.receivedUser = receivedUser;
    }

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
        System.out.println(receivedUser + "opened Issue List!");
    }

    void windowLoader(String location, String title){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(location));
            Parent parent = loader.load();

            if(location.contains("issue")){
                ListIssuedController controller = loader.getController();
                controller.setReceivedUser(receivedUser);
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
        databaseHandler = DatabaseHandler.getInstance();
    }

    public void userDetailsButtonPushed() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ui.edit_user_details.fxml"));
            Parent parent = loader.load();
            EditUserDetailsController controller = loader.getController();


            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Edit User Details");
            stage.setScene(new Scene(parent));
            stage.show();
            controller.setReceivedUser(receivedUser);
            controller.initializeByHand("");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
