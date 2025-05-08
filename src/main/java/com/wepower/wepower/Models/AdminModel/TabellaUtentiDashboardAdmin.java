package com.wepower.wepower.Models.AdminModel;

import com.wepower.wepower.Models.ConnessioneDatabase;
import com.wepower.wepower.Views.AdminView.RigaDashboardAdmin;
import com.wepower.wepower.Views.AlertHelper;
import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TabellaUtentiDashboardAdmin {

    //Prelevo dal database tutti gli utenti per riempire la tabella utenti
    public static ArrayList<RigaDashboardAdmin> riempiRiga() throws SQLException {
        Connection conn = ConnessioneDatabase.getConnection();

        ArrayList<RigaDashboardAdmin> ris = new ArrayList<RigaDashboardAdmin>();

        //Prelevo i dati dal database
        String query = """
                SELECT
                c.IdCliente,
                c.Nome,
                c.Cognome,
                c.Sesso,
                c.DataNascita,
                a.StatoAbbonamento,
                c.CertificatoValido,
                a.DataInizioAbbonamento,
                a.DataFineAbbonamento,
                t.NomeAbbonamento,
                cc.Email
                FROM Cliente c LEFT JOIN AbbonamentoCliente a
                ON c.IdCliente = a.IdCliente AND a.StatoAbbonamento=1
                LEFT JOIN TipoAbbonamento t ON a.IdTipoAbbonamento = t.IdTipoAbbonamento
                LEFT JOIN CredenzialiCliente cc ON c.IdCliente = cc.IdCliente""";

        PreparedStatement datiClienti = null;
        ResultSet risultatoTuttiClienti = null;
        try {
            datiClienti = conn.prepareStatement(query);
            risultatoTuttiClienti = datiClienti.executeQuery();

            //Per ogni utente creo un oggetto di tipo riga
            while (risultatoTuttiClienti.next()) {
                int IdCliente = risultatoTuttiClienti.getInt("IdCliente");
                String Nome = risultatoTuttiClienti.getString("Nome");
                String Cognome = risultatoTuttiClienti.getString("Cognome");
                String DataNascita = risultatoTuttiClienti.getString("DataNascita");
                int CertificatoValido = risultatoTuttiClienti.getInt("CertificatoValido");
                int StatoAbbonamento = risultatoTuttiClienti.getInt("StatoAbbonamento");
                String DataInizioAbbonamento = ("Scaduto");
                String DataFineAbbonamento = ("Scaduto");

                if (!(risultatoTuttiClienti.getString("DataInizioAbbonamento")==null)){
                   DataInizioAbbonamento = risultatoTuttiClienti.getString("DataInizioAbbonamento");
                   DataFineAbbonamento = risultatoTuttiClienti.getString("DataFineAbbonamento");
                }

                String email = risultatoTuttiClienti.getString("Email");
                String sesso = risultatoTuttiClienti.getString("Sesso");
                RigaDashboardAdmin A = new RigaDashboardAdmin(IdCliente, Nome, Cognome,DataNascita, StatoAbbonamento, CertificatoValido, DataInizioAbbonamento, DataFineAbbonamento, email, sesso);
                //Aggiungo la riga a ris per poi creare la tabella utenti
                ris.add(A);
            }
        } catch (Exception e) {
            AlertHelper.showAlert("Questo non doveva succedere", "Qualcosa è andato storto durante il caricamento degli utenti", null, Alert.AlertType.ERROR);
        } finally {
            if (risultatoTuttiClienti != null) {
                try { risultatoTuttiClienti.close(); } catch (SQLException ignored) {}
            }
            if (datiClienti != null) {
                try { datiClienti.close(); } catch (SQLException ignored) {}
            }
        }
        return ris;
    }

    //Rimuovo l'utente dal database e di conseguenza dalla tabella utenti
    public static boolean eliminaRiga(int id) throws SQLException {
        Connection conn = ConnessioneDatabase.getConnection();

        String eliminaUtente = "DELETE FROM Cliente WHERE IdCliente = ?";
        int risultatoEliminazione;

        PreparedStatement elimina = null;
        try {
            elimina = conn.prepareStatement(eliminaUtente);
            elimina.setInt(1, id);
            risultatoEliminazione = elimina.executeUpdate();
            return risultatoEliminazione > 0;
        } catch (Exception e) {
            AlertHelper.showAlert("Questo non doveva succedere", "Qualcosa è andato storto nell'eliminazione", null, Alert.AlertType.ERROR);
        } finally {
            if (elimina != null) {
                try { elimina.close(); } catch (SQLException ignored) {}
            }
        }
        return false;
    }


    //Aggiorno i dati dell'utente nel database
    public static boolean salvaModifiche (int id, String nome, String cognome,String dataNascita, String dataRinnovo, String dataScadenza,int statoAbbonamento, int idTipoAbbonamento) throws SQLException {
        Connection conn = ConnessioneDatabase.getConnection();

        try {
            //Disabilito l'auto commit
            conn.setAutoCommit(false);

            //Preparo la query
            String queryUpdateCliente = "UPDATE Cliente SET Nome = ?, Cognome=?, DataNascita=? WHERE IdCliente = ?";
            //Aggiorno i dati

            try (PreparedStatement psCliente = conn.prepareStatement(queryUpdateCliente)) {
                psCliente.setString(1, nome);
                psCliente.setString(2, cognome);
                psCliente.setString(3, dataNascita);
                psCliente.setInt(4, id);
                psCliente.executeUpdate();
            } catch (SQLException e) {
                AlertHelper.showAlert("Questo non doveva succedere", "Qualcosa è andato storto nel salvataggio delle modifiche", null, Alert.AlertType.ERROR);
            }

            //Verifico se è stato inserito un nuovo abbonamento
            if (idTipoAbbonamento !=-1) {
                //Se è stato inserito un nuovo abbonamento lo inserisco nel database
                String queryAddAbbonamento="INSERT INTO AbbonamentoCliente (IdCliente,IdTipoAbbonamento, StatoAbbonamento,DataInizioAbbonamento, DataFineAbbonamento) VALUES(?,?,?,?,?)";
                try (PreparedStatement psAddAbbonamento = conn.prepareStatement(queryAddAbbonamento)) {
                    psAddAbbonamento.setInt(1, id);
                    psAddAbbonamento.setInt(2, idTipoAbbonamento);
                    psAddAbbonamento.setInt(3, statoAbbonamento);
                    psAddAbbonamento.setString(4, dataRinnovo);
                    psAddAbbonamento.setString(5, dataScadenza);
                    psAddAbbonamento.executeUpdate();
                }
                catch (SQLException ex) {
                    //In caso di eccezioni annullo le modifiche
                    conn.rollback();
                    AlertHelper.showAlert("Questo non doveva succedere", "Qualcosa è andato storto nell'eliminazione", null, Alert.AlertType.ERROR);
                }
            }
            //Invio le modifiche
            conn.commit();
            return true;
        }
        catch (SQLException e) {
            try {
                //In caso di eccezioni annullo le modifiche
                conn.rollback();
                return false;
            }
            catch (SQLException ex) {
                AlertHelper.showAlert("Questo non doveva succedere", "Qualcosa è andato storto nell'eliminazione", null, Alert.AlertType.ERROR);
            }
        }
        return false;
    }

    //Preleva dal database tutti i tipi di abbonamenti disponibili
    public static ArrayList<String> prelevaTipiAbbonamenti(){
        //Prendo la connessione al database
        Connection conn = ConnessioneDatabase.getConnection();

        //Inizializzo una lista vuota
        ArrayList<String> tipi = new ArrayList<>();

        //Preparo la query per prelevare i tipi di abbonamenti
        String query = "SELECT NomeAbbonamento FROM TipoAbbonamento";

        //Eseguo la query
        PreparedStatement psTipi = null;
        try {
            psTipi = conn.prepareStatement(query);
            ResultSet rs = psTipi.executeQuery();

            //Salvo i vari abbonamenti in tipi
            while (rs.next()) {
                tipi.add(rs.getString("NomeAbbonamento"));
            }
        } catch (SQLException e) {
            AlertHelper.showAlert("Questo non doveva succedere", "Qualcosa è andato storto nel reperimento degli abbonamenti", null, Alert.AlertType.ERROR);
        } finally {
            if (psTipi != null) {
                try { psTipi.close(); } catch (SQLException ignored) {}
            }
        }

        //Restituisco un ArrayList contenente tutti gli abbonamenti disponibili
        return tipi;
    }

    //Trova l'id associato al nomeTipoAbbonamento
    public static int prelevaIdTipoAbbonamento(String nomeTipoAbbonamento) {
        //Prendo la connessione al database
        Connection conn = ConnessioneDatabase.getConnection();

        //Preparo la query
        String query="SELECT IdTipoAbbonamento FROM TipoAbbonamento WHERE NomeAbbonamento = ?";

        PreparedStatement psIdTipo = null;
        try {
            psIdTipo=conn.prepareStatement(query);
            psIdTipo.setString(1, nomeTipoAbbonamento);
            ResultSet rs=psIdTipo.executeQuery();

            //Restituisco l'id
            if (rs.next()) {
                return rs.getInt("IdTipoAbbonamento");
            }
        } catch (SQLException e) {
            AlertHelper.showAlert("Questo non doveva succedere", "Qualcosa è andato storto", null, Alert.AlertType.ERROR);
        } finally {
            if (psIdTipo != null) {
                try { psIdTipo.close(); } catch (SQLException ignored) {}
            }
        }

        return 0;
    }

    //Prelevo la durata dell'abbonamento in mesi
    public static int prelevaDurataTipoAbbonamento(String nomeTipoAbbonamento) {
        //Prendo la connessione al database
        Connection conn = ConnessioneDatabase.getConnection();

        //Preparo la query
        String query="SELECT Durata FROM TipoAbbonamento WHERE NomeAbbonamento = ?";
        PreparedStatement psDurata =  null;
        try {
            psDurata=conn.prepareStatement(query);
            psDurata.setString(1, nomeTipoAbbonamento);
            ResultSet rs = psDurata.executeQuery();

            //Restituisco la durata
            if (rs.next()) {
                return rs.getInt("Durata");
            }

        } catch (SQLException e) {
            AlertHelper.showAlert("Questo non doveva succedere", "Qualcosa è andato storto", null, Alert.AlertType.ERROR);
        } finally {
            if (psDurata != null) {
                try { psDurata.close(); } catch (SQLException ignored) {}
            }
        }
        return 0;
    }
}