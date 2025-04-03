package com.wepower.wepower.Models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ModelAutenticazione {
    public static boolean verificaCredenziali(String email,String password) throws SQLException {
        String Query = "SELECT * FROM CredenzialiCliente WHERE Email = ? AND Password = ?";

        try(Connection conn = ConnessioneDatabase.getConnection();
        PreparedStatement dati = conn.prepareStatement(Query)){
            dati.setString(1, email);
            dati.setString(2, password);
            ResultSet risultato = dati.executeQuery();
            return risultato.next();

        }catch(SQLException e){
            e.printStackTrace();
            System.out.println("Errore login");
            return false;
        }

    }
}
