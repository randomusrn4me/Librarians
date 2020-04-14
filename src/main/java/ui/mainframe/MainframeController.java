package ui.mainframe;


import database.DatabaseHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
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