package com.wepower.wepower.Models.AdminModel;

import com.wepower.wepower.Models.ConnessioneDatabase;
import com.wepower.wepower.Models.ModelValidazione;
import com.wepower.wepower.Views.AlertHelper;
import javafx.scene.control.Alert;
import javafx.util.Pair;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class ModelDashboardAdmin {

    //Conto il numero di abbonamenti attivi
    public static int numeroAbbonamentiAttivi(){
        Connection conn = ConnessioneDatabase.getConnection();

        String numeroAbbonamenti="SELECT COUNT(*) FROM AbbonamentoCliente WHERE StatoAbbonamento=1";
        try {
            PreparedStatement pst = conn.prepareStatement(numeroAbbonamenti);
            ResultSet rs=pst.executeQuery();
            if(rs.next()) return rs.getInt(1);
        }catch (Exception e){
            AlertHelper.showAlert("Questo non doveva succedere", "Errore durante il reperimento degli abbonamenti attivi", null, Alert.AlertType.ERROR);
        }
        return 0;
    }

    //Conto il numero di certificati in attesa
    public static int numeroCertificatiAttesa(){
        Connection conn = ConnessioneDatabase.getConnection();

        String numeroCertificati="SELECT COUNT(*) FROM Certificato WHERE Stato='Attesa'";
        try {
            PreparedStatement pst=conn.prepareStatement(numeroCertificati);
            ResultSet rs=pst.executeQuery();
            if(rs.next())return rs.getInt(1);
        } catch (SQLException e) {
            AlertHelper.showAlert("Questo non doveva succedere", "Errore durante il reperimento dei certificati in attesa", null, Alert.AlertType.ERROR);
        }
        return 0;
    }

    //Conto il numero di prenotati per oggi
    public static int numeroPrenotatiOggi(){
        Connection conn = ConnessioneDatabase.getConnection();

        String numeroPrenotatiOggi="SELECT COUNT(*) FROM PrenotazioneSalaPesi WHERE DataPrenotazione=?";
        try {
            PreparedStatement pst=conn.prepareStatement(numeroPrenotatiOggi);
            pst.setString(1, LocalDate.now().toString());
            ResultSet rs=pst.executeQuery();

            if(rs.next()) return rs.getInt(1);

        } catch (SQLException e) {
            AlertHelper.showAlert("Questo non doveva succedere", "Errore durante il reperimento dei prenotati per oggi", null, Alert.AlertType.ERROR);
        }
        return 0;
    }

    //Prelevo i nomi e i costi delle promozioni attive
    public static ArrayList<Pair<String,String>> promozioniAttive(){
        Connection conn = ConnessioneDatabase.getConnection();

        ArrayList<Pair<String,String>> promozioniAttive=new ArrayList<>();
        String promozioni="SELECT NomeAbbonamento,Costo FROM TipoAbbonamento";

        try {
            PreparedStatement pst=conn.prepareStatement(promozioni);
            ResultSet rs=pst.executeQuery();

            while(rs.next()){
                String nomeAbbonamento=rs.getString("NomeAbbonamento");
                String prezzoAbbonamento=rs.getInt("Costo")+"€";
                promozioniAttive.add(new Pair<>(nomeAbbonamento,prezzoAbbonamento));
            }

        } catch (SQLException e) {
            AlertHelper.showAlert("Questo non doveva succedere", "Errore durante il prelevamento delle promozioni", null, Alert.AlertType.ERROR);
        }
        return promozioniAttive;
    }

    //Elimina una promozione attiva
    public static boolean eliminaPromozione(String nome) throws SQLException {
        Connection conn = ConnessioneDatabase.getConnection();

        String eliminaPromozione="DELETE FROM TipoAbbonamento WHERE NomeAbbonamento=?";

        try{
            PreparedStatement caricoDati=conn.prepareStatement(eliminaPromozione);
            caricoDati.setString(1,nome);
            int righeModificate=caricoDati.executeUpdate();
            return righeModificate > 0;
        }catch (SQLException e){
            AlertHelper.showAlert("Questo non doveva succedere", "Errore durante l'eliminazione della promozione", null, Alert.AlertType.ERROR);
        }
        return false;
    }

    //Controllo se nome promozione è già esistente
    public static boolean cercoNomeOfferta(String nome){
        Connection conn = ConnessioneDatabase.getConnection();

        String cerco="SELECT NomeAbbonamento FROM TipoAbbonamento WHERE NomeAbbonamento=?";

        try {
            PreparedStatement caricoDati=conn.prepareStatement(cerco);
            caricoDati.setString(1,nome);
            ResultSet risultato=caricoDati.executeQuery();

            if(risultato.next()) return true;

        } catch (SQLException e) {
            AlertHelper.showAlert("Questo non doveva succedere", "Errore durante la ricerca", null, Alert.AlertType.ERROR);
        }
        return false;
    }

    //Aggiungo una promozione
    public static boolean aggiungiPromozione(String nome,String costo,String descrizione,String durata){
        Connection conn = ConnessioneDatabase.getConnection();

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

        try {
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
            AlertHelper.showAlert("Questo non doveva succedere", "Errore durante l'aggiunta della promozione", null, Alert.AlertType.ERROR);
        }
        return false;
    }

    //Vado a calcolare quanti clienti hanno richiesto una scheda all'admin
    public static int getNumeroSchedeRichieste(){
        Connection conn = ConnessioneDatabase.getConnection();

        String conto="SELECT COUNT(*) AS numSchede FROM SchedaAllenamento WHERE IdAdmin=1 AND SchedaAncoraInUso=1 AND SchedaCompilata=0";
        try {
            PreparedStatement preparo=conn.prepareStatement(conto);
            return preparo.executeQuery().getInt("numSchede");
        } catch (SQLException e) {
            AlertHelper.showAlert("Questo non doveva succedere", "Errore durante il prelevamento delle richieste delle schede", null, Alert.AlertType.ERROR);
        }
        return 0;
    }

    public static ArrayList<Pair<String,Integer>> getSessoUtentiPalestra(){
        Connection conn = ConnessioneDatabase.getConnection();

        String genere="SELECT Genere, NSesso FROM GraficoPerGenere";
        ArrayList<Pair<String,Integer>> lista=new ArrayList<>();

        try {
            PreparedStatement preparo=conn.prepareStatement(genere);
            ResultSet rs=preparo.executeQuery();

            while(rs.next()){
                String TipoGenere=rs.getString("Genere");
                int cont=rs.getInt("NSesso");
                lista.add(new Pair<>(TipoGenere,cont));
            }

            return lista;
        } catch (SQLException e) {
            AlertHelper.showAlert("Questo non doveva succedere", "Errore durante il prelevamento del genere", null, Alert.AlertType.ERROR);
        }
        return lista;
    }

    //Prelevo dal database gli anni per i quali è disponibile un grafico
    public static ArrayList<Integer> getAnniTendinaGrafico() {
        Connection conn = ConnessioneDatabase.getConnection();

        ArrayList<Integer> lista=new ArrayList<>();
        String anni="SELECT DISTINCT ANNO FROM Ab ORDER  BY Anno DESC";

        try {
            PreparedStatement preparo=conn.prepareStatement(anni);
            ResultSet rs=preparo.executeQuery();

            while(rs.next()){
                lista.add(rs.getInt("ANNO"));
            }
            return lista;
        }
         catch (SQLException e) {
             AlertHelper.showAlert("Questo non doveva succedere", "Errore durante il reperimento degli anni", null, Alert.AlertType.ERROR);
        }
        return lista;
    }

    /*Prelevo dal database in base all'anno mese per mese
    il numero di abbonamenti che sono stati attivati
    e li salvo per mese in una mappa*/
    public static Map<String,Integer> getDatiGraficoAnnuale(String anno) {
        Connection conn = ConnessioneDatabase.getConnection();

        Map mappa=new LinkedHashMap();
        for (int i=1;i<13;i++){
            //Inizializzo la mappa rispettando il formato nel database nelle chiavi
            if (i<10){
                mappa.put("0"+i,0);
            }
            else{
                mappa.put(i+"",0);
            }

        }

        //Prelevo per ogni mese il numero di abbonamenti attivati
        String Dati="SELECT Mese, Totale FROM Ab Where Anno=?";
        try {
            PreparedStatement preparo=conn.prepareStatement(Dati);
            preparo.setString(1,anno);
            ResultSet rs=preparo.executeQuery();
            //Salvo tutto nella mappa
            while(rs.next()){
                int i=rs.getInt("Mese");
                String chiave="";
                if (i<10){
                    chiave = "0"+i;
                }
                else{
                    chiave=""+i;
                }

                mappa.put(chiave, rs.getInt("Totale"));
            }
            return mappa;
        }
        catch(SQLException e){
            AlertHelper.showAlert("Questo non doveva succedere", "Errore durante il reperimento dei dati", null, Alert.AlertType.ERROR);
        }
        return mappa;
    }
}