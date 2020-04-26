package ui.mainframe;


import database.DatabaseHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ui.listbooks.ListBooksController;
import ui.listissued.ListIssuedAdminController;
import ui.listusers.ListUsersController;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainframeController implements Initializable {

    public String receivedUser;

    private ListUsersController.User receivedUserClass;

    private ListUsersController.User queriedUser;

    public void setReceivedUser(String receivedUser, ListUsersController.User receivedUserClass) {
        this.receivedUser = receivedUser;
        this.receivedUserClass = receivedUserClass;
        userInfoBox.setText("Current admin: " + receivedUserClass.getUsername());
    }

    @FXML
    private Text toolTip;

    @FXML
    private TextField bookIDInput;

    @FXML
    private Text bookTitle;

    @FXML
    private Text bookAuthor;

    @FXML
    private Text bookStatus;

    @FXML
    private TextField usernameInput;

    @FXML
    private Text nameOfUser;

    @FXML
    private Text emailOfUser;

    @FXML
    private Text userInfoBox;

    private DatabaseHandler databaseHandler;

    @FXML
    void loadBookInfo() {
        String id = bookIDInput.getText();
        String qu = "SELECT * FROM BOOK WHERE id = '" + id + "'";
        ResultSet rs = databaseHandler.execQuery(qu);
        boolean flag = false;
        while(true){
            try {
                assert rs != null;
                if (!rs.next()) break;
                String bName = rs.getString("title");
                String bAuthor = rs.getString("author");
                boolean bStatus = rs.getBoolean("isAvail");
                bookTitle.setText("Title: " + bName);
                bookAuthor.setText("Author: " + bAuthor);
                String printStatus = "Status: " + (bStatus ? "Available" : "Unavailable");
                bookStatus.setText(printStatus);
                flag = true;

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(!flag){
            bookTitle.setText("The requested Book ID is not registered.");
            bookAuthor.setText("");
            bookStatus.setText("");
        }

    }

    @FXML
    void loadUserInfo() {
        String username = usernameInput.getText();
        String qu = "SELECT * FROM USER WHERE username = '" + username + "'";
        ResultSet rs = databaseHandler.execQuery(qu);
        boolean flag = false;
        while(true){
            try {
                assert rs != null;
                if (!rs.next()) break;
                String uName = rs.getString("fullname");
                String uEmail = rs.getString("email");
                String fullname = rs.getString("fullname");
                String address = rs.getString("address");
                String phone = rs.getString("phone");
                Boolean isUser = rs.getBoolean("isUser");
                Boolean firstLog = rs.getBoolean("firstLog");
                nameOfUser.setText("Name: " + uName);
                emailOfUser.setText("E-mail: " + uEmail);
                queriedUser = new ListUsersController.User(uName,fullname, uEmail, address, phone, isUser, firstLog);
                flag = true;

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(!flag){
            nameOfUser.setText("The requested Username is not registered.");
            emailOfUser.setText("");
        }

    }

    @FXML
    void loadIssueBook() {
        String username = usernameInput.getText();
        String bookID = bookIDInput.getText();
        toolTip.setText("");
        toolTip.setStyle("-fx-font-weight:bold");

        if(username.isEmpty() || bookID.isEmpty()){
            toolTip.setFill(Color.RED);
            toolTip.setText("Please enter a Book ID and Username to issue a book.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Book Issue");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to issue that book to " + usernameInput.getText() + "?");

        Optional<ButtonType> response = alert.showAndWait();
        if(response.get() == ButtonType.OK){
            String add = "INSERT INTO ISSUE (username, bookID) VALUES ("
                    + "'" + username + "',"
                    + "'" + bookID + "')";

            String update = "UPDATE BOOK SET isAvail = false WHERE id = '" + bookID + "'";

            String setDueDate = "UPDATE ISSUE SET dueDate = DATEADD('week', 3, dueDate) WHERE bookID = '" + bookID + "'";


            if(databaseHandler.execAction(add) && databaseHandler.execAction(update) && databaseHandler.execAction(setDueDate)){
                    Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                    alert2.setTitle("Success");
                    alert2.setHeaderText(null);
                    alert2.setContentText("Book issued successfully.");
                    alert2.showAndWait();
            } else{
                    Alert alert3 = new Alert(Alert.AlertType.ERROR);
                    alert3.setTitle("Failed");
                    alert3.setHeaderText(null);
                    alert3.setContentText("Could not issue book.");
                    alert3.showAndWait();
            }
        }

    }

    @FXML
    void loadAddBookWindow() {
        windowLoader("/fxml/ui.add_book.fxml", "Add New Book");
    }

    @FXML
    void loadAddUserWindow() {
        windowLoader("/fxml/ui.add_user.fxml", "Add New User");
    }

    @FXML
    void loadListBooksWindow() {
        windowLoader("/fxml/ui.list_books.fxml", "List Of Books");
    }

    @FXML
    void loadListUsersWindow() {
        windowLoader("/fxml/ui.list_users.fxml", "List Of Users");
    }

    @FXML
    void loadSearchWindow() {
        windowLoader("/fxml/ui.search.fxml", "Search Books");
    }

    @FXML
    void loadUserIssueList() throws SQLException {
        String username = usernameInput.getText();
        toolTip.setText("");
        toolTip.setStyle("-fx-font-weight:bold");

        if(username.isEmpty()){
            toolTip.setFill(Color.RED);
            toolTip.setText("Please enter a Username to Renew/Return a book.");
            return;
        }
        String qu = "SELECT * FROM USER WHERE username = '" + username + "'";
        ResultSet rs = databaseHandler.execQuery(qu);
        assert rs != null;
        if(!rs.next()){
            toolTip.setFill(Color.RED);
            toolTip.setText("Please enter a valid Username.");
            return;
        }

        windowLoader("/fxml/ui.list_issued_admin.fxml", "Books issued to: " + username);
    }

    @FXML
    public void logoutButtonPushed(ActionEvent event) {
        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
        windowLoader("/fxml/ui.login.fxml", "Login");
    }

    void windowLoader(String location, String title){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(location));
            Parent parent = loader.load();

            if(location.contains("issue")){
                ListIssuedAdminController controller = loader.getController();
                controller.setReceivedUser(queriedUser);
            }

            if(location.contains("list_books")){
                System.out.println("contains");
                ListBooksController controller = loader.getController();
                controller.setReceivedUser(receivedUserClass);
            }

            if(location.contains("list_users")){
                if(receivedUserClass == null){
                    Alert emptyAlert = new Alert(Alert.AlertType.ERROR);
                    emptyAlert.setHeaderText("Current admin is /null/");
                    emptyAlert.setContentText("You must log in to view and edit the users!");
                    emptyAlert.showAndWait();
                    return;
                }
                System.out.println("contains users");
                ListUsersController controller = loader.getController();
                controller.setReceivedUser(receivedUserClass);
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
}