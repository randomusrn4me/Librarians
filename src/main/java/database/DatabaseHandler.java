package database;

import javax.swing.*;
import java.sql.*;

public final class DatabaseHandler {

    private static DatabaseHandler handler;
    private static final String DB_URL = "jdbc:h2:~/database;create=true";
    private static Connection conn = null;
    private static Statement stmt = null;

    public DatabaseHandler(){
        createConnection();
        setupBookTable();
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
            JOptionPane.showMessageDialog(null, "Error:" + e.getMessage(), "Error Occured", JOptionPane.ERROR_MESSAGE);
            System.out.println("Exception at execAction:dataHandler" + e.getLocalizedMessage());
            return false;
        }
    }

    public void setupBookTable(){
        String TABLE_NAME = "BOOK";
        try{
            stmt = conn.createStatement();  //creates statement object from databse to exec commands

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
        } finally{

        }
    }

}
