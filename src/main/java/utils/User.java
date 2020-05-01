package utils;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.Objects;

public class User{
    private SimpleStringProperty username;
    private SimpleStringProperty fullname;
    private SimpleStringProperty email;
    private SimpleStringProperty address;
    private SimpleStringProperty phone;
    private SimpleBooleanProperty isUser;
    private SimpleBooleanProperty firstLog;

    public User(String username, String fullname, String email, String address, String phone, Boolean isUser, Boolean firstLog){
        this.username = new SimpleStringProperty(username);
        this.fullname = new SimpleStringProperty(fullname);
        this.email = new SimpleStringProperty(email);
        this.address = new SimpleStringProperty(address);
        this.phone = new SimpleStringProperty(phone);
        this.isUser = new SimpleBooleanProperty(isUser);
        this.firstLog = new SimpleBooleanProperty(firstLog);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username.get(), user.username.get()) &&
                Objects.equals(fullname.get(), user.fullname.get()) &&
                Objects.equals(email.get(), user.email.get()) &&
                Objects.equals(address.get(), user.address.get()) &&
                Objects.equals(phone.get(), user.phone.get()) &&
                Objects.equals(isUser.get(), user.isUser.get()) &&
                Objects.equals(firstLog.get(), user.firstLog.get());
    }

    @Override
    public String toString() {
        return "User{" +
                "username=" + username.get() +
                ", fullname=" + fullname.get() +
                ", email=" + email.get() +
                ", address=" + address.get() +
                ", phone=" + phone.get() +
                ", isUser=" + isUser.get() +
                ", firstLog=" + firstLog.get() +
                '}';
    }

    public String getUsername() {
        return username.get();
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public String getFullname() {
        return fullname.get();
    }

    public void setFullname(String fullname) {
        this.fullname.set(fullname);
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getAddress() {
        return address.get();
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public String getPhone() {
        return phone.get();
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }

    public Boolean getIsUser(){return isUser.get();}

    public Boolean getFirstLog(){return firstLog.get();}

}