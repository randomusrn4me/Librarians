package ui.listBooks;

import database.DatabaseHandler;
import utils.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ui.addBook.AddBookController;
import ui.listUsers.ListUsersController;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class ListBooksController implements Initializable {

    ObservableList<Book> list = FXCollections.observableArrayList();

    private User receivedUserClass;

    public void setReceivedUser(User receivedUserClass) {
        this.receivedUserClass = receivedUserClass;
        System.out.println("Username of person listing users: " + receivedUserClass.getUsername());
    }

    @FXML
    private AnchorPane rootPane;

    @FXML
    private TableView<Book> tableView;

    @FXML
    private TableColumn<Book, String> titleCol;

    @FXML
    private TableColumn<Book, String> authorCol;

    @FXML
    private TableColumn<Book, String> yearCol;

    @FXML
    private TableColumn<Book, String> publisherCol;

    @FXML
    private TableColumn<Book, String> idCol;

    @FXML
    private TableColumn<Book, Boolean> availabilityCol;

    @FXML
    private MenuItem delMenu;

    @FXML
    private MenuItem editMenu;

    @FXML
    private MenuItem issuedMenu;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initCol();

        try {
            loadData();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        rootPane.setFocusTraversable(true);
        rootPane.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ESCAPE)  {
                    ((Stage) rootPane.getScene().getWindow()).close();
                }
            }
        });
    }

    public void initByHand(){
        if(receivedUserClass.getIsUser()){
            delMenu.setVisible(false);
            editMenu.setVisible(false);
            issuedMenu.setVisible(false);
            System.out.println("it's a user");
        }
    }

    private void alertError(String text){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }

    @FXML
    void handleShowIssued() throws SQLException{
        DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
        Book selectedBook = tableView.getSelectionModel().getSelectedItem();
        if(selectedBook == null){
            return;
        }
        if(selectedBook.isAvailability()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Issued to...");
            alert.setHeaderText(null);
            alert.setContentText("This book (ID: " + selectedBook.getId() + ") is not issued to anyone.");
            alert.showAndWait();
            return;
        }
        String qu = "SELECT username FROM ISSUE WHERE bookID='" + selectedBook.getId() + "'";

        ResultSet rs = databaseHandler.execQuery(qu);
        if(rs == null){
            System.out.println("db error");
            return;
        }
        rs.next();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Issued to...");
        alert.setHeaderText(null);
        alert.setContentText("This book (ID: " + selectedBook.getId() + ") is issued to \"" + rs.getString("username") + "\".");
        alert.showAndWait();
    }

    @FXML
    void handleDeleteBook() throws SQLException {
        DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
        ObservableList<Book> selectedBooks = tableView.getSelectionModel().getSelectedItems();
        if(selectedBooks == null){
            alertError("No book selected.\nPlease select a book to be deleted.");
            return;
        }
        StringBuilder in = new StringBuilder();
        List<String> ids = selectedBooks.stream().map(Book::getId).collect(Collectors.toList());

        for(String s : ids){
            in.append("'").append(s).append("'");
            if(ids.lastIndexOf(s) < ids.size() - 1){
                in.append(", ");
            }
            else{
                in.append(")");
            }
        }

        String qu = "SELECT * FROM ISSUE WHERE bookID IN (" + in.toString();
        ResultSet rs = databaseHandler.execQuery(qu);
        assert rs != null;
        if(rs.next()){
            alertError("Selected book(s) cannot be deleted while issued to a user.");
            return;
        }

        String act = "DELETE FROM BOOK WHERE id IN (" + in.toString();

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Book Delete");
        confirm.setHeaderText(null);
        confirm.setContentText("Are you sure you want to delete the selected book(s)?");

        Optional<ButtonType> response = confirm.showAndWait();
        if(response.get() != ButtonType.OK){
            return;
        }
        if(databaseHandler.execAction(act)){
            list.removeAll(selectedBooks);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Successfully deleted book(s)!");
            alert.showAndWait();
        }
        else{
            alertError("Book(s) could not be deleted!");
        }
    }

    @FXML
    void handleEditBook() {
        DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
        Book selectedForEdit = tableView.getSelectionModel().getSelectedItem();
        if(selectedForEdit == null){
            alertError("No book selected.\nPlease select a book to be edited.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ui.add_book.fxml"));
            Parent parent = loader.load();
            AddBookController controller = loader.getController();
            controller.inflateAddBookUI(selectedForEdit);
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Edit Book Details");
            stage.setScene(new Scene(parent));
            stage.showAndWait();

            String qu = "SELECT * FROM BOOK WHERE id = '" + selectedForEdit.getId() + "'";
            ResultSet rs = databaseHandler.execQuery(qu);
            assert rs != null;
            while(true){
                try {
                    if (!rs.next()) break;
                    String title = rs.getString("title");
                    String author = rs.getString("author");
                    String publisher = rs.getString("publisher");
                    String year = rs.getString("year");

                    list.set(list.indexOf(selectedForEdit),
                            new Book(title, author, publisher, year, selectedForEdit.getId(), selectedForEdit.isAvailability()));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadData() throws SQLException {
        DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
        String qu = "SELECT * FROM BOOK";
        ResultSet rs = databaseHandler.execQuery(qu);
        while(true){
            try {
                assert rs != null;
                if (!rs.next()) break;
                String title = rs.getString("title");
                String author = rs.getString("author");
                String publisher = rs.getString("publisher");
                String year = rs.getString("year");
                //String id = rs.getString("id").toUpperCase();
                String id = rs.getString("id");
                Boolean avail = rs.getBoolean("isAvail");

                list.add(new Book(title, author, publisher, year, id, avail));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            tableView.setItems(list);
        }

        tableView.getSelectionModel().setSelectionMode(
                SelectionMode.MULTIPLE
        );

    }

    private void initCol() {
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        publisherCol.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        yearCol.setCellValueFactory(new PropertyValueFactory<>("year"));
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        availabilityCol.setCellValueFactory(new PropertyValueFactory<>("availability"));
    }
}
