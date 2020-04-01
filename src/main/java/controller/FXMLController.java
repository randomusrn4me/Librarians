package controller;

import com.jfoenix.controls.JFXTextField;
import javafx.scene.control.Label;
import model.Book;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.util.Random;
import com.jfoenix.controls.JFXButton;

public class FXMLController implements Initializable {



    @FXML
    private JFXButton addBook;

    @FXML
    private JFXButton cancel;

    @FXML
    private JFXTextField titleBar;

    @FXML
    private JFXTextField authorBar;

    @FXML
    private JFXTextField yearBar;

    @FXML
    private JFXTextField publisherBar;

    @FXML
    void handleAddBookButtonPushed() throws IOException {
        System.out.println("AddBook button pushed!!");

        Book konyv = new Book(titleBar.getText(), authorBar.getText(), Integer.parseInt(yearBar.getText()), publisherBar.getText());

        try (FileOutputStream fos = new FileOutputStream("abook.ser");
             ObjectOutputStream oos = new ObjectOutputStream(fos);) {
            oos.writeObject(konyv);
        }
    }

    @FXML
    void handleCancelButtonPushed() throws IOException, ClassNotFoundException {
        System.out.println("Cancel button pushed!!");

        try (FileInputStream fis = new FileInputStream("abook.ser");
             ObjectInputStream ois = new ObjectInputStream(fis);) {

            Book s = (Book) ois.readObject();
            titleBar.setText(s.getTitle());
            authorBar.setText(s.getAuthor());
            yearBar.setText(s.getYear() + "");
            publisherBar.setText(s.getPublisher());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}

/*
void handleLoadButtonPushed() {
        //nameLabel.setText(model.getStudent().getName());
        nameLabel.textProperty().bind(model.getStudent().nameProperty());

        creditsLabel.setText("" + model.getStudent().getCredits());
        bdLabel.setText(model.getStudent().getBirthDate().toString());
    }

    @FXML
    void handleButtonPushed() {
        System.out.println("Gomb!!!");
        if (colorLabel.getText().equals("Fekete")) {
            colorLabel.setText("Fehér");
        } else {
            colorLabel.setText("Fekete");
        }
    }

    @FXML
    void handleChangeButtonPushed() {
        model.getStudent().setName(newNameTextField.getText());
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Button pushed");
        alert.setHeaderText("You have pushed the Change Name button");
        alert.setContentText("This shows that you have pushed a button...");
        alert.showAndWait();
    }

    @FXML
    void handleSaveToFileButtonPushed() throws IOException {
        try (FileOutputStream fos = new FileOutputStream("students.ser");
                ObjectOutputStream oos = new ObjectOutputStream(fos);) {
            oos.writeObject(model.getStudent());
        }
    }

    @FXML
    void handleLoadFromFileButtonPushed() throws IOException, ClassNotFoundException {
        try (FileInputStream fis = new FileInputStream("students.ser");
                ObjectInputStream ois = new ObjectInputStream(fis);) {
            Student s = (Student) ois.readObject();
            model.getStudent().setName(s.getName());
            model.getStudent().setCredits(s.getCredits());
            model.getStudent().setBirthDate(s.getBirthDate());
        }
 */