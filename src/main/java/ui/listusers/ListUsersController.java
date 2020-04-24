package ui.listusers;

import database.DatabaseHandler;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ui.listbooks.ListBooksController;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ListUsersController implements Initializable {

    ObservableList<User> list = FXCollections.observableArrayList();

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initCol();
        try {
            loadData();
        } catch (SQLException e) {
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
