package com.wepower.wepower.Models;

import com.wepower.wepower.Models.DatiPalestra.DatiSessionePalestra;
import com.wepower.wepower.Models.DatiPalestra.PrenotazioneSalaPesi;
import com.wepower.wepower.Models.DatiPalestra.PrenotazioneSalaPesiCliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ModelAutenticazione {
    public static boolean verificaCredenziali(String email, String password) throws SQLException {
        String Query2 = "SELECT * FROM Admin WHERE Email = ? AND Password = ?";
        String Query="SELECT c.idCliente,c.CertificatoValido,c.nome, c.cognome, cc.Email,cc.Telefono FROM CredenzialiCliente cc JOIN Cliente c ON cc.idCliente=c.idCliente WHERE cc.Email = ? AND cc.Password = ?";
        String Query3="SELECT a.StatoAbbonamento FROM AbbonamentoCliente a JOIN Cliente c ON a.idCliente=c.idCliente WHERE a.idCliente = ?";
        try (Connection conn = ConnessioneDatabase.getConnection()) {

            try (PreparedStatement datiCliente = conn.prepareStatement(Query)) {
                datiCliente.setString(1, email);
                datiCliente.setString(2, password);
                ResultSet risultatoClienti = datiCliente.executeQuery();
                //Prelevo i dati del cliente
                if (risultatoClienti.next()) {
                    System.out.println("Login cliente effettuato con successo");
                    DatiSessioneCliente.setIdUtente(risultatoClienti.getInt("idCliente"));
                    DatiSessioneCliente.setNomeUtente(risultatoClienti.getString("nome"));
                    DatiSessioneCliente.setCognome(risultatoClienti.getString("cognome"));
                    DatiSessioneCliente.setEmail(risultatoClienti.getString("Email"));
                    DatiSessioneCliente.setCertificato(risultatoClienti.getBoolean("CertificatoValido"));
                    DatiSessioneCliente.setDateOrariPrenotazioni(caricaDatePrenotazioniSalaPesi(risultatoClienti.getInt("idCliente")));
                    try(PreparedStatement statoAbbonamento=conn.prepareStatement(Query3)) {
                        statoAbbonamento.setInt(1, risultatoClienti.getInt("idCliente"));
                        ResultSet risultatoStato=statoAbbonamento.executeQuery();
                        if(risultatoStato.next()) {
                            System.out.println(risultatoStato.getInt("StatoAbbonamento"));
                            if(risultatoStato.getInt("StatoAbbonamento")==1) {
                                System.out.println("Abbonamento si");
                                DatiSessioneCliente.setStatoAbbonamento(true);
                            }
                            else{
                                DatiSessioneCliente.setStatoAbbonamento(false);
                            }
                        }
                    }

                    String querySchedaAllenamento = "SELECT IdScheda FROM SchedaAllenamento WHERE IdCliente = ?";

                    try (PreparedStatement datiSchedaAllenamento = conn.prepareStatement(querySchedaAllenamento)) {
                        datiSchedaAllenamento.setInt(1, DatiSessioneCliente.getIdUtente());
                        ResultSet risultatoScheda = datiSchedaAllenamento.executeQuery();

                        if (risultatoScheda.next()) {
                            DatiSessioneCliente.setIdSchedaAbbonamento(risultatoScheda.getInt("IdScheda"));
                        } else {
                            DatiSessioneCliente.setIdSchedaAbbonamento(0);
                        }
                    }
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
                    System.out.println("Trovata");
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


}
