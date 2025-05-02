package com.wepower.wepower.Models;

import com.wepower.wepower.Models.DatiPalestra.DatiSessionePalestra;
import com.wepower.wepower.Models.DatiPalestra.PrenotazioneSalaPesi;
import com.wepower.wepower.Models.DatiPalestra.PrenotazioneSalaPesiCliente;
import javafx.scene.image.Image;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ModelAutenticazione {
    public static boolean verificaCredenziali(String email, String password) throws SQLException {
        String Query2 = "SELECT * FROM Admin WHERE Email = ? AND Password = ?";
        String Query="SELECT c.IdCliente,c.CertificatoValido,c.Nome, c.Cognome,c.DataNascita, cc.Email,cc.Telefono, c.ImmagineProfilo, c.Altezza,c.Sesso FROM CredenzialiCliente cc JOIN Cliente c ON cc.idCliente=c.idCliente WHERE cc.Email = ? AND cc.Password = ?";
        String Query3="SELECT a.StatoAbbonamento FROM AbbonamentoCliente a JOIN Cliente c ON a.IdCliente=c.IdCliente WHERE a.IdCliente = ? and a.StatoAbbonamento=1";
        try (Connection conn = ConnessioneDatabase.getConnection()) {

            try (PreparedStatement datiCliente = conn.prepareStatement(Query)) {
                datiCliente.setString(1, email);
                datiCliente.setString(2, password);
                ResultSet risultatoClienti = datiCliente.executeQuery();
                //Prelevo i dati del cliente
                if (risultatoClienti.next()) {
                    System.out.println("Login cliente effettuato con successo");
                    DatiSessioneCliente.setIdUtente(risultatoClienti.getInt("IdCliente"));
                    DatiSessioneCliente.setNomeUtente(risultatoClienti.getString("Nome"));
                    DatiSessioneCliente.setCognome(risultatoClienti.getString("Cognome"));
                    DatiSessioneCliente.setEmail(risultatoClienti.getString("Email"));
                    DatiSessioneCliente.setCertificato(risultatoClienti.getInt("CertificatoValido"));
                    DatiSessioneCliente.setDataNascita(risultatoClienti.getString("DataNascita"));
                    DatiSessioneCliente.setTelefono(risultatoClienti.getString("Telefono"));
                    DatiSessioneCliente.caricaPesoAttuale(risultatoClienti.getInt("IdCliente"));
                    InputStream immagine = risultatoClienti.getBinaryStream("ImmagineProfilo");
                    DatiSessioneCliente.setAltezza(risultatoClienti.getString("Altezza"));
                    DatiSessioneCliente.setGenere(risultatoClienti.getString("Sesso"));
                    DatiSessionePalestra.setNumeroMassimoPrenotazioni();

                    Image immagineProfilo;
                    if (immagine != null) {
                        immagineProfilo = new Image(immagine);
                    } else {
                        immagineProfilo = new Image(ModelAutenticazione.class.getResource("/Images/defaultImgProfilo.png").toExternalForm());
                    }
                    DatiSessioneCliente.setImmagineProfilo(immagineProfilo);

                    DatiSessioneCliente.setDateOrariPrenotazioni(caricaDatePrenotazioniSalaPesi(risultatoClienti.getInt("IdCliente")));

                    if (DatiSessioneCliente.isAbbonamentoAttivo()) {
                        DatiSessioneCliente.setStatoAbbonamento(true);
                    } else {
                        DatiSessioneCliente.setStatoAbbonamento(false);
                        String updateStatoAbbonamento = "UPDATE AbbonamentoCliente SET StatoAbbonamento = 0 WHERE IdCliente = ? AND StatoAbbonamento = 1";
                        try (PreparedStatement updateStato = conn.prepareStatement(updateStatoAbbonamento)) {
                            updateStato.setInt(1, DatiSessioneCliente.getIdUtente());
                            updateStato.executeUpdate();
                        }
                    }

                    String querySchedaAllenamento = "SELECT IdScheda, IdAdmin FROM SchedaAllenamento WHERE IdCliente = ? AND SchedaAncoraInUso = 1 LIMIT 1";

                    try (PreparedStatement datiSchedaAllenamento = conn.prepareStatement(querySchedaAllenamento)) {
                        datiSchedaAllenamento.setInt(1, DatiSessioneCliente.getIdUtente());
                        ResultSet risultatoScheda = datiSchedaAllenamento.executeQuery();

                        if (risultatoScheda.next()) {
                            DatiSessioneCliente.setIdSchedaAllenamento(risultatoScheda.getInt("IdScheda"));
                            DatiSessioneCliente.setSeSchedaRichiesta(risultatoScheda.getInt("IdAdmin") == 1);
                        } else {
                            DatiSessioneCliente.setIdSchedaAllenamento(0);
                            DatiSessioneCliente.setSeSchedaRichiesta(false);
                        }
                    }
                    riempiListaEserciziConMassimali(DatiSessioneCliente.getIdUtente());
                    return true;
                }
                //Se non trovo il cliente, vado a vedere se è un admin e prelevo i suoi dati
                try(PreparedStatement datiAdmin =conn.prepareStatement(Query2)) {
                    datiAdmin.setString(1, email);
                    datiAdmin.setString(2, password);
                    ResultSet risultatoAdmin = datiAdmin.executeQuery();
                    if(risultatoAdmin.next()) {
                        System.out.println("Login admin effettuato con successo");
                        DatiSessioneCliente.setNomeUtente("Admin");
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("Login fallito");
        return false;
    }

    public static void prelevaDatiPalestra() throws SQLException {
        String prelevaDatiPrenotazioniSalaPesi="SELECT * FROM PrenotazioneSalaPesi";
        try(Connection conn=ConnessioneDatabase.getConnection()){
            PreparedStatement datiPrenotazioni=conn.prepareStatement(prelevaDatiPrenotazioniSalaPesi);
            try(ResultSet risultato=datiPrenotazioni.executeQuery()){
                while(risultato.next()){
                    PrenotazioneSalaPesiCliente prenotazione=new PrenotazioneSalaPesiCliente(risultato.getInt("IdCliente"),risultato.getString("DataPrenotazione"),risultato.getString("OrarioPrenotazione"));
                    DatiSessionePalestra.aggiungiPrenotazioneSalaPesi(prenotazione);
                }
            }
        }
    };

    //Vado a prendermi dal Database tutte le date in cui il cliente ha prenotato la salapesi per mostrarle nel calendario

    public static ArrayList<PrenotazioneSalaPesi> caricaDatePrenotazioniSalaPesi(int idUtente) throws SQLException{
        ArrayList<PrenotazioneSalaPesi>  datePrenotazioni = new ArrayList<>();

        String query = "SELECT DataPrenotazione, OrarioPrenotazione FROM PrenotazioneSalaPesi WHERE idCliente = ?";
        try (Connection conn = ConnessioneDatabase.getConnection()) {
            try (PreparedStatement datiPrenotazione = conn.prepareStatement(query)) {

                datiPrenotazione.setInt(1, idUtente);
                ResultSet risultatoPrenotazioni = datiPrenotazione.executeQuery();

                while (risultatoPrenotazioni.next()) {
                    String data = risultatoPrenotazioni.getString("DataPrenotazione");
                    String orario = risultatoPrenotazioni.getString("OrarioPrenotazione");
                    PrenotazioneSalaPesi prenotazione=new PrenotazioneSalaPesi(data,orario);
                    datePrenotazioni.add(prenotazione);
                }
            }
        }catch (SQLException e) {

        }
        return datePrenotazioni;
    }

    public static void riempiListaEserciziConMassimali(int Id) {

        String query = "SELECT NomeEsercizio FROM MassimaleImpostatoCliente WHERE IdCliente = ?";
        try (Connection conn = ConnessioneDatabase.getConnection()) {
            PreparedStatement datiEsercizi = conn.prepareStatement(query);
            datiEsercizi.setInt(1, Id);
            ResultSet risultatoTuttiEsercizi = datiEsercizi.executeQuery();

            while (risultatoTuttiEsercizi.next()) {
                String NomeEsercizio = risultatoTuttiEsercizi.getString("NomeEsercizio");
                DatiSessioneCliente.aggiungiEsercizioConMassimale(NomeEsercizio);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        for(String esercizio : DatiSessioneCliente.getEserciziConMassimale()) {
            System.out.println(esercizio);
        }
    }

    public static ArrayList<String> riempiEserciziDisponibiliPalestra() {
        String fetchEsercizi = "SELECT NomeEsercizio FROM Esercizio";
        ArrayList<String> temp = new ArrayList<>();

        try (Connection conn = ConnessioneDatabase.getConnection()) {
            PreparedStatement esercizi =  conn.prepareStatement(fetchEsercizi);
            ResultSet risultato = esercizi.executeQuery();

            while (risultato.next()) {
                temp.add(risultato.getString("NomeEsercizio"));
            }
            return temp;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}