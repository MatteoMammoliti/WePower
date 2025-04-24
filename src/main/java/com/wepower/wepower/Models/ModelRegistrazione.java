package com.wepower.wepower.Models;

import java.sql.*;
import java.time.LocalDate;


public class ModelRegistrazione {
    public static boolean verificaEmailEsistente(String email) throws SQLException {
        String query="SELECT * FROM CredenzialiCliente WHERE Email = ?";
        try(Connection conn=ConnessioneDatabase.getConnection()){
            try(PreparedStatement dati=conn.prepareStatement(query)){
                dati.setString(1,email);
                ResultSet risultato=dati.executeQuery();
                if(risultato.next()){
                    return true;
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return false;
    };

    public static boolean registraUtente(String nome, String cognome, LocalDate dataNascita, String email, String password) throws SQLException {
        String insertCliente = "INSERT INTO Cliente (Nome, Cognome, DataNascita) VALUES (?, ?, ?)";
        String insertCredenziali = "INSERT INTO CredenzialiCliente (Email, Password, IdCliente) VALUES (?, ?, ?)";


        try (Connection conn = ConnessioneDatabase.getConnection()) {
            conn.setAutoCommit(false);

            // 1. Inserisci cliente
            try (PreparedStatement datiCliente = conn.prepareStatement(insertCliente, Statement.RETURN_GENERATED_KEYS)) {
                datiCliente.setString(1, nome);
                datiCliente.setString(2, cognome);
                datiCliente.setString(3, dataNascita.toString());

                int affectedRows = datiCliente.executeUpdate();

                if (affectedRows == 0) {
                    conn.rollback();
                    System.out.println("Inserimento cliente fallito.");
                    return false;
                }

                // 2. Recupera ID cliente
                try (ResultSet generatedKeys = datiCliente.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int idCliente = generatedKeys.getInt(1);

                        // 3. Inserisci credenziali
                        try (PreparedStatement psCredenziali = conn.prepareStatement(insertCredenziali)) {
                            psCredenziali.setString(1, email);
                            psCredenziali.setString(2, password);
                            psCredenziali.setInt(3, idCliente);

                            psCredenziali.executeUpdate();
                            System.out.println("Registrazione completata.");
                            conn.commit();
                            return true;
                        }
                    } else {
                        conn.rollback();
                        System.out.println(" Nessun ID cliente generato.");
                        return false;
                    }
                }
            } catch (Exception e) {
                conn.rollback();
                throw new RuntimeException(e);
            } finally {
                conn.setAutoCommit(true);
            }

        }
    }



}

