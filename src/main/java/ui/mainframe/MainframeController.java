package ui.mainframe;

import com.jfoenix.controls.JFXButton;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import utils.*;
import database.DatabaseHandler;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ui.listBooks.ListBooksController;
import ui.listIssued.ListIssuedController;
import ui.listUsers.ListUsersController;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.TreeMap;

public class MainframeController implements Initializable {

    public String receivedUser;

    private User receivedUserClass;

    private User queriedUser;

    private boolean validBook = false, validUser = false;

    public void setReceivedUser(String receivedUser, User receivedUserClass) {
        this.receivedUser = receivedUser;
        this.receivedUserClass = receivedUserClass;
        loggedInUser.setText("Admin: " + receivedUserClass.getUsername());
    }

    @FXML
    private StackPane rootPane;

    @FXML
    private Text usernameOfUser;

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
    private VBox vbox;

    private DatabaseHandler databaseHandler;

    @FXML
    private Menu loggedInUser;

    @FXML
    private JFXButton issueButton;

    @FXML
    private JFXButton renewButton;

    @FXML
    void bulkRenewPressed() {
        StringBuilder act = new StringBuilder();
        act.append("UPDATE ISSUE SET dueDate = DATEADD('week',");

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
            "Are you sure you want to renew all the issued books?", ButtonType.NEXT, ButtonType.CANCEL);
        confirm.setTitle("Confirm Bulk Renew");
        confirm.setHeaderText(null);
        Optional<ButtonType> conf = confirm.showAndWait();
        if(conf.get() != ButtonType.NEXT) return;

        ButtonType one = new ButtonType("1 week", ButtonBar.ButtonData.OK_DONE);
        ButtonType two = new ButtonType("2 weeks", ButtonBar.ButtonData.OK_DONE);
        ButtonType four = new ButtonType("4 weeks", ButtonBar.ButtonData.OK_DONE);


        Alert amountAlert = new Alert(Alert.AlertType.CONFIRMATION,
                "How long would you like to renew them for?", one, two, four, ButtonType.CANCEL);
        amountAlert.setTitle("Bulk Renew Length");
        amountAlert.setHeaderText(null);

        Optional<ButtonType> response = amountAlert.showAndWait();
        if(response.get() == one){
            act.append("1, dueDate)");
        }
        else if(response.get() == two){
            act.append("2, dueDate)");
        }
        else if(response.get() == four){
            act.append("4, dueDate)");
        }
        else return;

        if(databaseHandler.execAction(act.toString())){
            Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
            alert2.setTitle("Success");
            alert2.setHeaderText(null);
            alert2.setContentText("All issued books have been renewed for an additional "
                    + response.get().getText() + " successfully.");
            alert2.showAndWait();
        }else {
            Alert alert3 = new Alert(Alert.AlertType.ERROR);
            alert3.setTitle("Failed");
            alert3.setHeaderText(null);
            alert3.setContentText("Could not renew the issued books.");
            alert3.showAndWait();
        }
    }

    @FXML
    void loadBookInfo() {
        String id = bookIDInput.getText().toUpperCase();
        if(id.isEmpty()){
            entID();
            validBook = false;
            validityCheck();
            return;
        }
        //String id = bookIDInput.getText().toLowerCase();
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
                bookStatus.setStyle("-fx-font-weight:bold");
                if(bStatus) bookStatus.setFill(Color.GREEN);
                if(!bStatus) bookStatus.setFill(Color.RED);
                flag = true;
                validBook = true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(!flag){
            validBook = false;
            bookTitle.setText("");
            bookAuthor.setText("The requested Book ID is not registered!");
            bookStatus.setText("");
        }
        validityCheck();
    }

    @FXML
    void loadUserInfo() {
        String username = usernameInput.getText().toLowerCase();
        //String username = usernameInput.getText();
        if(username.isEmpty()){
            entName();
            validUser = false;
            validityCheck();
            return;
        }
        String qu = "SELECT * FROM USER WHERE username = '" + username + "'";
        ResultSet rs = databaseHandler.execQuery(qu);
        boolean flag = false;
        while(true){
            try {
                assert rs != null;
                if (!rs.next()) break;
                String uName = rs.getString("username");
                String uEmail = rs.getString("email");
                String fullname = rs.getString("fullname");
                String address = rs.getString("address");
                String phone = rs.getString("phone");
                Boolean isUser = rs.getBoolean("isUser");
                Boolean firstLog = rs.getBoolean("firstLog");
                nameOfUser.setText("Name: " + fullname);
                emailOfUser.setText("E-mail: " + uEmail);
                usernameOfUser.setText("Username: "+ uName);
                queriedUser = new User(uName.toLowerCase(),fullname, uEmail, address, phone, isUser, firstLog);
                flag = true;
                validUser = true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(!flag){
            validUser = false;
            nameOfUser.setText("");
            usernameOfUser.setText("The requested Username is not registered!");
            emailOfUser.setText("");
        }
        validityCheck();

    }

    @FXML
    void loadIssueBook() {
        //String username = usernameInput.getText();
        loadBookInfo();
        if(!validBook){
            return;
        }
        String username = usernameInput.getText().toLowerCase();
        String bookID = bookIDInput.getText().toUpperCase();
        toolTip.setText("");
        toolTip.setStyle("-fx-font-weight:bold");



        if(username.isEmpty() || bookID.isEmpty()){
            toolTip.setFill(Color.RED);
            toolTip.setText("Please enter a Book ID and Username to issue a book.");
            if(username.isEmpty()){
                entName();
                validUser = false;
                validityCheck();
            }
            if(bookID.isEmpty()){
                entID();
                validBook = false;
                validityCheck();
            }
            return;
        }
        String qu = "SELECT * FROM ISSUE WHERE username = '" + username + "'";
        ResultSet rs = databaseHandler.execQuery(qu);
        TreeMap<String, LocalDate> overdueBooks = new TreeMap<>();
        LocalDate actual = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        formatter.format(actual);
        //boolean found = false;
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
            msg.append("Cannot issue more books to that user.\nThe following book(s) must be returned (or renewed) first:\n");
            for(Map.Entry<String, LocalDate> entry : overdueBooks.entrySet()){
                msg.append("\n-Title: ").append(entry.getKey()).append("\n-Exp. Due Date: ").append(entry.getValue()).append("\n");
            }
            Alert err = new Alert(Alert.AlertType.ERROR);
            err.setTitle("Overdue error");
            err.setHeaderText(null);
            err.setContentText(msg.toString());
            err.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Book Issue");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to issue that book to \"" + usernameInput.getText() + "\"?");

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
                    validBook = false;
                    validityCheck();
            }
        }

    }

    @FXML
    void handleAboutPushed() {
        windowLoader("/fxml/about.fxml", "About");
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
        loadUserInfo();
        String username = usernameInput.getText().toLowerCase();
        //String username = usernameInput.getText();
        toolTip.setText("");
        toolTip.setStyle("-fx-font-weight:bold");

        if(username.isEmpty()){
            toolTip.setFill(Color.RED);
            toolTip.setText("Please enter a Username to Renew/Return a book.");
            entName();
            validUser = false;
            validityCheck();
            //nameOfUser.setText("");
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

        windowLoader("/fxml/ui.list_issued.fxml", "Books issued to: " + username);
    }

    @FXML
    public void logoutButtonPushed() {
        ((Stage) rootPane.getScene().getWindow()).close();
        windowLoader("/fxml/ui.login.fxml", "Login");
    }

    void windowLoader(String location, String title){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(location));
            Parent parent = loader.load();
            boolean resizeable = false;

            if(location.contains("list_issued")){
                ListIssuedController controller = loader.getController();
                controller.setReceivedUser(queriedUser);
                System.out.println(queriedUser.getUsername());
                resizeable = true;
            }

            if(location.contains("list_books")){
                System.out.println("contains");
                ListBooksController controller = loader.getController();
                controller.setReceivedUser(receivedUserClass);
                resizeable = true;
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
                resizeable = true;
            }
            if(location.contains("search")){
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

    private void entName(){
        nameOfUser.setText("");
        usernameOfUser.setText("Enter Username then press ENTER!");
        emailOfUser.setText("");
    }

    private void entID(){
        bookTitle.setText("");
        bookAuthor.setText("Enter Book ID then press ENTER!");
        bookStatus.setText("");
    }

    private void validityCheck(){
        if(validBook && validUser) issueButton.setDisable(false);
        else issueButton.setDisable(true);

        if(validUser) renewButton.setDisable(false);
        else renewButton.setDisable(true);

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        databaseHandler = DatabaseHandler.getInstance();

        vbox.setStyle("-fx-background-image: url('/fxml/books.jpg');" +
                "-fx-background-repeat: no-repeat;" +
                "-fx-background-size: 600 450;" +
                "-fx-background-position: center center;");

        entID();
        entName();
        validityCheck();

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
}