package com.wepower.wepower.Models.AdminModel;

import com.wepower.wepower.Models.ConnessioneDatabase;
import com.wepower.wepower.Models.DatiSessioneCliente;
import com.wepower.wepower.Models.ModelValidazione;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;

import javax.swing.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class ModelDashboardAdmin {
    //Conto il numero di abbonamenti attivi
    public static int numeroAbbonamentiAttivi(){
        String numeroAbbonamenti="SELECT COUNT(*) FROM AbbonamentoCliente WHERE StatoAbbonamento=1";
        try(Connection conn= ConnessioneDatabase.getConnection()){
            PreparedStatement pst=conn.prepareStatement(numeroAbbonamenti);
            ResultSet rs=pst.executeQuery();
            if(rs.next()){
                return rs.getInt(1);
            }


        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return 0;
    }

    //Conto il numero di certificati in attesa
    public static int numeroCertificatiAttesa(){
        String numeroCertificati="SELECT COUNT(*) FROM Certificato WHERE Stato='Attesa'";
        try(Connection conn= ConnessioneDatabase.getConnection()){
            PreparedStatement pst=conn.prepareStatement(numeroCertificati);
            ResultSet rs=pst.executeQuery();
            if(rs.next()){
                return rs.getInt(1);
            }
} catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    //Conto il numero di prenotati per oggi
    public static int numeroPrenotatiOggi(){
        String numeroPrenotatiOggi="SELECT COUNT(*) FROM PrenotazioneSalaPesi WHERE DataPrenotazione=?";
        try(Connection conn= ConnessioneDatabase.getConnection()){
            PreparedStatement pst=conn.prepareStatement(numeroPrenotatiOggi);
            pst.setString(1, LocalDate.now().toString());
            ResultSet rs=pst.executeQuery();
            if(rs.next()){
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    //Prelevo i nomi e i costi delle promozioni attive
    public static ArrayList<Pair<String,String>> promozioniAttive(){
        ArrayList<Pair<String,String>> promozioniAttive=new ArrayList<>();
        String promozioni="SELECT NomeAbbonamento,Costo FROM TipoAbbonamento";
        try(Connection conn=ConnessioneDatabase.getConnection()) {
            PreparedStatement pst=conn.prepareStatement(promozioni);
            ResultSet rs=pst.executeQuery();
            while(rs.next()){
                String nomeAbbonamento=rs.getString("NomeAbbonamento");
                String prezzoAbbonamento=rs.getInt("Costo")+"€";
                promozioniAttive.add(new Pair<>(nomeAbbonamento,prezzoAbbonamento));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return promozioniAttive;
    }

    //Elimina una promozione attiva
    public static boolean eliminaPromozione(String nome) throws SQLException {
        String eliminaPromozione="DELETE FROM TipoAbbonamento WHERE NomeAbbonamento=?";
        try(Connection conn=ConnessioneDatabase.getConnection()){
            PreparedStatement caricoDati=conn.prepareStatement(eliminaPromozione);
            caricoDati.setString(1,nome);
            int righeModificate=caricoDati.executeUpdate();
            if(righeModificate>0){
                return true;
            }else{
                return false;
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    //Controllo se nome promozione è già esistente
    public static boolean cercoNomeOfferta(String nome){
        String cerco="SELECT NomeAbbonamento FROM TipoAbbonamento WHERE NomeAbbonamento=?";
        try(Connection conn= ConnessioneDatabase.getConnection()) {
            PreparedStatement caricoDati=conn.prepareStatement(cerco);
            caricoDati.setString(1,nome);
            ResultSet risultato=caricoDati.executeQuery();
            if(risultato.next()){
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    //Aggiungo una promozione
    public static boolean aggiungiPromozione(String nome,String costo,String descrizione,String durata){
        String aggiungo="INSERT into TipoAbbonamento (NomeAbbonamento,Descrizione,Costo,Durata) VALUES (?,?,?,?)";

        if(nome.isEmpty() || costo.isEmpty() || descrizione.isEmpty() || durata.isEmpty()){
            JDialog promozioneDialog=new JDialog();
            promozioneDialog.setTitle("Errore");
            JOptionPane.showMessageDialog(promozioneDialog,"Compila tutti i campi");
            return false;
        }
        if(!ModelValidazione.controlloNomeOfferta(nome)){
            JDialog promozioneDialog=new JDialog();
            promozioneDialog.setTitle("Errore");
            JOptionPane.showMessageDialog(promozioneDialog,"Nome abbonamento non valido,numeri non consentiti e non meno di 2 caratteri");
            return false;
        }
        if(cercoNomeOfferta(nome)){
            JDialog promozioneDialog=new JDialog();
            promozioneDialog.setTitle("Errore");
            JOptionPane.showMessageDialog(promozioneDialog,"Nome abbonamento già esistente");
            return false;
        }
        if(!ModelValidazione.controlloPrezzoOfferta(costo)){
            JDialog promozioneDialog=new JDialog();
            promozioneDialog.setTitle("Errore");
            JOptionPane.showMessageDialog(promozioneDialog,"Prezzo non valido");
            return false;
        }
        if(!ModelValidazione.controlloDurataOfferta(durata)){
            JDialog promozioneDialog=new JDialog();
            promozioneDialog.setTitle("Errore");
            JOptionPane.showMessageDialog(promozioneDialog,"Durata non valida");
            return false;
        }

        try(Connection conn=ConnessioneDatabase.getConnection()) {
            PreparedStatement caricoDati=conn.prepareStatement(aggiungo);
            caricoDati.setString(1,nome);
            caricoDati.setString(2,descrizione);
            caricoDati.setString(3,costo);
            caricoDati.setString(4,durata);
            int righeModificate=caricoDati.executeUpdate();
            if(righeModificate>0){
                JDialog promozioneDialog=new JDialog();
                promozioneDialog.setTitle("Promozione aggiunta");
                JOptionPane.showMessageDialog(promozioneDialog,"Promozione aggiunta con successo");
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}