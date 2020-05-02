package utils;

import database.DatabaseHandler;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

public abstract class Controller {
    private User receivedUserClass = null;

    private DatabaseHandler databaseHandler;

    @FXML
    private Pane rootPane;

    public abstract void setReceivedUser(User receivedUserClass);
    public abstract void initByHand();
    public abstract void closeWindow();
}
