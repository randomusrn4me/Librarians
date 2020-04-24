package ui.addbook;

import database.DatabaseHandler;
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

    private DatabaseHandler databaseHandler;


    @FXML
    void handleAddBookButtonPushed() {
        String bookTitle = title.getText();
        String bookAuthor = author.getText();
        String bookYear = year.getText();
        String bookPublisher = publisher.getText();
        String bookID = id.getText();

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

    @FXML
    void handleCancelButtonPushed() {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    void clear(){
        title.setText("");
        author.setText("");
        publisher.setText("");
        id.setText("");
        year.setText("");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        databaseHandler = DatabaseHandler.getInstance();

    }



}
