package ui.search;

import utils.*;
import com.jfoenix.controls.JFXTextField;
import database.DatabaseHandler;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.awt.print.Book;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class SearchController implements Initializable {

    ObservableList<Book> list = FXCollections.observableArrayList();

    @FXML
    private AnchorPane rootPane;

    @FXML
    private TableView<Book> tableView;

    @FXML
    private TableColumn<Book, String> idCol;

    @FXML
    private TableColumn<Book, String> titleCol;

    @FXML
    private TableColumn<Book, String> authorCol;

    @FXML
    private TableColumn<Book, String> yearCol;

    @FXML
    private TableColumn<Book, String> publisherCol;

    @FXML
    private TableColumn<Book, Boolean> availabilityCol;

    @FXML
    private JFXTextField titleInput;

    @FXML
    private JFXTextField authorInput;

    @FXML
    private JFXTextField publisherInput;

    @FXML
    private JFXTextField yearInput;

    @FXML
    private JFXTextField bookIDInput;

    private DatabaseHandler databaseHandler;

    private void alertError(String text){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }

    @FXML
    void handleCancelButtonPushed() {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    @FXML
    void handleSearchButtonPushed() throws SQLException {
        databaseHandler = DatabaseHandler.getInstance();

        list.clear();
        tableView.getItems().clear();

        String bookTitle = titleInput.getText();
        String bookAuthor = authorInput.getText();
        String bookYear = yearInput.getText();
        String bookPublisher = publisherInput.getText();
        String bookID = bookIDInput.getText();
        TreeMap<String, String> notEmp = new TreeMap<String, String>();

        if(!bookTitle.isEmpty()){
            notEmp.put("title", bookTitle);
        }
        if(!bookAuthor.isEmpty()){
            notEmp.put("author", bookAuthor);
        }
        if(!bookYear.isEmpty()){
            notEmp.put("year", bookYear);
        }
        if(!bookPublisher.isEmpty()){
            notEmp.put("publisher", bookPublisher);
        }
        if(!bookID.isEmpty()){
            notEmp.put("id", bookID);
        }

        if(notEmp.size() < 1){
            alertError("Please fill out one or more fields to search!");
            return;
        }
        else if(!bookYear.isEmpty() && !Character.isDigit(bookYear.charAt(0))){
            alertError("Please enter a valid year.");
            return;
        }
        else if(!bookYear.isEmpty() && Integer.parseInt(bookYear) > Calendar.getInstance().get(Calendar.YEAR)){
            alertError("Please enter a valid year.");
            return;
        }

        StringBuilder qu = new StringBuilder("SELECT * FROM BOOK WHERE LOWER(");
        ResultSet rs;
        boolean flag;

        for(Map.Entry<String, String> entry : notEmp.entrySet()) {
            if (entry.getKey().equals(notEmp.lastKey())) {
                if(entry.getKey().equals("title") || entry.getKey().equals("author") || entry.getKey().equals("publisher")){
                    qu.append(entry.getKey()).append(") LIKE '%").append(entry.getValue().toLowerCase()).append("%'");
                }else{
                    qu.append(entry.getKey()).append(") = '").append(entry.getValue().toLowerCase()).append("'");
                }
            }else if(entry.getKey().equals("title") || entry.getKey().equals("author") || entry.getKey().equals("publisher")){
                qu.append(entry.getKey()).append(") LIKE '%").append(entry.getValue().toLowerCase()).append("%'");
                qu.append(" AND LOWER(");
            }
            else {
                qu.append(entry.getKey()).append(") = '").append(entry.getValue().toLowerCase()).append("'");
                qu.append(" AND LOWER(");
            }
        }

        rs = databaseHandler.execQuery(qu.toString());
        flag = false;
        while(true){
            try {
                assert rs != null;
                if (!rs.next()) break;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            String tmpTitle = rs.getString("title");
            String tmpAuthor = rs.getString("author");
            String tmpPublisher = rs.getString("publisher");
            String tmpYear = rs.getString("year");
            String tmpID = rs.getString("id");
            Boolean tmpAvail = rs.getBoolean("isAvail");
            flag = true;

            list.add(new Book(tmpTitle, tmpAuthor, tmpPublisher, tmpYear, tmpID, tmpAvail));
        }
        if(!flag){
            alertError("No such book with those parameters. \nPlease enter valid search terms.");
            return;
        }

        tableView.getItems().setAll(list);

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initCol();
        rootPane.setFocusTraversable(true);
        rootPane.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ESCAPE)  {
                    ((Stage) rootPane.getScene().getWindow()).close();
                }
            }
        });

        rootPane.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER)  {
                    try {
                        handleSearchButtonPushed();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    private void initCol() {
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        publisherCol.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        yearCol.setCellValueFactory(new PropertyValueFactory<>("year"));
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        availabilityCol.setCellValueFactory(new PropertyValueFactory<>("availability"));
    }

    public static class Book{
        private final SimpleStringProperty title;
        private final SimpleStringProperty author;
        private final SimpleStringProperty publisher;
        private final SimpleStringProperty year;
        private final SimpleStringProperty id;
        private final SimpleBooleanProperty availability;

        public Book(String title, String author, String publisher, String year, String id, Boolean avail){
            this.title = new SimpleStringProperty(title);
            this.author = new SimpleStringProperty(author);
            this.publisher = new SimpleStringProperty(publisher);
            this.year = new SimpleStringProperty(year);
            this.id = new SimpleStringProperty(id);
            this.availability = new SimpleBooleanProperty(avail);
        }

        public String getTitle() {
            return title.get();
        }

        public String getAuthor() {
            return author.get();
        }

        public String getPublisher() {
            return publisher.get();
        }

        public String getYear() {
            return year.get();
        }

        public String getId() {
            return id.get();
        }

        public boolean isAvailability() {
            return availability.get();
        }

    }
}

