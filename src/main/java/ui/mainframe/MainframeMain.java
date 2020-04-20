package ui.mainframe;

import database.DatabaseHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainframeMain extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/ui.mainframe.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("General Library Manager");
        stage.setScene(scene);
        stage.show();;

        new Thread(DatabaseHandler::getInstance).start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
