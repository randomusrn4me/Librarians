package ui.userPanel;

import database.DatabaseHandler;
import javafx.scene.control.Alert;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;

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

    public void alertOverdue(){
        DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
        String qu = "SELECT * FROM ISSUE WHERE username = '" + receivedUserClass.getUsername() + "'";
        ResultSet rs = databaseHandler.execQuery(qu);
        TreeMap<String, LocalDate> overdueBooks = new TreeMap<>();
        LocalDate actual = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        formatter.format(actual);
        while(true){
            try {
                assert rs != null;
                if (!rs.next()) break;
                String bid = rs.getString("bookID");
                LocalDate dueDate = rs.getDate("dueDate").toLocalDate();
                if(dueDate.compareTo(actual) < 0){
                    qu = "SELECT * FROM BOOK WHERE id = '" + bid + "'";
                    ResultSet rsBook = databaseHandler.execQuery(qu);
                    assert rsBook != null;
                    if(rsBook.next()){
                        String title = rsBook.getString("title");
                        overdueBooks.put(title, dueDate);
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        if(!overdueBooks.isEmpty()){
            StringBuilder msg = new StringBuilder();
            msg.append("Hi '").append(receivedUserClass.getUsername()).append("',\nPlease return (or renew) the following overdue book(s):\n");
            for(Map.Entry<String, LocalDate> entry : overdueBooks.entrySet()){
                msg.append("\n-Title: ").append(entry.getKey()).append("\n-Exp. Due Date: ").append(entry.getValue()).append("\n");
            }
            msg.append("\nAll overdue books must be returned before borrowing more books.\nYou may be subject to overdue charges.");
            Alert err = new Alert(Alert.AlertType.WARNING);
            err.setTitle("Overdue books");
            err.setHeaderText(null);
            err.setContentText(msg.toString());
            err.showAndWait();
        }
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
            else if(location.contains("edit_user")){
                EditUserPasswordController controller = loader.getController();
                controller.setReceivedUser(receivedUserClass, receivedUserClass);
                controller.initByHand();
            }
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle(title);
            stage.initOwner(rootPane.getScene().getWindow());
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
        rootPane.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE)  {
                logoutButtonPushed();
            }
        });
    }

    @FXML
    void editUserInfoPushed() {
        windowLoader("/fxml/ui.edit_user_password.fxml", "Editing User Information");
    }

    @FXML
    void handleAboutPushed() {
        windowLoader("/fxml/about.fxml", "About");
    }


}
