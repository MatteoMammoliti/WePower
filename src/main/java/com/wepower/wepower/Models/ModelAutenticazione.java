package com.wepower.wepower.Models;
import com.wepower.wepower.Models.DatiPalestra.DatiSessionePalestra;
import com.wepower.wepower.Models.DatiPalestra.PrenotazioneSalaPesi;
import com.wepower.wepower.Models.DatiPalestra.PrenotazioneSalaPesiCliente;
import com.wepower.wepower.Views.AlertHelper;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ModelAutenticazione {


    public static boolean verificaCredenziali(String email, String password) {

        // query
        String loginAdmin = "SELECT * FROM Admin WHERE Email = ? AND Password = ?";
        String loginCliente ="SELECT c.IdCliente,c.CertificatoValido,c.Nome, c.Cognome,c.DataNascita, cc.Email,cc.Telefono, c.ImmagineProfilo, c.Altezza,c.Sesso FROM CredenzialiCliente cc JOIN Cliente c ON cc.idCliente=c.idCliente WHERE cc.Email = ? AND cc.Password = ?";

        // prelievo dati in base al tipo di utente
        try {
            Connection conn = ConnessioneDatabase.getConnection();
            PreparedStatement datiCliente = conn.prepareStatement(loginCliente);
            datiCliente.setString(1, email);
            datiCliente.setString(2, password);
            ResultSet risultatoClienti = datiCliente.executeQuery();

            //Prelevo i dati del cliente
                if (risultatoClienti.next()) {

                    DatiSessioneCliente.setIdUtente(risultatoClienti.getInt("IdCliente"));
                    DatiSessioneCliente.setNomeUtente(risultatoClienti.getString("Nome"));
                    DatiSessioneCliente.setCognome(risultatoClienti.getString("Cognome"));
                    DatiSessioneCliente.setEmail(risultatoClienti.getString("Email"));
                    DatiSessioneCliente.setCertificato(risultatoClienti.getInt("CertificatoValido"));
                    DatiSessioneCliente.setDataNascita(risultatoClienti.getString("DataNascita"));
                    DatiSessioneCliente.setTelefono(risultatoClienti.getString("Telefono"));
                    DatiSessioneCliente.caricaPesoAttuale(risultatoClienti.getInt("IdCliente"));
                    DatiSessioneCliente.setAltezza(risultatoClienti.getString("Altezza"));
                    DatiSessioneCliente.setGenere(risultatoClienti.getString("Sesso"));
                    DatiSessionePalestra.setNumeroMassimoPrenotazioni();

                    // set dell'immagine profilo per il menu
                    Image immagineProfilo;
                    InputStream immagine = risultatoClienti.getBinaryStream("ImmagineProfilo");
                    if (immagine != null) {
                        immagineProfilo = new Image(immagine);
                    } else {
                        immagineProfilo = new Image(ModelAutenticazione.class.getResource("/Images/defaultImgProfilo.png").toExternalForm());
                    }
                    DatiSessioneCliente.setImmagineProfilo(immagineProfilo);

                    // prelevamento di tutte le prenotazioni
                    DatiSessioneCliente.setDateOrariPrenotazioni(caricaDatePrenotazioniSalaPesi(risultatoClienti.getInt("IdCliente")));

                    // se al momento del login l'abbonamento del cliente risulta scaduto, aggiorniamo lo stato nel db
                    if (DatiSessioneCliente.isAbbonamentoAttivo()) {
                        DatiSessioneCliente.setStatoAbbonamento(true);
                    } else {
                        DatiSessioneCliente.setStatoAbbonamento(false);
                        String updateStatoAbbonamento = "UPDATE AbbonamentoCliente SET StatoAbbonamento = 0 WHERE IdCliente = ? AND StatoAbbonamento = 1";

                        try {
                            conn = ConnessioneDatabase.getConnection();
                            PreparedStatement updateStato = conn.prepareStatement(updateStatoAbbonamento);
                            updateStato.setInt(1, DatiSessioneCliente.getIdUtente());
                            updateStato.executeUpdate();
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    // prelievo dell'id scheda allenamento del cliente
                    String querySchedaAllenamento = "SELECT IdScheda, IdAdmin FROM SchedaAllenamento WHERE IdCliente = ? AND SchedaAncoraInUso = 1";

                    try {
                         conn = ConnessioneDatabase.getConnection();
                        PreparedStatement datiSchedaAllenamento = conn.prepareStatement(querySchedaAllenamento);
                        datiSchedaAllenamento.setInt(1, DatiSessioneCliente.getIdUtente());
                        ResultSet risultatoScheda = datiSchedaAllenamento.executeQuery();

                        if (risultatoScheda.next()) {
                            DatiSessioneCliente.setIdSchedaAllenamento(risultatoScheda.getInt("IdScheda"));
                            DatiSessioneCliente.setSeSchedaRichiesta(risultatoScheda.getInt("IdAdmin") == 1);
                        } else {
                            DatiSessioneCliente.setIdSchedaAllenamento(0);
                            DatiSessioneCliente.setSeSchedaRichiesta(false);
                        }

                        // riempimento della lista con gli esercizi a cui l'utente ha assegnato almeno un massimale (per il grafico)
                        riempiListaEserciziConMassimali(DatiSessioneCliente.getIdUtente());
                        return true;
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }

                try {
                     conn = ConnessioneDatabase.getConnection();
                    //Se non trovo il cliente, vado a vedere se è un admin e prelevo i suoi dati
                    PreparedStatement datiAdmin =conn.prepareStatement(loginAdmin);
                    datiAdmin.setString(1, email);
                    datiAdmin.setString(2, password);
                    ResultSet risultatoAdmin = datiAdmin.executeQuery();

                    if(risultatoAdmin.next()) {
                        DatiSessioneCliente.setNomeUtente("Admin");
                        return true;
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
        } catch (Exception e) {
            AlertHelper.showAlert("Questo non doveva succedere", " Errore durante l'autenticazione", null, Alert.AlertType.ERROR);
        }
        return false;
    }

    // preleviamo tutte le prenotazioni nelle sale pesi per il conteggio dei posti disponibili per ogni giorno e per ogni ora
    public static void prelevaDatiPalestra() throws SQLException {
        String prelevaDatiPrenotazioniSalaPesi="SELECT * FROM PrenotazioneSalaPesi";
        Connection conn = ConnessioneDatabase.getConnection();
        try {
            PreparedStatement datiPrenotazioni = conn.prepareStatement(prelevaDatiPrenotazioniSalaPesi);
            ResultSet risultato = datiPrenotazioni.executeQuery();

            while (risultato.next()) {
                PrenotazioneSalaPesiCliente prenotazione = new PrenotazioneSalaPesiCliente(risultato.getInt("IdCliente"), risultato.getString("DataPrenotazione"), risultato.getString("OrarioPrenotazione"));
                DatiSessionePalestra.aggiungiPrenotazioneSalaPesi(prenotazione);
            }

        } catch (Exception e) {
            System.out.printf(e.getMessage());
        }
    };

    // vado a prendermi dal Database tutte le date in cui il cliente ha prenotato la salapesi per mostrarle nel calendario
    public static ArrayList<PrenotazioneSalaPesi> caricaDatePrenotazioniSalaPesi(int idUtente) throws SQLException{
        ArrayList<PrenotazioneSalaPesi>  datePrenotazioni = new ArrayList<>();

        Connection conn = ConnessioneDatabase.getConnection();

        String query = "SELECT DataPrenotazione, OrarioPrenotazione FROM PrenotazioneSalaPesi WHERE idCliente = ?";
        try {
            PreparedStatement datiPrenotazione = conn.prepareStatement(query);

            datiPrenotazione.setInt(1, idUtente);
            ResultSet risultatoPrenotazioni = datiPrenotazione.executeQuery();

            while (risultatoPrenotazioni.next()) {
                String data = risultatoPrenotazioni.getString("DataPrenotazione");
                String orario = risultatoPrenotazioni.getString("OrarioPrenotazione");
                PrenotazioneSalaPesi prenotazione=new PrenotazioneSalaPesi(data,orario);
                datePrenotazioni.add(prenotazione);
            }
        } catch (SQLException e) {
            AlertHelper.showAlert("Questo non doveva succedere", "Impossibile prelevare le prenotazioni in sala pesi", null, Alert.AlertType.ERROR);
        }
        return datePrenotazioni;
    }

    // prelevo tutti gli esercizi a cui l'utente ha assegnato un massimale per mostrarli nel menu a tendina del grafico in dashboard
    public static void riempiListaEserciziConMassimali(int Id) {
        Connection conn = ConnessioneDatabase.getConnection();
        String query = "SELECT NomeEsercizio FROM MassimaleImpostatoCliente WHERE IdCliente = ?";
        try {
            PreparedStatement datiEsercizi = conn.prepareStatement(query);
            datiEsercizi.setInt(1, Id);
            ResultSet risultatoTuttiEsercizi = datiEsercizi.executeQuery();

            while (risultatoTuttiEsercizi.next()) {
                String NomeEsercizio = risultatoTuttiEsercizi.getString("NomeEsercizio");
                DatiSessioneCliente.aggiungiEsercizioConMassimale(NomeEsercizio);
            }
        } catch (SQLException e) {
            System.out.printf(e.getMessage());
        }
    }

    // creo un array con gli esercizi disponibili nella palestra in modo tale che il bot sappia che esercizi suggerire
    public static ArrayList<String> riempiEserciziDisponibiliPalestra() {
        String fetchEsercizi = "SELECT NomeEsercizio FROM Esercizio";
        ArrayList<String> eserciziInPalestra = new ArrayList<>();

        try {
            Connection conn = ConnessioneDatabase.getConnection();
            PreparedStatement esercizi =  conn.prepareStatement(fetchEsercizi);
            ResultSet risultato = esercizi.executeQuery();

            while (risultato.next()) {
                eserciziInPalestra.add(risultato.getString("NomeEsercizio"));
            }
            return eserciziInPalestra;
        } catch (SQLException e) {
            System.out.printf(e.getMessage());
        }
        return eserciziInPalestra;
    }
}