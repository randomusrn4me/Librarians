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
        stage.setTitle("Library Manager");
        stage.setScene(scene);
        stage.show();

        DatabaseHandler.getInstance();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
