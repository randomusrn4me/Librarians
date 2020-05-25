package ui.listUsers;

import javafx.scene.image.Image;
import ui.editUserPassword.EditUserPasswordController;
import utils.*;
import database.DatabaseHandler;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ui.addUser.AddUserController;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class ListUsersController implements Initializable {

    ObservableList<User> list = FXCollections.observableArrayList();

    private User receivedUserClass;

    public void setReceivedUser(User receivedUserClass) {
        this.receivedUserClass = receivedUserClass;
        System.out.println("Username of admin listing users: " + receivedUserClass.getUsername());
    }

    @FXML
    private TableView<User> tableView;

    @FXML
    private TableColumn<User, String> usernameCol;

    @FXML
    private TableColumn<User, String> nameCol;

    @FXML
    private TableColumn<User, String> emailCol;

    @FXML
    private TableColumn<User, String> addressCol;

    @FXML
    private TableColumn<User, String> phoneCol;

    @FXML
    private TableColumn<User, String> userCol;

    @FXML
    private TableColumn<User, String> loggedCol;

    @FXML
    private MenuItem delMenu;

    @FXML
    private MenuItem pwMenu;

    @FXML
    private MenuItem editMenu;

    @FXML
    private AnchorPane rootPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initCol();
        try {
            loadData();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        rootPane.setFocusTraversable(true);
        rootPane.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ESCAPE)  {
                    ((Stage) rootPane.getScene().getWindow()).close();
                }
            }
        });
    }

    private void alertError(String text){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }

    @FXML
    void handleDeleteUser() throws SQLException {
        DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
        User selectedForDelete = tableView.getSelectionModel().getSelectedItem();
        System.out.println("User deletion attempt: " + selectedForDelete.getUsername());
        if(selectedForDelete == null){
            alertError("No user selected.\nPlease select a user to delete.");
            return;
        }
        String qu = "SELECT * FROM ISSUE WHERE username = '" + selectedForDelete.getUsername() + "'";

        ResultSet rs = databaseHandler.execQuery(qu);
        assert rs != null;
        if(rs.next()){
            alertError("Selected user cannot be deleted, there are still books issued to them." +
                    "Please notify them to return all books before deletion.");
            return;
        }
        String act = "DELETE FROM USER WHERE username = '" + selectedForDelete.getUsername() + "'";
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm User Delete");
        confirm.setHeaderText(null);
        confirm.setContentText("Are you sure you want to delete the selected user?");

        Optional<ButtonType> response = confirm.showAndWait();
        if(response.get() != ButtonType.OK){
            return;
        }
        if(databaseHandler.execAction(act)){
            list.remove(selectedForDelete);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Successfully deleted user!");
            alert.showAndWait();
        }
        else{
            alertError("User could not be deleted!");
        }
    }

    @FXML
    void handleEditPassword() throws IOException {

        User editedUser = tableView.getSelectionModel().getSelectedItem();
        System.out.println("User password edit attempt: " + editedUser.getUsername());
        if(editedUser == null){
            alertError("No user selected.\nPlease select a user to edit.");
            return;
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ui.edit_user_password.fxml"));
        Parent parent = loader.load();
        EditUserPasswordController controller = loader.getController();
        controller.setReceivedUser(receivedUserClass, editedUser);


        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setTitle("Change password");
        stage.setScene(new Scene(parent));
        stage.getIcons().add(new Image("icons/library.png"));
        stage.show();

        controller.initByHand();
    }

    @FXML
    void handleEditUser() {
        DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
        User selectedForEdit = tableView.getSelectionModel().getSelectedItem();
        if(selectedForEdit == null) {
            alertError("No user selected.\nPlease select a user to edit.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ui.add_user.fxml"));
            Parent parent = loader.load();
            AddUserController controller = loader.getController();
            controller.inflateAddUserUI(selectedForEdit);
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Editing User Information");
            stage.initOwner(rootPane.getScene().getWindow());
            stage.setScene(new Scene(parent));
            stage.getIcons().add(new Image("icons/library.png"));
            stage.showAndWait();

            String qu = "SELECT * FROM USER WHERE username = '" + selectedForEdit.getUsername() + "'";
            ResultSet rs = databaseHandler.execQuery(qu);
            assert rs != null;
            while(true){
                try {
                    if (!rs.next()) break;
                    String fullname = rs.getString("fullname");
                    String email = rs.getString("email");
                    String address = rs.getString("address");
                    String phone = rs.getString("phone");

                    list.set(list.indexOf(selectedForEdit),
                            new User(selectedForEdit.getUsername(), fullname, email,
                                    address, phone, selectedForEdit.getIsUser(), selectedForEdit.getFirstLog()));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initCol() {
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("fullname"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        userCol.setCellValueFactory(cellData -> {
            boolean temp = cellData.getValue().getIsUser();
            String asString = temp ? "user" : "admin";
            return new ReadOnlyStringWrapper(asString);
        });
        loggedCol.setCellValueFactory(cellData -> {
            boolean temp = cellData.getValue().getFirstLog();
            String asString = temp ? "no" : "yes";
            return new ReadOnlyStringWrapper(asString);
        });
    }

    private void loadData() throws SQLException {
        DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
        String qu = "SELECT * FROM USER";
        ResultSet rs = databaseHandler.execQuery(qu);
        while(true){
            try {
                assert rs != null;
                if (!rs.next()) break;
                String username = rs.getString("username");
                String fullname = rs.getString("fullname");
                String email = rs.getString("email");
                String address = rs.getString("address");
                String phone = rs.getString("phone");
                Boolean isUser = rs.getBoolean("isUser");
                Boolean firstLog = rs.getBoolean("firstLog");

                list.add(new User(username, fullname, email, address, phone, isUser, firstLog));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            tableView.setItems(list);
        }
    }
}
