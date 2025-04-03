package com.wepower.wepower.Models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ModelAutenticazione {
    public static boolean verificaCredenziali(String email, String password) throws SQLException {
        String Query = "SELECT * FROM CredenzialiCliente WHERE Email = ? AND Password = ?";
        String Query2 = "SELECT * FROM Admin WHERE Email = ? AND Password = ?";

        try (Connection conn = ConnessioneDatabase.getConnection()) {

            try (PreparedStatement datiCliente = conn.prepareStatement(Query)) {
                datiCliente.setString(1, email);
                datiCliente.setString(2, password);
                ResultSet risultatoClienti = datiCliente.executeQuery();
                if (risultatoClienti.next()) {
                    System.out.println("Login cliente effettuato con successo");
                    return true;
                }

            }
            try (PreparedStatement datiAdmin = conn.prepareStatement(Query2)) {
                datiAdmin.setString(1, email);
                datiAdmin.setString(2, password);
                ResultSet risultatoAdmin = datiAdmin.executeQuery();
                if (risultatoAdmin.next()) {
                    System.out.println("Login admin effettuato con successo");
                    return true;
                }

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("Login fallito");
        return false;
    }
}
