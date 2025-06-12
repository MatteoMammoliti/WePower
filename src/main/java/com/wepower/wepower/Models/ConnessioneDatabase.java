package com.wepower.wepower.Models;

import com.wepower.wepower.Views.AlertHelper;
import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnessioneDatabase {
    private static final String DB_URL ="jdbc:sqlite:database/database.db";
    private static Connection conn = null;

    public static Connection getConnection() {
        try{
            if (conn == null || conn.isClosed()) {
                conn = DriverManager.getConnection(DB_URL);
                conn.createStatement().execute("PRAGMA foreign_keys = ON");
            }
        }catch (SQLException e){
            AlertHelper.showAlert("Questo non doveva succedere", "Qualcosa è andato nella connessione al database", null, Alert.AlertType.ERROR);
        }
        return  conn;
    }

    public static void closeConnection() {
        try {
            if(conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            AlertHelper.showAlert("Questo non doveva succedere", "Qualcosa è andato nella chiusura della connessione al database", null, Alert.AlertType.ERROR);
        }
    }
}