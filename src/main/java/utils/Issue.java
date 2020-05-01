package utils;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;

public class Issue{
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
