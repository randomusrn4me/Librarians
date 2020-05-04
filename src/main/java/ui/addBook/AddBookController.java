package ui.addBook;

import database.DatabaseHandler;
import utils.*;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;

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

    private void alertError(String text){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }

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
            alertError("Please fill out all fields.");
            return;
        }
        else if(!Character.isDigit(bookYear.charAt(0))){
            alertError("Please enter a valid year.");
            return;
        }
        else if(Integer.parseInt(bookYear) > Calendar.getInstance().get(Calendar.YEAR)){
            alertError("Please enter a valid year.");
            return;
        }
        else if((idChars[0] != 'B') || !correctID || bookID.length() > 5){
            alertError("Please enter a correct Book ID form.\nIt must begin with 'B' and be numbered from 1-9999");
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
            alertError("Failed to add the book to the database.");
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
            alertError("Failed to update the book details.");
        }

    }

    @FXML
    void handleCancelButtonPushed() {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    public void inflateAddBookUI(Book book){
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
