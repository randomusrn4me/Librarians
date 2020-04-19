package ui.mainframe;


import com.jfoenix.controls.JFXTextField;
import database.DatabaseHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainframeController implements Initializable {

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
    private JFXTextField bookIDToReturn;

    @FXML
    private ListView<String> issueDataList;


    DatabaseHandler databaseHandler;

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
                nameOfUser.setText("Name: " + uName);
                emailOfUser.setText("E-mail: " + uEmail);

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

        if(username.isEmpty() || bookID.isEmpty()){
            Alert empty = new Alert(Alert.AlertType.ERROR);
            empty.setTitle("Input error");
            empty.setHeaderText(null);
            empty.setContentText("Please enter a Book ID and Username to issue a book.");
            empty.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Book Issue");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to issue that book to " + usernameInput.getText() + "?");
        //alert.showAndWait();

        Optional<ButtonType> response = alert.showAndWait();
        if(response.get() == ButtonType.OK){
            String add = "INSERT INTO ISSUE (username, bookID) VALUES ("
                    + "'" + username + "',"
                    + "'" + bookID + "')";

            String update = "UPDATE BOOK SET isAvail = false WHERE id = '" + bookID + "'";


            if(databaseHandler.execAction(add) && databaseHandler.execAction(update)){
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
    void loadBookInfoForReturn() {
        ObservableList<String> list = FXCollections.observableArrayList();

        String bookID = bookIDToReturn.getText();
        String qu = "SELECT * FROM ISSUE WHERE bookID = '" + bookID + "'";
        ResultSet rs = databaseHandler.execQuery(qu);
        while(true){
            try {
                assert rs != null;
                if (!rs.next()) break;
                String tUsername = rs.getString("username");
                Timestamp tIssueTime = rs.getTimestamp("issueDate");
                int tRenewCount = rs.getInt("renewCount");

                list.add("Issue Date and Time: " + tIssueTime.toString());
                list.add("Renew Count: " + tRenewCount);
                list.add("Book Information:- ");

                qu = "SELECT * FROM BOOK WHERE id = '" + bookID + "'";
                ResultSet rsBook = databaseHandler.execQuery(qu);
                assert rsBook != null;
                while(rsBook.next()){
                    list.add("\tTitle: " + rsBook.getString("title"));
                    list.add("\tAuthor: " + rsBook.getString("author"));
                    list.add("\tPublisher: " + rsBook.getString("publisher"));
                    list.add("\tYear: " + rsBook.getString("year"));
                    list.add("\tID: " + rsBook.getString("id"));
                }

                list.add("User Information:- ");
                qu = "SELECT * FROM USER WHERE username = '" + tUsername + "'";
                ResultSet rsUser = databaseHandler.execQuery(qu);
                assert rsUser != null;
                while(rsUser.next()){
                    list.add("\tUsername: " + rsUser.getString("username"));
                    list.add("\tName: " + rsUser.getString("fullname"));
                    list.add("\tEmail address: " + rsUser.getString("email"));
                    list.add("\tPhone number: " + rsUser.getString("phone"));
                }


            } catch (SQLException e) {
                e.printStackTrace();
            }

            issueDataList.getItems().setAll(list);

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
    public void logoutButtonPushed(ActionEvent event) {
        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
        windowLoader("/fxml/ui.login.fxml", "Login");
    }

    void windowLoader(String location, String title){
        try {
            Parent parent = FXMLLoader.load(getClass().getResource(location));
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