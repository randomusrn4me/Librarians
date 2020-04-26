package ui.listusers;

import database.DatabaseHandler;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ui.listbooks.ListBooksController;
import ui.userpanel.EditUserPasswordController;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class ListUsersController implements Initializable {

    ObservableList<User> list = FXCollections.observableArrayList();

    private ListUsersController.User receivedUserClass;

    public void setReceivedUser(ListUsersController.User receivedUserClass) {
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initCol();
        try {
            loadData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleDeleteUser() {
        User editedUser = tableView.getSelectionModel().getSelectedItem();
        System.out.println("User deletion attempt: " + editedUser.getUsername());
        if(editedUser == null) return;
    }

    @FXML
    void handleEditPassword() throws IOException {

        User editedUser = tableView.getSelectionModel().getSelectedItem();
        System.out.println("User password edit attempt: " + editedUser.getUsername());
        if(editedUser == null) return;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ui.edit_user_password.fxml"));
        Parent parent = loader.load();
        EditUserPasswordController controller = loader.getController();
        controller.setReceivedUser(receivedUserClass, editedUser);


        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setTitle("Change password");
        stage.setScene(new Scene(parent));
        stage.show();

        controller.initByHand();
    }

    @FXML
    void handleEditUser() {

        User editedUser = tableView.getSelectionModel().getSelectedItem();
        System.out.println("User details edit attempt: " + editedUser.getUsername());
        if(editedUser == null) return;
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
            } catch (SQLException e) {
                e.printStackTrace();
            }
            String username = rs.getString("username");
            String fullname = rs.getString("fullname");
            String email = rs.getString("email");
            String address = rs.getString("address");
            String phone = rs.getString("phone");
            Boolean isUser = rs.getBoolean("isUser");
            Boolean firstLog = rs.getBoolean("firstLog");

            list.add(new User(username, fullname, email, address, phone, isUser, firstLog));
        }

        tableView.getItems().setAll(list);
    }

    public static class User{
        private final SimpleStringProperty username;
        private final SimpleStringProperty fullname;
        private final SimpleStringProperty email;
        private final SimpleStringProperty address;
        private final SimpleStringProperty phone;
        private final SimpleBooleanProperty isUser;
        private final SimpleBooleanProperty firstLog;

        public User(String username, String fullname, String email, String address, String phone, Boolean isUser, Boolean firstLog){
            this.username = new SimpleStringProperty(username);
            this.fullname = new SimpleStringProperty(fullname);
            this.email = new SimpleStringProperty(email);
            this.address = new SimpleStringProperty(address);
            this.phone = new SimpleStringProperty(phone);
            this.isUser = new SimpleBooleanProperty(isUser);
            this.firstLog = new SimpleBooleanProperty(firstLog);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            User user = (User) o;
            return Objects.equals(username.get(), user.username.get()) &&
                    Objects.equals(fullname.get(), user.fullname.get()) &&
                    Objects.equals(email.get(), user.email.get()) &&
                    Objects.equals(address.get(), user.address.get()) &&
                    Objects.equals(phone.get(), user.phone.get()) &&
                    Objects.equals(isUser.get(), user.isUser.get()) &&
                    Objects.equals(firstLog.get(), user.firstLog.get());
        }

        @Override
        public String toString() {
            return "User{" +
                    "username=" + username.get() +
                    ", fullname=" + fullname.get() +
                    ", email=" + email.get() +
                    ", address=" + address.get() +
                    ", phone=" + phone.get() +
                    ", isUser=" + isUser.get() +
                    ", firstLog=" + firstLog.get() +
                    '}';
        }

        public String getUsername() {
            return username.get();
        }

        public String getFullname() {
            return fullname.get();
        }

        public String getEmail() {
            return email.get();
        }

        public String getAddress() {
            return address.get();
        }

        public String getPhone() {
            return phone.get();
        }

        public Boolean getIsUser(){return isUser.get();}

        public Boolean getFirstLog(){return firstLog.get();}

    }
}
