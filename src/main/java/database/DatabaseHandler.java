package database;

import javax.swing.*;
import java.sql.*;

public final class DatabaseHandler {

    private static DatabaseHandler handler = null;
    private static final String DB_URL = "jdbc:h2:~/database;create=true";
    private static Connection conn = null;
    private static Statement stmt = null;

    public DatabaseHandler(){
        createConnection();
        setupBookTable();
        setupUserTable();
        setupIssueTable();
    }

    public static DatabaseHandler getInstance(){

        if(handler == null){
            handler = new DatabaseHandler();
        }
        return handler;
    }

    private void createConnection(){
        try{
            Class.forName("org.h2.Driver");
            conn = DriverManager.getConnection(DB_URL);
            //Statement st = conn.createStatement();
            //st.execute("create table test(name varchar(20))");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet execQuery(String query){
        ResultSet result;
        try{
            stmt = conn.createStatement();
            result = stmt.executeQuery(query);
        } catch (SQLException e) {
            System.out.println("Exception at execQuery:dataHandler" + e.getLocalizedMessage());
            return null;
        }
        return result;
    }

    public boolean execAction(String qu){
        try{
            stmt = conn.createStatement();
            stmt.execute(qu);
            return true;
        } catch (SQLException e) {
            //JOptionPane.showMessageDialog(null, "Error:" + e.getMessage(), "Error Occured", JOptionPane.ERROR_MESSAGE);
            System.out.println("Exception at execAction:dataHandler" + e.getLocalizedMessage());
            return false;
        }
    }

    public void setupBookTable(){
        String TABLE_NAME = "BOOK";
        try{
            stmt = conn.createStatement();  //creates statement object from database to exec commands

            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(null, null, TABLE_NAME.toUpperCase(), null);
            if(tables.next()){
                System.out.println("Table " + TABLE_NAME + "already exists. Ready to go!");
            } else{
                stmt.execute("CREATE TABLE " + TABLE_NAME + "("
                        + "     id varchar(200) primary key,\n"
                        + "     title varchar(200),\n"
                        + "     author varchar(200),\n"
                        + "     publisher varchar(100),\n"
                        + "     year varchar(100), \n"
                        + "     isAvail boolean default true"
                        + " )");
            }
        } catch(SQLException e){
            System.err.println(e.getMessage() + " --- setupDatabase");
        }
    }

    private void setupUserTable() {
        String TABLE_NAME = "USER";
        try{
            stmt = conn.createStatement();

            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(null, null, TABLE_NAME.toUpperCase(), null);
            if(tables.next()){
                System.out.println("Table " + TABLE_NAME + "already exists. Ready to go!");
            } else{
                stmt.execute("CREATE TABLE " + TABLE_NAME + "("
                        + "     username varchar(200) primary key,\n"
                        + "     fullname varchar(200),\n"
                        + "     email varchar(200),\n"
                        + "     address varchar(100),\n"
                        + "     phone varchar(100) \n"
                        + " )");
            }
        } catch(SQLException e){
            System.err.println(e.getMessage() + " --- setupDatabase");
        }
    }

    private void setupIssueTable() {
        String TABLE_NAME = "ISSUE";
        try{
            stmt = conn.createStatement();

            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(null, null, TABLE_NAME.toUpperCase(), null);
            if(tables.next()){
                System.out.println("Table " + TABLE_NAME + "already exists. Ready to go!");
            } else{
                stmt.execute("CREATE TABLE " + TABLE_NAME + "("
                        + "     bookID varchar(200) primary key,\n"
                        + "     username varchar(200),\n"
                        + "     issueDate timestamp default CURRENT_TIMESTAMP,\n"
                        + "     renewCount integer default 0,\n"
                        + "     FOREIGN KEY (bookID) REFERENCES BOOK(id),\n"
                        + "     FOREIGN KEY (username) REFERENCES USER(username)\n"
                        + " )");
            }
        } catch(SQLException e){
            System.err.println(e.getMessage() + " --- setupDatabase");
        }
    }

}
