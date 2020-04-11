package controller;

import com.jfoenix.controls.JFXTextField;
import javafx.scene.control.Label;
import model.Book;

import java.io.*;
import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import com.jfoenix.controls.JFXButton;
import model.User;

public class LoginFXMLCTRL implements Initializable{
    @FXML
    private JFXButton loginButton;

    @FXML
    private JFXTextField usernameBar;

    @FXML
    private JFXTextField PasswordBar;

    @FXML
    void handleLoginButtonPushed() {
        User us = new User(usernameBar.getText(), hashing(PasswordBar.getText()));

        try (FileInputStream fis = new FileInputStream("users.ser");
             ObjectInputStream ois = new ObjectInputStream(fis);) {
            TreeMap<String, String> userList = (TreeMap<String, String>) ois.readObject();
            if(userList.containsKey(usernameBar.getText())) PasswordBar.setText("Found");
            else PasswordBar.setText("Not found");
        } catch (FileNotFoundException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String hashing(String pw){
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] messageDigest = md.digest(pw.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return pw;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TreeMap<String, String> userList = new TreeMap();
        User user1 = new User("admin", hashing("admin") );
        User user2 = new User("usr", hashing("usr"));
        userList.put(user1.getUsername(), user1.getpwhash());
        userList.put(user2.getUsername(), user2.getpwhash());
        try (FileOutputStream fos = new FileOutputStream("users.ser");
             ObjectOutputStream oos = new ObjectOutputStream(fos);) {
            oos.writeObject(userList);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
