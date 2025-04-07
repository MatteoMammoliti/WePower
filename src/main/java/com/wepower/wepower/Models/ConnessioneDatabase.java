package com.wepower.wepower.Models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnessioneDatabase {
    private static final String DB_URL ="jdbc:sqlite:database/database.db";

    public static Connection getConnection() {
        Connection conn = null;
        try{
            conn = DriverManager.getConnection(DB_URL);
            System.out.println("Connessione al database riuscita");
        }catch (SQLException e){
            System.out.println("Connessione al database fallita");
            e.printStackTrace();
        }
return  conn;  }
}
