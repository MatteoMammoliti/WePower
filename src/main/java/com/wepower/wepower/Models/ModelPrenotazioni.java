package com.wepower.wepower.Models;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ModelPrenotazioni {

    public static boolean aggiuntiPrenotazioneSalaPesi(String data, String orario, int idUtente) throws SQLException {
            if(!DatiSessioneCliente.getStatoAbbonamento()){
                JOptionPane.showMessageDialog(null,"Non hai un abbonamento attivo, impossibile prenotare");
                return false;
            }
            if (orario.matches("\\d{1,2}")) {
                int ora = Integer.parseInt(orario);
                orario = String.format("%02d:00", ora);
            }
            String Query = "INSERT into PrenotazioneSalaPesi (IdCliente,IdSalaPesi,DataPrenotazione,OrarioPrenotazione) VALUES (?,?,?,?)";
            try (Connection conn = ConnessioneDatabase.getConnection()) {
                try (PreparedStatement dati = conn.prepareStatement(Query)) {
                    dati.setInt(1, idUtente);
                    dati.setInt(2, 1);
                    dati.setString(3, data);
                    dati.setString(4, orario);


                    int righeAffette = dati.executeUpdate();

                    if (righeAffette > 0) {
                        System.out.println("Inserimento riuscito!");
                        return true;
                    } else {
                        System.out.println("Errore nell'inserimento.");
                        return false;
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
