package com.wepower.wepower.Models;

import com.wepower.wepower.Views.AlertHelper;
import javafx.scene.control.Alert;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.sql.*;
import java.time.LocalDate;


public class ModelRegistrazione {

    public static boolean verificaEmailEsistente(String email) {
        Connection conn = ConnessioneDatabase.getConnection();
        String emailLower = email.toLowerCase();
        String query="SELECT * FROM CredenzialiCliente WHERE Email = ?";

        PreparedStatement dati = null;
        ResultSet risultato = null;
        try {
            dati=conn.prepareStatement(query);
            dati.setString(1,emailLower);
            risultato=dati.executeQuery();

            if(risultato.next()) return true;

        }catch(SQLException e){
            AlertHelper.showAlert("Questo non doveva succedere", " Errore durante la verifica campo email", null, Alert.AlertType.ERROR);
        } finally {
            if (dati != null) {
                try { dati.close(); } catch (SQLException ignored) {}
            }
            if(risultato != null) {
                try { risultato.close(); } catch (SQLException ignored) {}
            }
        }
        return false;
    }

    public static boolean registraUtente(String nome, String cognome, LocalDate dataNascita, String email, String password) throws SQLException {
        Connection conn = ConnessioneDatabase.getConnection();

        String emailLower = email.toLowerCase();
        String pwcript = BCrypt.hashpw(password,BCrypt.gensalt(12));

        String insertCliente = "INSERT INTO Cliente (Nome, Cognome, DataNascita) VALUES (?, ?, ?)";
        String insertCredenziali = "INSERT INTO CredenzialiCliente (Email, Password, IdCliente) VALUES (?, ?, ?)";

        PreparedStatement datiCliente = null;
        ResultSet generatedKeys = null;
        PreparedStatement psCredenziali = null;
        try {
            conn.setAutoCommit(false);

            // 1. Inserisci cliente
            datiCliente = conn.prepareStatement(insertCliente, Statement.RETURN_GENERATED_KEYS);
            datiCliente.setString(1, nome);
            datiCliente.setString(2, cognome);
            datiCliente.setString(3, dataNascita.toString());

            int affectedRows = datiCliente.executeUpdate();

            if (affectedRows == 0) {
                conn.rollback();
                return false;
            }

            // 2. Recupera ID cliente
            generatedKeys = datiCliente.getGeneratedKeys();
            if (generatedKeys.next()) {
                int idCliente = generatedKeys.getInt(1);

                // 3. Inserisci credenziali
                psCredenziali = conn.prepareStatement(insertCredenziali);
                psCredenziali.setString(1, emailLower);
                psCredenziali.setString(2, pwcript);
                psCredenziali.setInt(3, idCliente);

                psCredenziali.executeUpdate();
                conn.commit();
                return true;
            } else {
                conn.rollback();
                return false;
            }
        } catch (Exception e) {
            conn.rollback();
            AlertHelper.showAlert("Questo non doveva succedere", " Errore durante la registrazione", null, Alert.AlertType.ERROR);
        } finally {
            conn.setAutoCommit(true);
            if (psCredenziali != null) {
                try { psCredenziali.close(); } catch (SQLException ignored) {}
            }
            if(generatedKeys != null) {
                try { generatedKeys.close(); } catch (SQLException ignored) {}
            }
            if(datiCliente != null) {
                try { datiCliente.close(); } catch (SQLException ignored) {}
            }
        }
        return false;
    }
}