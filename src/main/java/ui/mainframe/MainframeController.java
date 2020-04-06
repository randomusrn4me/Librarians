package ui.mainframe;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainframeController implements Initializable {

    @FXML
    void loadAddBookWindow() {
        windowLoader("/fxml/ui.add_book.fxml", "Add New Book");
    }

    @FXML
    void loadAddUserWindow() {
        windowLoader("/fxml/ui.add_user.fxml", "Add New User");
    }

    @FXML
    void loadListBooksWindow() {
        windowLoader("/fxml/ui.list_books.fxml", "List Of Books");
    }

    @FXML
    void loadListUsersWindow() {
        windowLoader("/fxml/ui.list_users.fxml", "List Of Users");
    }

    void windowLoader(String location, String title){
        try {
            Parent parent = FXMLLoader.load(getClass().getResource(location));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle(title);
            stage.setScene(new Scene(parent));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}