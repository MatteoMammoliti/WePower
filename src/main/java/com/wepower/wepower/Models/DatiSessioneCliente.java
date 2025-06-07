package com.wepower.wepower.Models;

import com.wepower.wepower.ControlloTemi;
import com.wepower.wepower.Models.DatiPalestra.DatiSessionePalestra;
import com.wepower.wepower.Models.DatiPalestra.PrenotazioneSalaPesi;
import com.wepower.wepower.Views.AlertHelper;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Pair;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
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
    private static boolean schedaRichiesta = false;
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
    public static int getIdUtente() {return idUtente;}
    public static String getEmail() {return email;}
    public static String getNomeUtente() {return nome;}
    public static String getAltezza() {return altezza;}
    public static Integer getPesoAttuale() {return pesoAttuale;}
    public  static String getTelefono() {return telefono;}
    public static int getCertificato() {return certificato;}
    public static String getCognome() { return cognome; }
    public static boolean getStatoAbbonamento(){return statoAbbonamento;}
    public static int getIdSchedaAllenamento() { return idSchedaAllenamento; }
    public static boolean getSeSchedaRichiesta() { return schedaRichiesta; }

    public static String getOrarioPrenotazione(String data){
        for (PrenotazioneSalaPesi prenotazioneSalaPesi : dateOrariPrenotazioni) {
            if (prenotazioneSalaPesi.getDataPrenotazione().equals(data)) {
                return prenotazioneSalaPesi.getOrarioPrenotazione();
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

    //CONTROLLO TIPO DI ABBONAMENTO ATTIVO DEL CLIENTE
    public static String getTipoAbbonamentoAttivo(){
        String abbonamentoAttivo;
        String nomeTipoAbbonamento="SELECT t.NomeAbbonamento from TipoAbbonamento t join AbbonamentoCliente a on t.IdTipoAbbonamento=a.IdTipoAbbonamento where a.IdCliente=?";

        PreparedStatement nomeAbbonamento = null;
        ResultSet risultato = null;
        try {
            Connection conn = ConnessioneDatabase.getConnection();
            nomeAbbonamento=conn.prepareStatement(nomeTipoAbbonamento);
            nomeAbbonamento.setInt(1,idUtente);
            risultato=nomeAbbonamento.executeQuery();
            if(risultato.next()){
                abbonamentoAttivo=risultato.getString("NomeAbbonamento");
                return abbonamentoAttivo;
            }
        } catch (SQLException e) {
            AlertHelper.showAlert("Questo non doveva succedere", "Qualcosa è andato storto nel reperimento del tipo di abbonamento", null, Alert.AlertType.ERROR);
        } finally {
            if (nomeAbbonamento != null) {
                try { nomeAbbonamento.close(); } catch (SQLException ignored) {}
            }
            if(risultato != null) {
                try { risultato.close(); } catch (SQLException ignored) {}
            }
        }
        return null;
    }

    //RECUPERO DATA INIZIO ABBONAMENTO ATTIVO DEL CLIENTE
    public static String getDataInizioAbbonamentoAttivo(){
        String dataInizioAbbonamento="SELECT ac.DataInizioAbbonamento from AbbonamentoCliente ac where ac.IdCliente=? and ac.StatoAbbonamento=1";

        PreparedStatement dataInizio = null;
        ResultSet risultato = null;
        try {
            Connection conn = ConnessioneDatabase.getConnection();
            dataInizio=conn.prepareStatement(dataInizioAbbonamento);
            dataInizio.setInt(1,idUtente);
            risultato=dataInizio.executeQuery();
            if(risultato.next()) return risultato.getString("DataInizioAbbonamento");
        } catch (SQLException e) {
            AlertHelper.showAlert("Questo non doveva succedere", "Qualcosa è andato storto nel reperimento della data inizio dell'abbonamento", null, Alert.AlertType.ERROR);
        } finally {
            if (dataInizio != null) {
                try { dataInizio.close(); } catch (SQLException ignored) {}
            }
            if(risultato != null) {
                try { risultato.close(); } catch (SQLException ignored) {}
            }
        }
        return null;
    }

    //RECUPERO DATA FINE ABBONAMENTO ATTIVO DEL CLIENTE
    public static String getDataFineAbbonamentoAttivo() throws SQLException {
        String dataFineAbbonamentoAttivo="SELECT ac.DataFineAbbonamento from AbbonamentoCliente ac where ac.IdCliente= ? and ac.StatoAbbonamento=1";

        PreparedStatement dataFine = null;
        ResultSet risultato = null;
        try {
            Connection conn = ConnessioneDatabase.getConnection();
            dataFine=conn.prepareStatement(dataFineAbbonamentoAttivo);
            dataFine.setInt(1, idUtente);
            risultato=dataFine.executeQuery();
            if (risultato.next()) return risultato.getString("DataFineAbbonamento");

        }catch (SQLException e){
            AlertHelper.showAlert("Questo non doveva succedere", "Qualcosa è andato storto nel reperimento della data fine dell'abbonamento", null, Alert.AlertType.ERROR);
        } finally {
            if (dataFine != null) {
                try { dataFine.close(); } catch (SQLException ignored) {}
            }
            if(risultato != null) {
                try { risultato.close(); } catch (SQLException ignored) {}
            }
        }
        return null;
    }

    //Prelevo le date degli allenamenti già conclusi
    public static ArrayList<PrenotazioneSalaPesi> getDateAllenamentiEffettuati(){
        ArrayList<PrenotazioneSalaPesi> prenotazioni=new ArrayList<>();
        String cerco="SELECT DataPrenotazione,OrarioPrenotazione FROM PrenotazioneSalaPesi WHERE IdCliente=? AND DataPrenotazione < ?  ORDER BY DataPrenotazione DESC";

        PreparedStatement prelevamento = null;
        ResultSet rs = null;
        try {
            Connection conn = ConnessioneDatabase.getConnection();
            prelevamento=conn.prepareStatement(cerco);
            prelevamento.setInt(1,DatiSessioneCliente.getIdUtente());
            prelevamento.setString(2,LocalDate.now().toString());;
            rs=prelevamento.executeQuery();
            while(rs.next()){
                PrenotazioneSalaPesi p=new PrenotazioneSalaPesi(rs.getString("DataPrenotazione"),rs.getString("OrarioPrenotazione"));
                prenotazioni.add(p);
            }
        } catch (SQLException e) {
            AlertHelper.showAlert("Questo non doveva succedere", "Qualcosa è andato storto nel reperimento degli allenamenti effettuati", null, Alert.AlertType.ERROR);
        } finally {
            if (prelevamento != null) {
                try { prelevamento.close(); } catch (SQLException ignored) {}
            }
            if(rs != null) {
                try { rs.close(); } catch (SQLException ignored) {}
            }
        }
        return prenotazioni;
    }

    //Prelevo le date degli allenamenti ancora da effettuare
    public static ArrayList<PrenotazioneSalaPesi> getDateAllenamentiDaFare() {
        ArrayList<PrenotazioneSalaPesi> date=new ArrayList<>();
        String cerco="SELECT DataPrenotazione,OrarioPrenotazione FROM PrenotazioneSalaPesi WHERE IdCliente=? AND DataPrenotazione >= ? ORDER BY DataPrenotazione ASC LIMIT 8";

        PreparedStatement prelevamento = null;
        ResultSet rs = null;
        try {
            Connection conn = ConnessioneDatabase.getConnection();
            prelevamento=conn.prepareStatement(cerco);
            prelevamento.setInt(1,DatiSessioneCliente.getIdUtente());
            prelevamento.setString(2,LocalDate.now().toString());
            rs=prelevamento.executeQuery();
            while(rs.next()){
                PrenotazioneSalaPesi nuova=new PrenotazioneSalaPesi(rs.getString("DataPrenotazione"),rs.getString("OrarioPrenotazione"));
                date.add(nuova);
            }
            return date;
        } catch (SQLException e) {
            AlertHelper.showAlert("Questo non doveva succedere", "Qualcosa è andato storto nel reperimento degli allenamenti da fare", null, Alert.AlertType.ERROR);
        } finally {
            if (prelevamento != null) {
                try { prelevamento.close(); } catch (SQLException ignored) {}
            }
            if(rs != null) {
                try { rs.close(); } catch (SQLException ignored) {}
            }
        }
        return date;
    }

    // SETTER
    public static void setStatoAbbonamento(boolean abbonamento){statoAbbonamento = abbonamento;}
    public static void setDataNascita(String data){dataNascita=data;}
    public static void setNomeUtente(String n) {nome=n;}
    public static void setIdUtente(int id) {idUtente = id;}
    public static void setEmail(String e_mail){email = e_mail;}
    public static void setAltezza(String a) {altezza = a;}
    public static void setPesoAttuale(Integer p) {pesoAttuale = p;}
    public static void setCertificato(int valore) {certificato = valore;}
    public static void setCognome(String c) { cognome = c; }
    public static void setTelefono(String t) {telefono = t; }
    public static void setIdSchedaAllenamento(int id) { idSchedaAllenamento = id; }
    public static void setSeSchedaRichiesta(boolean b) { schedaRichiesta = b; }
    public static void setImmagineProfilo(Image immagine) { immagineProfilo = immagine; }

    public static void setDateOrariPrenotazioni(ArrayList<PrenotazioneSalaPesi> d) {
        dateOrariPrenotazioni = d;

        datePrenotazioniSalaPesi.clear();
        for (PrenotazioneSalaPesi prenotazioneSalaPesi : dateOrariPrenotazioni) {
            datePrenotazioniSalaPesi.add(prenotazioneSalaPesi.getDataPrenotazione());
        }
    }

    public static void setGenere(String s) {genere = s;}
    public static void setAlertScadenzaAbbonamento(boolean alert) { alertScadenzaAbbonamento = alert; }
    public static void setAlertCertificatoMancante(boolean alert) { alertCertificatoMancante = alert; }

    // LOGOUT
    public static void logout() {
        ConnessioneDatabase.closeConnection();
        idUtente = 0;
        email = null;
        nome = null;
        certificato = 0;
        telefono = null;
        dateOrariPrenotazioni.clear();
        datePrenotazioniSalaPesi.clear();
        statoAbbonamento = false;
        idSchedaAllenamento = 0;
        schedaRichiesta = false;
        altezza = null;
        pesoAttuale = null;
        genere=null;
        immagineProfilo = null;
        eserciziConMassimale.clear();
        DatiSessionePalestra.svuotaPrenotazioniSalaPesi();
        alertScadenzaAbbonamento = false;
        alertCertificatoMancante = false;
        Model.invalidate();
    }

    // CONTROLLO DATA PRENOTAZIONE SALA PESI
    public static boolean controlloDataPrenotazioneSalaPesi(LocalDate data) {
        String dataControllo = data.toString();

        return datePrenotazioniSalaPesi.contains(dataControllo);
    }

    // CONTROLLO DATA E ORARIO PRENOTAZIONE SALA PESI PER STORICO
    public static boolean controlloDataPrenotazioneSalaPesi(LocalDate data,String ora) {
        String dataControllo = data.toString();
        PrenotazioneSalaPesi temp=new PrenotazioneSalaPesi(dataControllo,ora);

        return dateOrariPrenotazioni.contains(temp);
    }

    //CONTROLLO SE L'ABBONAMENTO DEL CLIENTE è ATTIVO
    public static boolean isAbbonamentoAttivo() throws SQLException {
        String dataInizioAbbonamento = getDataInizioAbbonamentoAttivo();
        String dataFineAbbonamento = getDataFineAbbonamentoAttivo();

        if (dataInizioAbbonamento != null && dataFineAbbonamento != null) {
            LocalDate dataInizio = LocalDate.parse(dataInizioAbbonamento);
            LocalDate dataFine = LocalDate.parse(dataFineAbbonamento);
            LocalDate oggi = LocalDate.now();

            return (oggi.isAfter(dataInizio) || oggi.equals(dataInizio)) && oggi.isBefore(dataFine);
        }
        return false;
    }

    //AGGIUNGI UNA PRENOTAZIONE SALA PESI
    public static void aggiungiPrenotazione(PrenotazioneSalaPesi p) {
        dateOrariPrenotazioni.add(p);
        datePrenotazioniSalaPesi.add(p.getDataPrenotazione());
    }

    //RIMUOVI UNA PRENOTAZIONE SALA PESI
    public static void rimuoviPrenotazione(PrenotazioneSalaPesi p) {
        dateOrariPrenotazioni.remove(p);
        datePrenotazioniSalaPesi.remove(p.getDataPrenotazione());
    }

    //SALVO L'IMMAGINE DEL CLIENTE NEL DB
    public static void salvaImmagineProfiloUtente(int idUtente, File immagine) throws SQLException, IOException {
        String salvaImmagine = "UPDATE Cliente SET ImmagineProfilo=? where IdCliente=?";

        //Leggo l'immagine e la converto come un array di byte ,questo array rappresenta l'immagine come un array di byte
        //Questo perchè il database non "riconosce" i file, ma può salvare array di byte nei campi "Blob"
        byte[] imageBytes = Files.readAllBytes(Paths.get(immagine.getAbsolutePath()));
        //Files.readAllByte Apre un input Stream legge tutto il contenuto del file byte per byte in RAM e lo chiude automaticamente.
        //Paths.get trasforma la String con il percorso del file in un oggetto path, un oggetto path rappresenta un percorso nel filesystem.
        //Quindi, dato che la funzione richiede un oggetto di tipo Path, e con .getAbsolutePath otteniamo il percorso sotto forma di String
        //dobbiamo convertire la stringa ottenuta in un Path.

        PreparedStatement immagineProfilo = null;
        try {
            Connection conn = ConnessioneDatabase.getConnection();
            immagineProfilo = conn.prepareStatement(salvaImmagine);
            //Passo il contenuto dell'immagine,è uno stream binario
            immagineProfilo.setBytes(1, imageBytes);
            immagineProfilo.setInt(2, idUtente);
            immagineProfilo.executeUpdate();
        } catch (SQLException e) {
            AlertHelper.showAlert("Questo non doveva succedere", "Qualcosa è andato storto nel salvataggio dell'immagine profilo", null, Alert.AlertType.ERROR);
        } finally {
            if (immagineProfilo != null) {
                try { immagineProfilo.close(); } catch (SQLException ignored) {}
            }
        }
    }

    //CARICO L'IMMAGINE DEL CLIENTE DAL DB
    public static Image caricaImmagineProfiloUtente(int idUtente) throws SQLException {
        String caricaImmagine="Select ImmagineProfilo from Cliente where IdCliente=?";

        PreparedStatement immagineProfilo = null;
        ResultSet risultato = null;
        InputStream stream = null;
        try {
            Connection conn = ConnessioneDatabase.getConnection();
            immagineProfilo=conn.prepareStatement(caricaImmagine);
            immagineProfilo.setInt(1,idUtente);
            risultato=immagineProfilo.executeQuery();

            if(risultato.next()){
                //Usiamo un InputStream, rappresenta uno stream di byte in ingresso
                //un "tubo" in cui scorrono byte da una fonte esterna.
                stream=risultato.getBinaryStream("ImmagineProfilo");
                // .getBinaryStream restituisce un InputStream
                if(stream!=null){
                    return new Image(stream);
                }
            }
        }catch (SQLException e){
            AlertHelper.showAlert("Questo non doveva succedere", "Qualcosa è andato storto nel caricamento dell'immagine profilo", null, Alert.AlertType.ERROR);
        } finally {
            if (risultato != null) {
                try { risultato.close(); } catch (SQLException ignored) {}
            }
            if(immagineProfilo != null) {
                try { immagineProfilo.close(); } catch (SQLException ignored) {}
            }
            if (stream != null){
                try {stream.close();} catch (IOException ignored) {}
            }
        }
        return null;
    }

    //SALVO CERTIFICATO MEDICO DEL CLIENTE NEL DB
    public static boolean salvaCertificatoMeidico(int idUtente,File certificato)throws IOException{

        if(certificato==null) return false;

        String caricoCertificato="INSERT INTO Certificato (IdCliente,Stato,ImgCertificato,DataCaricamentoCertificato) VALUES (?,?,?,?)";
        String aggiornoCliente="UPDATE Cliente SET CertificatoValido=1 WHERE IdCliente=?";
        byte[] certificatoBytes = Files.readAllBytes(Paths.get(certificato.getAbsolutePath()));

        if(certificatoBytes.length==0) return false;

        PreparedStatement datiCertificato = null;
        try {
            Connection conn = ConnessioneDatabase.getConnection();
            conn.setAutoCommit(false);
            datiCertificato=conn.prepareStatement(caricoCertificato);
            datiCertificato.setInt(1, idUtente);
            datiCertificato.setString(2,"Attesa");
            datiCertificato.setBytes(3,certificatoBytes);
            datiCertificato.setString(4, LocalDate.now().toString());

            if(datiCertificato.executeUpdate()<=0){
                conn.rollback();
                return false;
            }

            PreparedStatement datiCliente = null;
            try {
                datiCliente=conn.prepareStatement(aggiornoCliente);
                datiCliente.setInt(1, idUtente);
                int risultato=datiCliente.executeUpdate();

                if (risultato<=0) {
                    conn.rollback();
                    return false;
                }
                conn.commit();
                return true;
            }
            catch (SQLException e){
                conn.rollback();
                AlertHelper.showAlert("Questo non doveva succedere", "Qualcosa è andato storto nel salvataggio del certificato", null, Alert.AlertType.ERROR);
            } finally {
                if (datiCliente != null) {
                    try { datiCliente.close(); } catch (SQLException ignored) {}
                }
            }

        }catch (SQLException e){
            AlertHelper.showAlert("Questo non doveva succedere", "Qualcosa è andato storto nel salvataggio del certificato", null, Alert.AlertType.ERROR);
        } finally {
            if (datiCertificato != null) {
                try { datiCertificato.close(); } catch (SQLException ignored) {}
            }
        }
        return false;
    }

    //AGGIUNGO MASSIMALE DI UN ESERCIZIO
    public static void aggiungiEsercizioConMassimale(String esercizio) {
        if (!eserciziConMassimale.contains(esercizio)) {
            eserciziConMassimale.add(esercizio);
        }
    }

    //ELIMINA UTENTE
    public static boolean onClickEliminaUtente(int id) throws SQLException {
        String eliminaUtente="DELETE FROM Cliente WHERE IdCliente=?";

        PreparedStatement datiEliminaUtente = null;
        try {
            Connection conn = ConnessioneDatabase.getConnection();
            datiEliminaUtente=conn.prepareStatement(eliminaUtente);
            datiEliminaUtente.setInt(1,id);

            Alert conferma=new Alert(Alert.AlertType.CONFIRMATION);
            conferma.setTitle("Conferma eliminazione");
            conferma.setHeaderText("Sei sicuro di voler eliminare questo cliente?");
            conferma.setContentText("Questa azione è irreversibile!");
            ImageView icon = new ImageView(new Image(DatiSessioneCliente.class.getResourceAsStream("/Images/IconeAlert/question.png")));
            DialogPane dialogPane = conferma.getDialogPane();
            dialogPane.getStylesheets().add(DatiSessioneCliente.class.getResource("/Styles/alertStyle.css").toExternalForm());
            dialogPane.getStylesheets().add(ControlloTemi.getInstance().getCssTemaCorrente());
            conferma.setGraphic(icon);

            Optional<ButtonType> resultat = conferma.showAndWait();
            if (resultat.isPresent() && resultat.get() == ButtonType.OK) {
                // Se l'utente ha confermato l'eliminazione, eseguiamo la query
                int righeModificate = datiEliminaUtente.executeUpdate();

                if(righeModificate>0) {
                    DatiSessioneCliente.logout();
                    Model.getInstance().getViewFactoryClient().showLoginWindow();
                    return true;
                }
                else return false;
            }
        } catch (Exception e) {
            AlertHelper.showAlert("Questo non doveva succedere", "Qualcosa è andato storto nell'eliminazione dell'utente", null, Alert.AlertType.ERROR);
        } finally {
            if (datiEliminaUtente != null) {
                try { datiEliminaUtente.close(); } catch (SQLException ignored) {}
            }
        }
        return false;
    }

    //PRELEVO DAL TADABASE L'ULTIMO AGGIORNAMENTO PESO INSERITO DALL'UTENTE
    public static void caricaPesoAttuale(int IdUtente) {
        String caricaPeso="SELECT Peso FROM PesoCliente WHERE IdCliente=? ORDER BY DataRegistrazionePeso DESC LIMIT 1";

        PreparedStatement datiCaricaPeso = null;
        ResultSet risultato = null;
        try {
            Connection conn = ConnessioneDatabase.getConnection();
            datiCaricaPeso=conn.prepareStatement(caricaPeso);
            datiCaricaPeso.setInt(1,IdUtente);
            risultato=datiCaricaPeso.executeQuery();
            if(risultato.next()){
                pesoAttuale=risultato.getInt("Peso");
            }
        } catch (SQLException e) {
            AlertHelper.showAlert("Questo non doveva succedere", "Qualcosa è andato storto nel caricamento del peso", null, Alert.AlertType.ERROR);
        } finally {
            if (datiCaricaPeso != null) {
                try { datiCaricaPeso.close(); } catch (SQLException ignored) {}
            }
            if(risultato != null) {
                try { risultato.close(); } catch (SQLException ignored) {}
            }
        }
    }

    //PRELEVO DAL DATABASE STORICO PESI CON DATE
    public static ArrayList<Pair<String,Integer>> caricaStroicoPesi(int IdUtente){
        ArrayList<Pair<String,Integer>> storicoPesi=new ArrayList<>();
        String caricaStoricoPesi="SELECT DataRegistrazionePeso,Peso FROM PesoCliente WHERE IdCliente=? ORDER BY DataRegistrazionePeso DESC LIMIT 10";

        PreparedStatement datiCaricaStorico = null;
        ResultSet risultato = null;
        try {
            Connection conn = ConnessioneDatabase.getConnection();
            datiCaricaStorico=conn.prepareStatement(caricaStoricoPesi);
            datiCaricaStorico.setInt(1,IdUtente);
            risultato=datiCaricaStorico.executeQuery();

            while(risultato.next()){
                Pair<String,Integer> peso=new Pair<>(risultato.getString("DataRegistrazionePeso"),risultato.getInt("Peso"));
                storicoPesi.addFirst(peso);
            }
            return storicoPesi;
        } catch (SQLException e) {
            AlertHelper.showAlert("Questo non doveva succedere", "Qualcosa è andato storto nel caricamento dello storico dei pesi", null, Alert.AlertType.ERROR);
        } finally {
            if (datiCaricaStorico != null) {
                try { datiCaricaStorico.close(); } catch (SQLException ignored) {}
            }
            if(risultato != null) {
                try { risultato.close(); } catch (SQLException ignored) {}
            }
        }
        return storicoPesi;
    }

    // PRELEVO DAL DATABASE STORICO DEI MASSIMALI CON DATE PER CIASCUN ESERCIZIO
    public static ArrayList<Pair<String,Number>> caricaStoricoMassimalePerEsercizio(String esercizio){

        String prelevamento = "SELECT Peso, DataInserimento FROM MassimaleImpostatoCliente WHERE IDCliente = ? AND NomeEsercizio = ? ORDER BY DataInserimento DESC LIMIT 10";
        ArrayList<Pair<String,Number>> lista = new ArrayList<>();

        PreparedStatement prelievo = null;
        ResultSet rs = null;
        try {
            Connection conn = ConnessioneDatabase.getConnection();
            prelievo = conn.prepareStatement(prelevamento);
            prelievo.setInt(1, DatiSessioneCliente.getIdUtente());
            prelievo.setString(2, esercizio);
            rs = prelievo.executeQuery();

            while(rs.next()) {

                String dataInserimento = rs.getString("DataInserimento");
                LocalDate ld =  LocalDate.parse(dataInserimento);
                Date dataUtil = Date.from(ld.atStartOfDay(ZoneId.systemDefault()).toInstant());
                String dataFormattata = new SimpleDateFormat("dd/MM/yyyy").format(dataUtil);

                double peso = rs.getDouble("Peso");

                Pair<String,Number> pair = new Pair<>(dataFormattata,peso);
                lista.addFirst(pair);
            }
            return lista;

        } catch (SQLException e) {
            AlertHelper.showAlert("Questo non doveva succedere", "Qualcosa è andato storto nel caricamento degli esercizi con massimali", null, Alert.AlertType.ERROR);
        } finally {
            if (prelievo != null) {
                try { prelievo.close(); } catch (SQLException ignored) {}
            }
            if(rs != null) {
                try { rs.close(); } catch (SQLException ignored) {}
            }
        }
        return lista;
    }

    // PRELIEVO STORICO PRENOTAZIONI PER GRAFICO
    public static ArrayList<Pair<String, Number>> caricaStoricoPrenotazioni(int IdUtente) {

        String prelevaPrenotazioni = "SELECT SUBSTR(DataPrenotazione, 1, 7) AS Mese, COUNT(*) AS numeroPrenotazioni FROM PrenotazioneSalaPesi WHERE IdCliente = ? GROUP BY Mese ORDER BY Mese";
        ArrayList<Pair<String,Number>> lista = new ArrayList<>();

        PreparedStatement prelevamento = null;
        ResultSet rs = null;
        try {
            Connection conn = ConnessioneDatabase.getConnection();
            prelevamento = conn.prepareStatement(prelevaPrenotazioni);
            prelevamento.setInt(1, IdUtente);
            rs = prelevamento.executeQuery();

            while(rs.next()) {
                String mese = rs.getString("Mese");
                int cont = rs.getInt("numeroPrenotazioni");

                Pair<String, Number> p = new Pair<>(mese, cont);
                lista.add(p);
            }
            return lista;
        } catch (SQLException e) {
            AlertHelper.showAlert("Questo non doveva succedere", "Qualcosa è andato storto nel caricamento dello storico delle prenotazioni", null, Alert.AlertType.ERROR);
        } finally {
            if (prelevamento != null) {
                try { prelevamento.close(); } catch (SQLException ignored) {}
            }
            if(rs != null) {
                try { rs.close(); } catch (SQLException ignored) {}
            }
        }
        return lista;
    }

    // PRELIEVO DATA DI SCADENZA DELL'ABBONAMENTO
    public static String caricaDataScadenzaAbbonamento(int IdUtente) {
        String fetchDataScadenza = "SELECT DataFineAbbonamento FROM AbbonamentoCliente WHERE IdCliente = ? AND StatoAbbonamento = 1";

        PreparedStatement prelevamento = null;
        ResultSet rs = null;
        try {
            Connection conn = ConnessioneDatabase.getConnection();
            prelevamento = conn.prepareStatement(fetchDataScadenza);
            prelevamento.setInt(1, IdUtente);
            rs = prelevamento.executeQuery();

            if (rs.next()) return rs.getString("DataFineAbbonamento");
        } catch (SQLException e) {
            AlertHelper.showAlert("Questo non doveva succedere", "Qualcosa è andato storto nel caricamento della data di scadenza abbonamento", null, Alert.AlertType.ERROR);
        } finally {
            if (prelevamento != null) {
                try { prelevamento.close(); } catch (SQLException ignored) {}
            }
            if(rs != null) {
                try { rs.close(); } catch (SQLException ignored) {}
            }
        }
        return null;
    }

    // PRELIEVO DATA DI SCADENZA DELL'ABBONAMENTO
    public static int caricaPresenzaCertificato(int IdUtente) {
        String fetchCertificato = "SELECT IdCertificato FROM Certificato WHERE IdCliente = ?";

        PreparedStatement prelevamento = null;
        ResultSet rs = null;
        try {
            Connection conn = ConnessioneDatabase.getConnection();
            prelevamento = conn.prepareStatement(fetchCertificato);
            prelevamento.setInt(1, IdUtente);
            rs = prelevamento.executeQuery();

            if (rs.next()) return rs.getInt("IdCertificato");

        } catch (SQLException e) {
            AlertHelper.showAlert("Questo non doveva succedere", "Qualcosa è andato storto nel caricamento del certificato", null, Alert.AlertType.ERROR);
        } finally {
            if (prelevamento != null) {
                try { prelevamento.close(); } catch (SQLException ignored) {}
            }
            if(rs != null) {
                try { rs.close(); } catch (SQLException ignored) {}
            }
        }
        return 0;
    }
}