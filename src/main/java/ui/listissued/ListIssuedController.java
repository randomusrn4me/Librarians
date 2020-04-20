package ui.listissued;

import database.DatabaseHandler;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ListIssuedController implements Initializable {

    private DatabaseHandler databaseHandler;

    public String receivedUser;

    public void setReceivedUser(String receivedUser) {
        this.receivedUser = receivedUser;
        try {
            loadData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
    private TableColumn<Issue, LocalDate> dateCol;

    @FXML
    private TableColumn<Issue, LocalDate> dueDateCol;

    @FXML
    private TableColumn<Issue, Integer> renewCol;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configureTableView();
    }

    private void loadData() throws SQLException {
        DatabaseHandler databaseHandler = DatabaseHandler.getInstance();

        String qu = "SELECT * FROM ISSUE WHERE username = '" + receivedUser + "'";
        ResultSet rs = databaseHandler.execQuery(qu);
        while(true){
            try {
                assert rs != null;
                if (!rs.next()) break;

                String title = null;
                String author = null;

                LocalDate dIssued = rs.getDate("issueDate").toLocalDate();
                LocalDate dueDate = rs.getDate("dueDate").toLocalDate();
                String id = rs.getString("bookID");
                int renewCount = rs.getInt("renewCount");

                qu = "SELECT * FROM BOOK WHERE id = '" + id + "'";
                ResultSet rsBook = databaseHandler.execQuery(qu);
                assert rsBook != null;
                while(rsBook.next()){
                    title = rsBook.getString("title");
                    author = rsBook.getString("author");
                }

                list.add(new Issue(title, author, id, dIssued, dueDate, renewCount));

            } catch (SQLException e) {
                e.printStackTrace();
            }
            tableView.getItems().setAll(list);
        }




    }

    private void configureTableView(){
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        dateCol.setCellValueFactory(cellData -> cellData.getValue().issueDateProperty());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        dateCol.setCellFactory(column -> {
            return new TableCell<>() {
                @Override
                protected void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(formatter.format(item));

                    }
                }
            };
        });
        dateCol.setCellValueFactory(new PropertyValueFactory<>("issueDate"));

        dueDateCol.setCellValueFactory(cellData -> cellData.getValue().issueDateProperty());

        //DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        dueDateCol.setCellFactory(column -> {
            return new TableCell<>() {
                @Override
                protected void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(formatter.format(item));

                    }
                }
            };
        });
        dueDateCol.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        renewCol.setCellValueFactory(new PropertyValueFactory<>("renewCount"));

    }

    public static class Issue{
        private final SimpleStringProperty title;
        private final SimpleStringProperty author;
        private final SimpleStringProperty id;
        private ObjectProperty<LocalDate> issueDate;
        private ObjectProperty<LocalDate> dueDate;
        private final SimpleIntegerProperty renewCount;

        public Issue(String title, String author, String id, LocalDate issueDate, LocalDate dueDate, int renewCount){
            this.title = new SimpleStringProperty(title);
            this.author = new SimpleStringProperty(author);
            this.issueDate = new SimpleObjectProperty<>(issueDate);
            this.dueDate = new SimpleObjectProperty<>(dueDate);
            this.id = new SimpleStringProperty(id);
            this.renewCount = new SimpleIntegerProperty(renewCount);
        }

        public String getTitle() {
            return title.get();
        }

        public String getAuthor() {
            return author.get();
        }

        public LocalDate getIssueDate() {
            return issueDate.get();
        }

        public void setIssueDate(LocalDate issueDate) {
            this.issueDate = new SimpleObjectProperty<>(issueDate);
        }

        public ObjectProperty<LocalDate> issueDateProperty() {
            return issueDate;
        }

        public LocalDate getDueDate() {
            return dueDate.get();
        }

        public void setDueDate(LocalDate dueDate) {
            this.dueDate = new SimpleObjectProperty<>(dueDate);
        }

        public ObjectProperty<LocalDate> dueDateProperty() {
            return dueDate;
        }

        public String getId() {
            return id.get();
        }

        public int getRenewCount() { return renewCount.get();}

    }
}