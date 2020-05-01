package utils;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class Book{
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
