package com.wepower.wepower.Models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ModelAutenticazione {
    public static boolean verificaCredenziali(String email, String password) throws SQLException {
       // String Query = "SELECT * FROM CredenzialiCliente WHERE Email = ? AND Password = ?";
       // String Query2 = "SELECT * FROM Admin WHERE Email = ? AND Password = ?";
        String Query="SELECT c.idCliente,c.CertificatoValido,c.nome,cc.Email,cc.Telefono FROM CredenzialiCliente cc JOIN Cliente c ON cc.idCliente=c.idCliente WHERE cc.Email = ? AND cc.Password = ?";

        try (Connection conn = ConnessioneDatabase.getConnection()) {

            try (PreparedStatement datiCliente = conn.prepareStatement(Query)) {
                datiCliente.setString(1, email);
                datiCliente.setString(2, password);
                ResultSet risultatoClienti = datiCliente.executeQuery();
                if (risultatoClienti.next()) {
                    System.out.println("Login cliente effettuato con successo");
                    DatiSessioneCliente.setIdUtente(risultatoClienti.getInt("idCliente"));
                    DatiSessioneCliente.setNomeUtente(risultatoClienti.getString("nome"));
                    DatiSessioneCliente.setEmail(risultatoClienti.getString("Email"));
                    DatiSessioneCliente.setCertificato(risultatoClienti.getBoolean("CertificatoValido"));
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
