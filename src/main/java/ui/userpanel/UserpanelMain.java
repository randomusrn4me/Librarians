package ui.userpanel;

import database.DatabaseHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class UserpanelMain extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/ui.userpanel.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Personal Library Manager");
        stage.setScene(scene);
        stage.show();

        new Thread(DatabaseHandler::getInstance).start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
