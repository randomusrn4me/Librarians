package ui.listissued;

import database.DatabaseHandler;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ui.listusers.ListUsersController;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

public class ListIssuedAdminController implements Initializable {

    private ListUsersController.User receivedUserClass;

    public void setReceivedUser(ListUsersController.User receivedUserClass) {
        this.receivedUserClass = receivedUserClass;
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

    void loadData() throws SQLException {
        DatabaseHandler databaseHandler = DatabaseHandler.getInstance();

        String qu = "SELECT * FROM ISSUE WHERE username = '" + receivedUserClass.getUsername() + "'";
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
            tableView.setItems(list);
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configureTableView();
    }

    public void loadRenewBook() {
        DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
        Issue selectedForRenew = tableView.getSelectionModel().getSelectedItem();
        if(selectedForRenew == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("No book selected.\nPlease select a book to be renewed.");
            alert.showAndWait();
            return;
        }

        int renewCount = 0;
        LocalDate dueDate = null;
        String qu = "SELECT * FROM ISSUE WHERE bookID = '" + selectedForRenew.getId() +"'";
        ResultSet rs = databaseHandler.execQuery(qu);
        while(true){
            try {
                assert rs != null;
                if (!rs.next()) break;
                renewCount = rs.getInt("renewCount");
                dueDate = rs.getDate("dueDate").toLocalDate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Book Renew");
        confirm.setHeaderText(null);
        confirm.setContentText("Are you sure you want to renew the selected book?");

        Optional<ButtonType> response = confirm.showAndWait();
        if(response.get() != ButtonType.OK){
            return;
        }

        if(renewCount == 3){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            assert dueDate != null;
            alert.setContentText("This book has already been renewed 3 times!\n " +
                    "Notify the user that it must be returned by " + dueDate.toString() + ", or they will be have to pay a fine.");
            alert.showAndWait();
            return;
        }
        if(renewCount == 2){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            assert dueDate != null;
            alert.setContentText("This book can only be renewed one more time!");
            alert.showAndWait();
        }

        String act = "UPDATE ISSUE SET dueDate = default, issueDate = default, renewCount = renewCount + 1  WHERE bookID = '" + selectedForRenew.getId() + "'";
        String act2 = "UPDATE ISSUE SET dueDate = DATEADD('week', 2, issueDate) WHERE bookID = '" + selectedForRenew.getId() + "'";
        //renew = +2 weeks
        if(databaseHandler.execAction(act) && databaseHandler.execAction(act2)){
            renewCount++;
            selectedForRenew.setRenewCount(renewCount);
            list.set(list.indexOf(selectedForRenew), selectedForRenew);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            assert dueDate != null;
            dueDate = dueDate.plusWeeks(2);
            alert.setContentText("Successfully renewed book!\n" +
                    "The new Due Date for return is: " + dueDate.toString()
                    + "\nThis book may be renewed " + (3 - renewCount) + " more time(s).");
            alert.showAndWait();
        }
    }

    public void loadReturnBook() {
        DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
        Issue selectedForReturn = tableView.getSelectionModel().getSelectedItem();
        if(selectedForReturn == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("No book selected.\nPlease select a book to be returned.");
            alert.showAndWait();
            return;
        }

        String act = "DELETE FROM ISSUE WHERE bookID = '" + selectedForReturn.getId() + "'";
        String act2 = "UPDATE BOOK SET isAvail = true WHERE id = '" + selectedForReturn.getId() +"'";

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Book Return");
        confirm.setHeaderText(null);
        confirm.setContentText("Are you sure you want to return the selected book?");

        Optional<ButtonType> response = confirm.showAndWait();
        if(response.get() != ButtonType.OK){
            return;
        }

        if(databaseHandler.execAction(act) && databaseHandler.execAction(act2)){
            list.remove(selectedForReturn);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Successfully returned book!");
            alert.showAndWait();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Book could not be returned!");
            alert.showAndWait();
        }
    }

    public static class Issue{
        private final SimpleStringProperty title;
        private final SimpleStringProperty author;
        private final SimpleStringProperty id;
        private ObjectProperty<LocalDate> issueDate;
        private ObjectProperty<LocalDate> dueDate;
        private SimpleIntegerProperty renewCount;

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

        public void setRenewCount(int renewCount) {
            this.renewCount.set(renewCount);
        }
    }
}
