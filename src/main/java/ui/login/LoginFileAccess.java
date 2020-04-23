package ui.login;

import database.DatabaseHandler;

import javax.xml.transform.Result;
import java.io.*;
import java.sql.ResultSet;
import java.util.TreeMap;

public final class LoginFileAccess {

    /*
    private static TreeMap<String, String[]> mapOfUsers = null;

    public LoginFileAccess(){
        /*mapOfUsers = new TreeMap<String, String[]>();
        mapOfUsers.put("admin", new String[] {LoginController.hashing("admin"), "admin"});
        mapOfUsers.put("usr", new String[] {LoginController.hashing("usr"), "user"});
        mapOfUsers.put("asd989", new String[] {LoginController.hashing("bookworm"), "user"});

        if(mapOfUsers == null){
            try (FileInputStream fis = new FileInputStream("users.ser");
                 ObjectInputStream ois = new ObjectInputStream(fis);) {
                mapOfUsers = (TreeMap<String, String[]>) ois.readObject();
            } catch (Exception e) {
                System.out.println("Exception detected. Creating new file...");
                createMap();
                e.printStackTrace();
            }
        }
    }


    public TreeMap<String, String[]> getMapOfUsers(){ return mapOfUsers; }

    private void createMap(){
        if(mapOfUsers != null){
            System.out.println("Map already exists!");
            return;
        }
        mapOfUsers = new TreeMap<String, String[]>();
        writeFile();
    }

    private void writeFile(){
        try (FileOutputStream fos = new FileOutputStream("users.ser");
             ObjectOutputStream oos = new ObjectOutputStream(fos);) {
            oos.writeObject(mapOfUsers);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addUser(String username, String passwordHash, String type){
        if(mapOfUsers.containsKey(username)){
            System.out.println("User is already registered!");
            return;
        }
        mapOfUsers.put(username, new String[] {passwordHash, type});
        writeFile();
    }



    public void modifyUser(String username, String passwordHash){

        if(mapOfUsers.containsKey(username)){
            mapOfUsers.put(username, new String[] {passwordHash, type});
            writeFile();
        } else {
            System.out.println("This user is not registered!");
        }

    }

    public void removeUser(String username){


        if(!mapOfUsers.containsKey(username)){
            System.out.println("No such user!");
            return;
        }
        mapOfUsers.remove(username);
        writeFile();


    }


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

    }


    */

}
