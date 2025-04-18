package com.wepower.wepower.Models.AdminModel;

import com.wepower.wepower.Models.ConnessioneDatabase;
import com.wepower.wepower.Views.AdminView.RigaDashboardAdmin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TabellaUtentiDashboardAdmin {
    public static ArrayList<RigaDashboardAdmin> riempiRiga() throws SQLException {
        ArrayList<RigaDashboardAdmin> ris = new ArrayList<RigaDashboardAdmin>();
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
        try (Connection conn = ConnessioneDatabase.getConnection()) {
            PreparedStatement datiClienti = conn.prepareStatement(query);
            ResultSet risultatoTuttiClienti = datiClienti.executeQuery();

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

                String NomeAbbonamento = risultatoTuttiClienti.getString("NomeAbbonamento");
                String email = risultatoTuttiClienti.getString("Email");
                String sesso = risultatoTuttiClienti.getString("Sesso");
                System.out.println(DataNascita);
                RigaDashboardAdmin A = new RigaDashboardAdmin(IdCliente, Nome, Cognome,DataNascita, StatoAbbonamento, CertificatoValido, DataInizioAbbonamento, DataFineAbbonamento, email, sesso);
                ris.add(A);
            }

        } catch (Exception e) {

            throw new RuntimeException(e);
        }

        return ris;
    }



    public static boolean eliminaRiga(int id) throws SQLException {
        String eliminaUtente = "DELETE FROM Cliente WHERE IdCliente = ?";
        int risultatoEliminazione;
        try (Connection conn = ConnessioneDatabase.getConnection()) {
            PreparedStatement elimina = conn.prepareStatement(eliminaUtente);

            elimina.setInt(1, id);
            risultatoEliminazione = elimina.executeUpdate();

        } catch (Exception e) {
            // Se gestisci transazioni, puoi eventualmente chiamare conn.rollback(),
            // ma qui non sembra necessario.
            throw new RuntimeException(e);
        }
        return risultatoEliminazione > 0;

    }

    public static boolean aggiornaNome(int idCliente, String nuovoNome) throws SQLException {
        String queryUpdate = "UPDATE Cliente SET Nome = ? WHERE IdCliente = ?";
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(queryUpdate)) {

            ps.setString(1, nuovoNome);
            ps.setInt(2, idCliente);

            int righeAggiornate = ps.executeUpdate();
            return righeAggiornate > 0;
        }
    }

    public static boolean aggiornaCognome(int idCliente, String nuovoCognome) throws SQLException {
        String queryUpdate = "UPDATE Cliente SET Cognome = ? WHERE IdCliente = ?";
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(queryUpdate)) {

            ps.setString(1, nuovoCognome);
            ps.setInt(2, idCliente);

            int righeAggiornate = ps.executeUpdate();
            return righeAggiornate > 0;
        }
    }
    public static boolean annulla(){
        return false;
    }

    public static boolean salvaModifiche (int id, String nome, String cognome,String dataNascita, String dataRinnovo, String dataScadenza,int statoAbbonamento, int idTipoAbbonamento) throws SQLException {

        Connection conn = null;
        try {
            conn = ConnessioneDatabase.getConnection();
            conn.setAutoCommit(false);
            String queryUpdateCliente = "UPDATE Cliente SET Nome = ?, Cognome=?, DataNascita=? WHERE IdCliente = ?";


            try (PreparedStatement psCliente = conn.prepareStatement(queryUpdateCliente)) {
                psCliente.setString(1, nome);
                psCliente.setString(2, cognome);
                psCliente.setString(3, dataNascita);
                psCliente.setInt(4, id);
                System.out.printf("MATTTEO CHECK");
                psCliente.executeUpdate();
            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }

            if (idTipoAbbonamento !=-1 && dataRinnovo != null) {
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
                    throw new RuntimeException(ex);
                }

            }

            System.out.println("ora sto inviando");
            conn.commit();
            return true;
        }
        catch (SQLException e) {
            try {
                conn.rollback();
                return false;
            }
            catch (SQLException ex) {
                ex.printStackTrace();
            }
            throw e;

        }
        finally {
            conn.close();

        }
    }


    public static boolean aggiornaDataNascita(int idCliente, String dataNascita) throws SQLException {
        String queryUpdate = "UPDATE Cliente SET DataNascita = ? WHERE IdCliente = ?";
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(queryUpdate)) {

            ps.setString(1, dataNascita);
            ps.setInt(2, idCliente);
            int righeAggiornate = ps.executeUpdate();
            return righeAggiornate > 0;
        }
    }

    public static boolean aggiornaDataRinnovo(int idCliente, String dataRinnovo) throws SQLException {
        String queryUpdate = "UPDATE AbbonamentoCliente SET DataInizioAbbonamento = ? WHERE IdCliente = ?";
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(queryUpdate)) {

            ps.setString(1, dataRinnovo);
            ps.setInt(2, idCliente);
            int righeAggiornate = ps.executeUpdate();
            return righeAggiornate > 0;
        }


    }

    public static boolean aggiornaDataScadenza(int idCliente, String dataScadenza) throws SQLException {
        String queryUpdate = "UPDATE AbbonamentoCliente SET DataFineAbbonamento = ? WHERE IdCliente = ?";
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(queryUpdate)) {

            ps.setString(1, dataScadenza);
            ps.setInt(2, idCliente);
            int righeAggiornate = ps.executeUpdate();
            return righeAggiornate > 0;
        }


    }

    public static boolean aggiornaStatoAbbonamento(int idCliente, int statoAbbonamento) throws SQLException {
        String queryUpdate = "UPDATE AbbonamentoCliente SET StatoAbbonamento = ? WHERE IdCliente = ?";
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(queryUpdate)) {

            ps.setInt(1, statoAbbonamento);
            ps.setInt(2, idCliente);
            int righeAggiornate = ps.executeUpdate();
            return righeAggiornate > 0;
        }}

    public static ArrayList<String> prelevaTipiAbbonamenti( ){
        ArrayList<String> tipi = new ArrayList<>();
        String query = "SELECT NomeAbbonamento FROM TipoAbbonamento";
        try (Connection conn=ConnessioneDatabase.getConnection();
             PreparedStatement psTipi=conn.prepareStatement(query)) {
            ResultSet rs=psTipi.executeQuery();
            while (rs.next()) {
                tipi.add(rs.getString("NomeAbbonamento"));
            }
            return tipi;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static int prelevaIdTipiAbbonamento(String nomeTipoAbbonamento) throws SQLException {
        String query="SELECT IdTipoAbbonamento FROM TipoAbbonamento WHERE NomeAbbonamento = ?";
        try(Connection conn=ConnessioneDatabase.getConnection();
            PreparedStatement psIdTipo=conn.prepareStatement(query)){
            psIdTipo.setString(1, nomeTipoAbbonamento);
            ResultSet rs=psIdTipo.executeQuery();
            if (rs.next()) {
                return rs.getInt("IdTipoAbbonamento");
            }
            else{
                return 0;
            }
        }



    }





    public static int prelevaDurataTipoAbbonamento(String nomeTipoAbbonamento) throws SQLException {
        String query="SELECT Durata FROM TipoAbbonamento WHERE NomeAbbonamento = ?";
        try(Connection conn=ConnessioneDatabase.getConnection();
            PreparedStatement psDurata=conn.prepareStatement(query)) {
            psDurata.setString(1, nomeTipoAbbonamento);
            ResultSet rs = psDurata.executeQuery();
            if (rs.next()) {
                return rs.getInt("Durata");
            } else {
                return 0;
            }
        }

    }
}