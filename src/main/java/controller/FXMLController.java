package controller;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
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

public class FXMLController implements Initializable {

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
    private JFXButton cancel;


    @FXML
    void handleAddBookButtonPushed() {
        String bookTitle = title.getText();
        String bookAuthor = author.getText();
        String bookYear = year.getText();
        String bookPublisher = publisher.getText();

        if(bookTitle.isEmpty() || bookAuthor.isEmpty() || bookYear.isEmpty() || bookPublisher.isEmpty()){
            Alert emptyAlert = new Alert(Alert.AlertType.ERROR);
            emptyAlert.setHeaderText(null);
            emptyAlert.setContentText("Please fill out all fields.");
            emptyAlert.showAndWait();
            //return;
        }
        else if(!Character.isDigit(bookYear.charAt(0))){
            Alert numberAlert = new Alert(Alert.AlertType.ERROR);
            numberAlert.setHeaderText("Incorrect Year");
            numberAlert.setContentText("Please enter a valid year.");
            numberAlert.showAndWait();
        }
        else if(Integer.parseInt(bookYear) > Calendar.getInstance().get(Calendar.YEAR)){
            Alert numberAlert = new Alert(Alert.AlertType.ERROR);
            numberAlert.setHeaderText("Incorrect Year");
            numberAlert.setContentText("Please enter a valid year.");
            numberAlert.showAndWait();
        }
        else{
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setHeaderText(null);
            successAlert.setContentText("The book has been added successfully.");
            successAlert.showAndWait();
        }

    }

    @FXML
    void handleCancelButtonPushed() {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
