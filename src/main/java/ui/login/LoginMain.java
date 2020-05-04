package ui.login;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class LoginMain extends Application {
    static Stage stg;

    @Override
    public void start(Stage stage) throws IOException {
        stg = stage;
        Parent root = (Parent)FXMLLoader.load(this.getClass().getResource("/fxml/ui.login.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.getIcons().add(new Image("icons/library.png"));
        stage.show();
    }

    public static void close(){
        stg.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
