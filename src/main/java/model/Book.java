package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Book implements Serializable {

    private StringProperty title = new SimpleStringProperty();
    private StringProperty author = new SimpleStringProperty();
    private int year;
    private StringProperty publisher = new SimpleStringProperty();

    public String getTitle() {
        return title.getValue();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public String getAuthor() {
        return author.getValue();
    }

    public StringProperty authorProperty() {
        return author;
    }

    public int getYear() {
        return year;
    }

    public String getPublisher() {
        return publisher.getValue();
    }

    public StringProperty publisherProperty() {
        return publisher;
    }

    public Book(String title, String author, int year, String publisher) {
        this.title.setValue(title);
        this.author.setValue(author);
        this.year = year;
        this.publisher.setValue(publisher);
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        s.writeUTF(title.getValue());
        s.writeUTF(author.getValue());
        s.writeInt(year);
        s.writeUTF(publisher.getValue());
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        this.title = new SimpleStringProperty(s.readUTF());
        this.author = new SimpleStringProperty(s.readUTF());
        this.year = s.readInt();
        this.publisher = new SimpleStringProperty(s.readUTF());
    }

}
