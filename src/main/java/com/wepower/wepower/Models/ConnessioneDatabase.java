package com.wepower.wepower.Models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnessioneDatabase {
    private static final String DB_URL ="jdbc:sqlite:Database/database.db";
    private static Connection conn = null;

    public static Connection getConnection() {
        try{
            if (conn == null || conn.isClosed()) {
                conn = DriverManager.getConnection(DB_URL);
                conn.createStatement().execute("PRAGMA foreign_keys = ON");
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return  conn;
    }

    public static void closeConnection() {
        try {
            if(conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}