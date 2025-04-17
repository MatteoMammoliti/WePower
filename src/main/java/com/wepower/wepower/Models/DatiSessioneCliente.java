package com.wepower.wepower.Models;

import com.wepower.wepower.Models.DatiPalestra.PrenotazioneSalaPesi;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class DatiSessioneCliente {
    private static int idUtente;
    private static boolean statoAbbonamento;
    private static String email;
    private static String nome;
    private static String cognome;
    private static int certificato;
    private static String telefono;
    private static int idSchedaAllenamento;
    private static String dataNascita;
    private static String altezza;
    private static Integer pesoAttuale;
    private static String genere;
    private static Image immagineProfilo;
    private static boolean alertScadenzaAbbonamento = false;
    private static boolean alertCertificatoMancante = false;

    //Tutto lo storico delle prenotazioni sala pesi dell'utente
    private static ArrayList<PrenotazioneSalaPesi> dateOrariPrenotazioni = new ArrayList<>();

    //Set per accedere velocemente ad una dataPrenotazione per velocizzare e alleggerire i calcoli
    private static Set<String> datePrenotazioniSalaPesi = new HashSet<>();

    private static ArrayList<String>  eserciziConMassimale = new ArrayList<>();

    // GETTER
    public static int getIdUtente() {
        return idUtente;
    }
    public static String getEmail() {
        return email;
    }
    public static String getNomeUtente() {
        return nome;
    }
    public static String getAltezza() {return altezza;}
    public static Integer getPesoAttuale() {return pesoAttuale;}

    public  static String getTelefono() {
        return telefono;
    }
    public static int getCertificato() {
        return certificato;
    }
    public static String getCognome() { return cognome; }
    public static ArrayList<PrenotazioneSalaPesi> getDateOrariPrenotazioni() {
        return dateOrariPrenotazioni;
    }
    public static boolean getStatoAbbonamento(){return statoAbbonamento;}
    public static int getIdSchedaAllenamento() { return idSchedaAllenamento; }
    public static String getOrarioPrenotazione(String data){
        for (int i=0;i<dateOrariPrenotazioni.size();i++){
            if (dateOrariPrenotazioni.get(i).getDataPrenotazione().equals(data)){
                return dateOrariPrenotazioni.get(i).getOrarioPrenotazione();
            }
        }
        return null;
    }
    public static String getDataNascita(){return dataNascita;}
    public static Image getImmagineProfilo() { return immagineProfilo; }
    public static String getGenere() {return genere;}
    public static ArrayList<String> getEserciziConMassimale() {
        return eserciziConMassimale;
    }
    public static boolean getAlertScadenzaAbbonamento() { return alertScadenzaAbbonamento; }
    public static boolean getAlertCertificatoMancante() { return alertCertificatoMancante; }

    // SETTER
    public static void setStatoAbbonamento(boolean abbonamento){statoAbbonamento = abbonamento;}
    public static void setDataNascita(String data){dataNascita=data;}
    public static void setNomeUtente(String n) {
        nome=n;
    }
    public static void setIdUtente(int id) {
        idUtente = id;
    }
    public static void setEmail(String e_mail){
        email = e_mail;
    }
    public static void setAltezza(String a) {altezza = a;}
    public static void setPesoAttuale(Integer p) {pesoAttuale = p;}

    public static void setCertificato(int valore) {
        certificato = valore;
    }
    public static void setCognome(String c) { cognome = c; }
    public static void setTelefono(String t) {telefono = t; }
    public static void setIdSchedaAllenamento(int id) { idSchedaAllenamento = id; }
    public static void setImmagineProfilo(Image immagine) { immagineProfilo = immagine; }
    public static void setDateOrariPrenotazioni(ArrayList<PrenotazioneSalaPesi> d) {
        dateOrariPrenotazioni = d;

        datePrenotazioniSalaPesi.clear();
        for (int i = 0; i < dateOrariPrenotazioni.size(); i++) {
            datePrenotazioniSalaPesi.add(dateOrariPrenotazioni.get(i).getDataPrenotazione());
        }
    }
    public static void setEserciziConMassimale(ArrayList<String> esercizi) {
        eserciziConMassimale = esercizi;
    }
    public static void setGenere(String s) {genere = s;}

    public static void setAlertScadenzaAbbonamento(boolean alert) { alertScadenzaAbbonamento = alert; }
    public static void setAlertCertificatoMancante(boolean alert) { alertCertificatoMancante = alert; }


    // LOGOUT
    public static void logout() {
        idUtente = 0;
        email = null;
        nome = null;
        certificato = 0;
        telefono = null;
        dateOrariPrenotazioni.clear();
        datePrenotazioniSalaPesi.clear();
        statoAbbonamento = false;
        idSchedaAllenamento = 0;
        altezza = null;
        pesoAttuale = null;
        genere=null;
        immagineProfilo = null;
        eserciziConMassimale.clear();
        alertScadenzaAbbonamento = false;
        alertCertificatoMancante = false;
        Model.invalidate();
    }

    // CONTROLLO DATA PRENOTAZIONE SALA PESI
    public static boolean controlloDataPrenotazioneSalaPesi(LocalDate data) {
        String dataControllo = data.toString();

        if (datePrenotazioniSalaPesi.contains(dataControllo)) {
            return true;
        }
        return false;
    }

    // CONTROLLO DATA E ORARIO PRENOTAZIONE SALA PESI PER STORICO
    public static boolean controlloDataPrenotazioneSalaPesi(LocalDate data,String ora) {
        String dataControllo = data.toString();
        PrenotazioneSalaPesi temp=new PrenotazioneSalaPesi(dataControllo,ora);

        if (dateOrariPrenotazioni.contains(temp)) {
            return true;
        }
        return false;
    }

    public static String getTipoAbbonamentoAttivo(){
        String abbonamentoAttivo;
        String nomeTipoAbbonamento="SELECT t.NomeAbbonamento from TipoAbbonamento t join AbbonamentoCliente a on t.IdTipoAbbonamento=a.IdTipoAbbonamento where a.IdCliente=?";
        try(Connection conn=ConnessioneDatabase.getConnection()){
            try(PreparedStatement nomeAbbonamento=conn.prepareStatement(nomeTipoAbbonamento)){
                nomeAbbonamento.setInt(1,idUtente);
                ResultSet risultato=nomeAbbonamento.executeQuery();
                if(risultato.next()){
                    abbonamentoAttivo=risultato.getString("NomeAbbonamento");
                    return abbonamentoAttivo;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static String getDataInizioAbbonamentoAttivo(){
        String dataInizioAbbonamento="SELECT ac.DataInizioAbbonamento from AbbonamentoCliente ac where ac.IdCliente=? and ac.StatoAbbonamento=1";
        try(Connection conn=ConnessioneDatabase.getConnection()){
            try(PreparedStatement dataInizio=conn.prepareStatement(dataInizioAbbonamento)){
                dataInizio.setInt(1,idUtente);
                ResultSet risultato=dataInizio.executeQuery();
                if(risultato.next()){
                    return risultato.getString("DataInizioAbbonamento");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static String getDataFineAbbonamentoAttivo() throws SQLException {
        String dataFineAbbonamentoAttivo="SELECT ac.DataFineAbbonamento from AbbonamentoCliente ac where ac.IdCliente= ? and ac.StatoAbbonamento=1";
        try(Connection conn=ConnessioneDatabase.getConnection()){
            try (PreparedStatement dataFine=conn.prepareStatement(dataFineAbbonamentoAttivo)){
                dataFine.setInt(1, idUtente);
                ResultSet risultato=dataFine.executeQuery();
                if (risultato.next()) {
                    return risultato.getString("DataFineAbbonamento");
                }
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        System.out.println("no");
        return null;
    }

    public static boolean isAbbonamentoAttivo() throws SQLException {
        String dataInizioAbbonamento = getDataInizioAbbonamentoAttivo();
        String dataFineAbbonamento = getDataFineAbbonamentoAttivo();

        if (dataInizioAbbonamento != null && dataFineAbbonamento != null) {
            LocalDate dataInizio = LocalDate.parse(dataInizioAbbonamento);
            LocalDate dataFine = LocalDate.parse(dataFineAbbonamento);
            LocalDate oggi = LocalDate.now();

            if (oggi.isAfter(dataInizio) && oggi.isBefore(dataFine)) {
                return true;
            }
        }
        return false;
    }

    //AGGIUNGI UNA PRENOTAZIONE
    public static void aggiungiPrenotazione(PrenotazioneSalaPesi p) {
        dateOrariPrenotazioni.add(p);
        datePrenotazioniSalaPesi.add(p.getDataPrenotazione());
    }
    //RIMUOVI UNA PRENOTAZIONE
    public static void rimuoviPrenotazione(PrenotazioneSalaPesi p) {
        dateOrariPrenotazioni.remove(p);
        datePrenotazioniSalaPesi.remove(p.getDataPrenotazione());
        for (int i=0;dateOrariPrenotazioni.size()>i;i++){
            System.out.println(dateOrariPrenotazioni.get(i).getDataPrenotazione());
        }
    }

    //Salvo l'immagine profilo dell'utente nel DB
    public static void salvaImmagineProfiloUtente(int idUtente, File immagine) throws SQLException, IOException {
        String salvaImmagine = "UPDATE Cliente SET ImmagineProfilo=? where IdCliente=?";

        //Leggo l'immagine e la converto come un array di byte ,questo array rappresenta l'immagine come un array di byte
        //Questo perchè il database non "riconosce" i file, ma può salvare array di byte nei campi "Blob"
        byte[] imageBytes = Files.readAllBytes(Paths.get(immagine.getAbsolutePath()));
        //Files.readAllByte Apre un input Stream legge tutto il contenuto del file byte per byte in RAM e lo chiude automaticamente.
        //Paths.get trasforma la String con il percorso del file in un oggetto path, un oggetto path rappresenta un percorso nel filesystem.
        //Quindi, dato che la funzione richiede un oggetto di tipo Path, e con .getAbsolutePath otteniamo il percorso sotto forma di String
        //dobbiamo convertire la stringa ottenuta in un Path.

        try (Connection conn = ConnessioneDatabase.getConnection()) {
            try (PreparedStatement immagineProfilo = conn.prepareStatement(salvaImmagine)) {
                //Passo il contenuto dell'immagine,è uno stream binario
                immagineProfilo.setBytes(1, imageBytes);
                immagineProfilo.setInt(2, idUtente);
                immagineProfilo.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //Carico l'immagine profilo dell'utente dal db
    public static Image caricaImmagineProfiloUtente(int idUtente) throws SQLException {
        String caricaImmagine="Select ImmagineProfilo from Cliente where IdCliente=?";
        try(Connection conn=ConnessioneDatabase.getConnection()){
            PreparedStatement immagineProfilo=conn.prepareStatement(caricaImmagine);
            immagineProfilo.setInt(1,idUtente);
            ResultSet risultato=immagineProfilo.executeQuery();

            if(risultato.next()){
                //Usiamo un InputStream, rappresenta uno stream di byte in ingresso
                //un "tubo" in cui scorrono byte da una fonte esterna.
                InputStream stream=risultato.getBinaryStream("ImmagineProfilo");
                // .getBinaryStream restituisce un InputStream
                if(stream!=null){
                    return new Image(stream);
                }
            }
        }catch (SQLException e){
            throw new RuntimeException(e);

        }
        return null;
    }

    public static void salvaCertificatoMeidico(int idUtente,File certificato) throws SQLException, IOException {
        String caricoCertificato="INSERT INTO Certificato (IdCliente,Stato,ImgCertificato,DataCaricamentoCertificato) VALUES (?,?,?,?)";
        byte[] certificatoBytes = Files.readAllBytes(Paths.get(certificato.getAbsolutePath()));
        try(Connection conn=ConnessioneDatabase.getConnection()){
            try(PreparedStatement datiCertificato=conn.prepareStatement(caricoCertificato)){
                datiCertificato.setInt(1, idUtente);
                datiCertificato.setString(2,"Attesa");
                datiCertificato.setBytes(3,certificatoBytes);
                datiCertificato.setString(4, LocalDate.now().toString());
                datiCertificato.executeUpdate();

                if(datiCertificato.getUpdateCount()>0){
                    System.out.println("Caricato certificato medico");
                }
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public static void aggiungiEsercizioConMassimale(String esercizio) {
        if (!eserciziConMassimale.contains(esercizio)) {
            eserciziConMassimale.add(esercizio);
        }
    }

    //Elimina utente
    public static boolean onClickEliminaUtente(int id) throws SQLException {
        String eliminaUtente="DELETE FROM Cliente WHERE IdCliente=?";
        try(Connection conn=ConnessioneDatabase.getConnection()){
            try(PreparedStatement datiEliminaUtente=conn.prepareStatement(eliminaUtente)){
                datiEliminaUtente.setInt(1,id);

                Alert conferma=new Alert(Alert.AlertType.CONFIRMATION);
                conferma.setTitle("Conferma eliminazione");
                conferma.setHeaderText("Sei sicuro di voler eliminare questo cliente?");
                conferma.setContentText("Questa azione è irreversibile!");
                Optional<ButtonType> resultat = conferma.showAndWait();
                    if (resultat.isPresent() && resultat.get() == ButtonType.OK) {
                        // Se l'utente ha confermato l'eliminazione, eseguiamo la query
                        int righeModificate = datiEliminaUtente.executeUpdate();
                        if(righeModificate>0) {
                            System.out.println("Utente eliminato con successo");
                            DatiSessioneCliente.logout();
                            Model.getInstance().getViewFactoryClient().showLoginWindow();
                            return true;
                        }
                        else{
                            System.out.println("Nessun utente eliminato");
                            return false;
                        }

                }

            }
        }
        return false;
    }
}