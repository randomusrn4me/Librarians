package utils;

import database.DatabaseHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public abstract class Controller implements Initializable {
    private User receivedUserClass;
    private DatabaseHandler databaseHandler;

    @FXML
    private AnchorPane rootPane;

    public abstract void initByHand();

    public void setReceivedUser(User receivedUserClass){
        this.receivedUserClass = receivedUserClass;
    };

    public void closeWindow(){
        ((Stage) rootPane.getScene().getWindow()).close();
    };
}
