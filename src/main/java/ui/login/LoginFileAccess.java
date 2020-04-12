package ui.login;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.TreeMap;

public final class LoginFileAccess {

    public static TreeMap<String, String> mapOfUsers = null;

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

    public static void setMapOfUsers(TreeMap<String, String> mapOfUsers) {
        if(mapOfUsers != null){
            LoginFileAccess.mapOfUsers = mapOfUsers;
        }

    }
}
