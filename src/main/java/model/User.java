package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class User implements Serializable {

    private StringProperty username = new SimpleStringProperty();
    private StringProperty pwhash = new SimpleStringProperty();


    public String getUsername() {
        return username.getValue();
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public String getpwhash() {
        return pwhash.getValue();
    }

    public StringProperty pwhashProperty() {
        return pwhash;
    }

    public User( String username, String pwhash) {
        this.username.setValue(username);
        this.pwhash.setValue(pwhash);
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        s.writeUTF(username.getValue());
        s.writeUTF(pwhash.getValue());
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        this.username = new SimpleStringProperty(s.readUTF());
        this.pwhash = new SimpleStringProperty(s.readUTF());
    }
}
