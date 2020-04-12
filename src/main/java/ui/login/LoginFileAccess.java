package ui.login;

import java.io.*;
import java.util.TreeMap;

public final class LoginFileAccess {

    private static TreeMap<String, String> mapOfUsers = null;

    public LoginFileAccess(){
        if(mapOfUsers == null){
            try (FileInputStream fis = new FileInputStream("users.ser");
                 ObjectInputStream ois = new ObjectInputStream(fis);) {
                mapOfUsers = (TreeMap<String, String>) ois.readObject();
            } catch (FileNotFoundException | ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public TreeMap<String, String> getMapOfUsers(){
        return mapOfUsers;
    }

    public void addUser(String username, String passwordHash){
        if(mapOfUsers.containsKey(username)){
            System.out.println("User is already registered!");
            return;
        }
        mapOfUsers.put(username, passwordHash);
        try (FileOutputStream fos = new FileOutputStream("users.ser");
             ObjectOutputStream oos = new ObjectOutputStream(fos);) {
            oos.writeObject(mapOfUsers);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
/*
    public void setMapOfUsers(TreeMap<String, String> mapOfUsers) {
            LoginFileAccess.mapOfUsers = mapOfUsers;
            try (FileOutputStream fos = new FileOutputStream("users.ser");
                 ObjectOutputStream oos = new ObjectOutputStream(fos);) {
                oos.writeObject(LoginFileAccess.mapOfUsers);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

    }*/
}
