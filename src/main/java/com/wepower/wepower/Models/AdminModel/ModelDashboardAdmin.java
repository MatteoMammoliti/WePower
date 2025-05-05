package com.wepower.wepower.Models.AdminModel;

import com.wepower.wepower.Models.ConnessioneDatabase;
import com.wepower.wepower.Models.ModelValidazione;
import com.wepower.wepower.Views.AlertHelper;
import javafx.scene.control.Alert;
import javafx.util.Pair;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

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
            AlertHelper.showAlert("Errore", "Compila tutti i campi", null, Alert.AlertType.ERROR);
            return false;
        }
        if(!ModelValidazione.controlloNomeOfferta(nome)){
            AlertHelper.showAlert("Errore", "Nome abbonamento non valido", "Il nome deve essere di almeno 2 caratteri e senza numeri", Alert.AlertType.ERROR);
            return false;
        }
        if(cercoNomeOfferta(nome)){
            AlertHelper.showAlert("Errore", "Nome abbonamento già presente", null, Alert.AlertType.ERROR);
            return false;
        }
        if(!ModelValidazione.controlloPrezzoOfferta(costo)){
            AlertHelper.showAlert("Errore", "Prezzo non valido", null, Alert.AlertType.ERROR);
            return false;
        }
        if(!ModelValidazione.controlloDurataOfferta(durata)){
            AlertHelper.showAlert("Errore", "Durata non valida", null, Alert.AlertType.ERROR);
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
                AlertHelper.showAlert("Promozione aggiunta", "Promozione aggiunta con successo", null, Alert.AlertType.INFORMATION);
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    //Vado a calcolare quanti clienti hanno richiesto una scheda all'admin
    public static int getNumeroSchedeRichieste(){
        String conto="SELECT COUNT(*) AS numSchede FROM SchedaAllenamento WHERE IdAdmin=1 AND SchedaAncoraInUso=1 AND SchedaCompilata=0";
        try(Connection conn=ConnessioneDatabase.getConnection()) {
            PreparedStatement preparo=conn.prepareStatement(conto);
            return preparo.executeQuery().getInt("numSchede");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<Pair<String,Integer>> getSessoUtentiPalestra(){
        String genere="SELECT Genere, NSesso FROM GraficoPerGenere";
        ArrayList<Pair<String,Integer>> lista=new ArrayList<>();
        try(Connection conn=ConnessioneDatabase.getConnection()){
            PreparedStatement preparo=conn.prepareStatement(genere);
            ResultSet rs=preparo.executeQuery();
                while(rs.next()){
                    String TipoGenere=rs.getString("Genere");
                    int cont=rs.getInt("NSesso");
                    lista.add(new Pair<>(TipoGenere,cont));
                }

            return lista;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<Integer> getAnniTendinaGrafico() {
        ArrayList<Integer> lista=new ArrayList<>();
        String anni="SELECT DISTINCT ANNO FROM Ab ORDER  BY Anno DESC";
        try(Connection conn=ConnessioneDatabase.getConnection()){
            PreparedStatement preparo=conn.prepareStatement(anni);
            ResultSet rs=preparo.executeQuery();
            while(rs.next()){
                lista.add(rs.getInt("ANNO"));
            }
            return lista;
        }
         catch (SQLException e) {
        throw new RuntimeException(e);
    }
}

public static Map<String,Integer> getDatiGraficoAnnuale(String anno) {
    System.out.println("cri");
        Map mappa=new LinkedHashMap();
        mappa.put("01",0);
        mappa.put("02",0);
        mappa.put("03",0);
        mappa.put("04",0);
        mappa.put("05",0);
        mappa.put("06",0);
        mappa.put("07",0);
        mappa.put("08",0);
        mappa.put("09",0);
        mappa.put("10",0);
        mappa.put("11",0);
        mappa.put("12",0);

        String Dati="SELECT Mese, Totale FROM Ab Where Anno=?";
        try(Connection conn=ConnessioneDatabase.getConnection()){
            PreparedStatement preparo=conn.prepareStatement(Dati);
            preparo.setString(1,anno);
            ResultSet rs=preparo.executeQuery();
            while(rs.next()){
                String chiave = String.format("%02d", rs.getInt("Mese"));
                mappa.put(chiave, rs.getInt("Totale"));
            }
            return mappa;

        }
        catch(SQLException e){
            throw new RuntimeException(e);
        }
}




}