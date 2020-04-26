package utils;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ui.listbooks.ListBooksController;
import ui.listissued.ListIssuedController;
import ui.listusers.ListUsersController;

public class WindowLoadUtils {
    public static void windowLoader(String location, String title, Class gottenClass, ListUsersController.User workingUser, ListUsersController.User editedUser, Controller controller) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(gottenClass.getResource(location));
            Parent parent = loader.load();

                controller = loader.getController();
                controller.setReceivedUser(workingUser);

                Stage stage = new Stage(StageStyle.DECORATED);
                stage.setTitle(title);
                stage.setScene(new Scene(parent));
                stage.show();
                controller.initByHand();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
