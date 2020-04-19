package ui.listissued;

import database.DatabaseHandler;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ResourceBundle;

public class ListIssuedController implements Initializable {

    private DatabaseHandler databaseHandler;

    public String receivedUser;

    public void setReceivedUser(String receivedUser) {
        this.receivedUser = receivedUser;
    }

    ObservableList<Issue> list = FXCollections.observableArrayList();

    @FXML
    private TableView<Issue> tableView;

    @FXML
    private TableColumn<Issue, String> titleCol;

    @FXML
    private TableColumn<Issue, String> authorCol;

    @FXML
    private TableColumn<Issue, String> idCol;

    @FXML
    private TableColumn<Issue, String> dateCol;

    @FXML
    private TableColumn<Issue, Integer> renewCol;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initCol();

        try {
            loadData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadData() throws SQLException {
        DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
        /*
        String qu = "SELECT * FROM ISSUE WHERE username = '" + username + "'";
        ResultSet rs = databaseHandler.execQuery(qu);
        while(true){
            try {
                assert rs != null;
                if (!rs.next()) break;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            String title = rs.getString("title");
            String author = rs.getString("author");
            Timestamp dateIssued = rs.getTimestamp("issueDate");
            String dIssued = dateIssued.toString();
            String id = rs.getString("bookID");
            int renewCount = rs.getInt("renewCount");

            list.add(new Issue(title, author, dIssued, id, renewCount));
        }

        tableView.getItems().setAll(list);

         */
    }

    private void initCol() {
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        idCol.setCellValueFactory(new PropertyValueFactory<>("bookID"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("issueDate"));
        renewCol.setCellValueFactory(new PropertyValueFactory<>("renewCount"));
    }

    public static class Issue{
        private final SimpleStringProperty title;
        private final SimpleStringProperty author;
        private final SimpleStringProperty dateIssued;
        private final SimpleStringProperty id;
        private final SimpleIntegerProperty renewCount;

        public Issue(String title, String author, String dateIssued, String id, int renew){
            this.title = new SimpleStringProperty(title);
            this.author = new SimpleStringProperty(author);
            this.dateIssued = new SimpleStringProperty(dateIssued);
            this.id = new SimpleStringProperty(id);
            this.renewCount = new SimpleIntegerProperty(renew);
        }

        public String getTitle() {
            return title.get();
        }

        public String getAuthor() {
            return author.get();
        }

        public String getDateIssued() {
            return dateIssued.get();
        }

        public String getId() {
            return id.get();
        }

        public int getRenewCount() { return renewCount.get();}

    }
}