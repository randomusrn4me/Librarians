package ui.listIssued;

import database.DatabaseHandler;
import utils.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import ui.listUsers.ListUsersController;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ListIssuedController implements Initializable {

    public MenuItem renewMenu;
    public MenuItem returnMenu;

    private User receivedUserClass;

    public void setReceivedUser(User receivedUserClass) {
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

    @FXML
    private AnchorPane rootPane;

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

        tableView.getSelectionModel().setSelectionMode(
                SelectionMode.MULTIPLE
        );
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

    public void initByHand(){
        if(receivedUserClass.getIsUser()){
            //renewMenu.setVisible(false);
            returnMenu.setVisible(false);
            System.out.println("it's a user");
        }
    }
    private void alertError(String text){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }

    public void loadRenewBook() {
        DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
        ObservableList<Issue> selectedForRenew = tableView.getSelectionModel().getSelectedItems();
        if(selectedForRenew == null){
            alertError("No book selected.\nPlease select a book to be renewed.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Renew");
        confirm.setHeaderText(null);
        confirm.setContentText("Are you sure you want to renew the selected book(s)?");

        Optional<ButtonType> response = confirm.showAndWait();
        if(response.get() != ButtonType.OK){
            return;
        }

        StringBuilder in = new StringBuilder();
        List<String> ids = selectedForRenew.stream().map(Issue::getId).collect(Collectors.toList());

        for(String s : ids){
            in.append("'").append(s).append("'");
            if(ids.lastIndexOf(s) < ids.size() - 1){
                in.append(", ");
            }
            else{
                in.append(")");
            }
        }
        StringBuilder toAlert = new StringBuilder();
        toAlert.append("The following book(s) have already been renewed 3 times " +
                "and must be returned before their due date: \n");
        boolean flag = false;
        for(Issue i : selectedForRenew){
            if(i.getRenewCount() == 3){
                flag = true;
                toAlert.append(i.getId()).append("\n");
            }
        }
        if(flag){
            alertError(toAlert.toString());
            return;
        }

        String act = "UPDATE ISSUE SET dueDate = default, " +
                "renewCount = renewCount + 1  WHERE bookID IN (" + in.toString();
        String act2 = "UPDATE ISSUE SET dueDate = DATEADD('week', 2, dueDate) WHERE bookID IN (" + in.toString();

        if(databaseHandler.execAction(act) && databaseHandler.execAction(act2)){
            for(Issue li : list){
                for(Issue issue : selectedForRenew){
                    if(issue.getId().equals(li.getId())){
                        li.setRenewCount(li.getRenewCount() + 1);
                    }
                }
            }
            tableView.refresh();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Successfully renewed selected book(s)!");
            alert.showAndWait();
        }
    }

    public void loadReturnBook() {
        DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
        ObservableList<Issue> selectedForReturn = tableView.getSelectionModel().getSelectedItems();

        if(selectedForReturn == null){
            alertError("No book(s) selected.");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Return");
        confirm.setHeaderText(null);
        confirm.setContentText("Are you sure you want to return the selected book(s)?");

        Optional<ButtonType> response = confirm.showAndWait();
        if(response.get() != ButtonType.OK){
            return;
        }

        StringBuilder in = new StringBuilder();
        List<String> ids = selectedForReturn.stream().map(Issue::getId).collect(Collectors.toList());

        for(String s : ids){
            in.append("'").append(s).append("'");
            if(ids.lastIndexOf(s) < ids.size() - 1){
                in.append(", ");
            }
            else{
                in.append(")");
            }
        }
        String act = "DELETE FROM ISSUE WHERE bookID IN (" + in.toString();
        String act2 = "UPDATE BOOK SET isAvail = true WHERE id IN (" + in.toString();

        if(databaseHandler.execAction(act) && databaseHandler.execAction(act2)){
            list.removeAll(selectedForReturn);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Successfully returned the selected book(s)!");
            alert.showAndWait();
        }
        else{
            alertError("Selected book(s) could not be returned!");
        }
    }
/*
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
    }*/
}
