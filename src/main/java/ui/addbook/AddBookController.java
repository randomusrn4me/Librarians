package ui.addbook;

import database.DatabaseHandler;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.net.URL;
import java.util.Calendar;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import com.jfoenix.controls.JFXButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import com.jfoenix.controls.JFXTextField;
import javafx.stage.Stage;
import ui.listbooks.ListBooksController;

public class AddBookController implements Initializable {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private JFXButton addBook;

    @FXML
    private JFXTextField title;

    @FXML
    private JFXTextField author;

    @FXML
    private JFXTextField year;

    @FXML
    private JFXTextField publisher;

    @FXML
    private JFXTextField id;

    @FXML
    private JFXButton cancel;

    private boolean isInEditMode = false;

    private DatabaseHandler databaseHandler;


    @FXML
    void handleAddBookButtonPushed() {
        String bookTitle = title.getText();
        String bookAuthor = author.getText();
        String bookYear = year.getText();
        String bookPublisher = publisher.getText();
        String bookID = id.getText();
        char[] idChars = bookID.toCharArray();
        boolean correctID = idChars.length >= 2;
        for(int i = 1; i < idChars.length; i++){
            if(!Character.isDigit(idChars[i])) {
                correctID = false;
                System.out.println("Attempted character or too short bookID");
                break;
            }
        }

        if(bookTitle.isEmpty() || bookAuthor.isEmpty() || bookYear.isEmpty() || bookPublisher.isEmpty() || bookID.isEmpty()){
            Alert emptyAlert = new Alert(Alert.AlertType.ERROR);
            emptyAlert.setHeaderText(null);
            emptyAlert.setContentText("Please fill out all fields.");
            emptyAlert.showAndWait();
            return;
        }
        else if(!Character.isDigit(bookYear.charAt(0))){
            Alert numberAlert = new Alert(Alert.AlertType.ERROR);
            numberAlert.setHeaderText("Incorrect Year");
            numberAlert.setContentText("Please enter a valid year.");
            numberAlert.showAndWait();
            return;
        }
        else if(Integer.parseInt(bookYear) > Calendar.getInstance().get(Calendar.YEAR)){
            Alert numberAlert = new Alert(Alert.AlertType.ERROR);
            numberAlert.setHeaderText("Incorrect Year");
            numberAlert.setContentText("Please enter a valid year.");
            numberAlert.showAndWait();
            return;
        }
        else if((idChars[0] != 'B') || !correctID || bookID.length() > 5){
            Alert numberAlert = new Alert(Alert.AlertType.ERROR);
            numberAlert.setHeaderText("Incorrect ID");
            numberAlert.setContentText("Please enter a correct Book ID form.\nIt must begin with 'B' and be numbered from 1-9999");
            numberAlert.showAndWait();
            return;
        }

        if(isInEditMode){
            handleEditBook();
            handleCancelButtonPushed();
            return;
        }

        String qu = "INSERT INTO BOOK VALUES ("
                + "'" + bookID + "',"
                + "'" + bookTitle + "',"
                + "'" + bookAuthor + "',"
                + "'" + bookPublisher + "',"
                + "'" + bookYear + "',"
                + "" + true + "" +
                ")";
        System.out.println(qu);
        if(databaseHandler.execAction(qu)){
            Alert emptyAlert = new Alert(Alert.AlertType.INFORMATION);
            emptyAlert.setHeaderText(null);
            emptyAlert.setContentText("Successfully added the book to database.");
            emptyAlert.showAndWait();
            clear();
        }
        else{
            Alert emptyAlert = new Alert(Alert.AlertType.ERROR);
            emptyAlert.setHeaderText(null);
            emptyAlert.setContentText("Failed to add the book to the database.");
            emptyAlert.showAndWait();
        }

    }

    private void handleEditBook() {
        String act = "UPDATE BOOK SET title = '" + title.getText() + "', author = '" + author.getText() + "',"
                + " publisher = '" + publisher.getText() + "', year = '" + year.getText() + "'"
                + " WHERE id = '" + id.getText() + "'";
        if(databaseHandler.execAction(act)){
            Alert emptyAlert = new Alert(Alert.AlertType.INFORMATION);
            emptyAlert.setHeaderText(null);
            emptyAlert.setContentText("Successfully updated the book details.");
            emptyAlert.showAndWait();
        }
        else{
            Alert emptyAlert = new Alert(Alert.AlertType.ERROR);
            emptyAlert.setHeaderText(null);
            emptyAlert.setContentText("Failed to update the book details.");
            emptyAlert.showAndWait();
        }

    }

    @FXML
    void handleCancelButtonPushed() {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    public void inflateAddBookUI(ListBooksController.Book book){
        title.setText(book.getTitle());
        id.setText(book.getId());
        author.setText(book.getAuthor());
        year.setText(book.getYear());
        publisher.setText(book.getPublisher());
        addBook.setText("Save");
        id.setEditable(false);
        isInEditMode = true;
    }

    private void clear(){
        title.setText("");
        author.setText("");
        publisher.setText("");
        id.setText("");
        year.setText("");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        databaseHandler = DatabaseHandler.getInstance();
        rootPane.setFocusTraversable(true);
        rootPane.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ESCAPE)  {
                    handleCancelButtonPushed();
                }
            }
        });

        rootPane.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER)  {
                    handleAddBookButtonPushed();
                }
            }
        });
    }

}
