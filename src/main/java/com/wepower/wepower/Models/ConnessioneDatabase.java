package com.wepower.wepower.Models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnessioneDatabase {
    private static final String DB_URL ="jdbc:sqlite:Database/database.db";

    public static Connection getConnection() {
        Connection conn = null;
        try{
            conn = DriverManager.getConnection(DB_URL);
            conn.createStatement().execute("PRAGMA foreign_keys = ON");
        }catch (SQLException e){
            e.printStackTrace();
        }
return  conn;  }
}
