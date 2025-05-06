package com.wepower.wepower.Models.AdminModel;

import com.wepower.wepower.Models.ConnessioneDatabase;
import com.wepower.wepower.Views.AdminView.RigaDashboardAdmin;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TabellaUtentiDashboardAdmin {

    static Connection conn = ConnessioneDatabase.getConnection();

    // ???????? GESTISCI LE ECCEZIONI E PULISCI IL CODICE
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

        try {
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

                String email = risultatoTuttiClienti.getString("Email");
                String sesso = risultatoTuttiClienti.getString("Sesso");
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
        try {
            PreparedStatement elimina = conn.prepareStatement(eliminaUtente);

            elimina.setInt(1, id);
            risultatoEliminazione = elimina.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return risultatoEliminazione > 0;
    }

    public static boolean salvaModifiche (int id, String nome, String cognome,String dataNascita, String dataRinnovo, String dataScadenza,int statoAbbonamento, int idTipoAbbonamento) throws SQLException {
        try {
            conn.setAutoCommit(false);
            String queryUpdateCliente = "UPDATE Cliente SET Nome = ?, Cognome=?, DataNascita=? WHERE IdCliente = ?";
            try (PreparedStatement psCliente = conn.prepareStatement(queryUpdateCliente)) {
                psCliente.setString(1, nome);
                psCliente.setString(2, cognome);
                psCliente.setString(3, dataNascita);
                psCliente.setInt(4, id);
                psCliente.executeUpdate();
            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }

            if (idTipoAbbonamento !=-1) {
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
                    conn.rollback();
                    throw new RuntimeException(ex);
                }

            }
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
    }

    public static ArrayList<String> prelevaTipiAbbonamenti( ) {
        ArrayList<String> tipi = new ArrayList<>();
        String query = "SELECT NomeAbbonamento FROM TipoAbbonamento";
        try {
            PreparedStatement psTipi = conn.prepareStatement(query);
            ResultSet rs = psTipi.executeQuery();

            while (rs.next()) {
                tipi.add(rs.getString("NomeAbbonamento"));
            }
            return tipi;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return tipi;
    }

    public static int prelevaIdTipiAbbonamento(String nomeTipoAbbonamento) {
        String query="SELECT IdTipoAbbonamento FROM TipoAbbonamento WHERE NomeAbbonamento = ?";
        try {
            PreparedStatement psIdTipo=conn.prepareStatement(query);
            psIdTipo.setString(1, nomeTipoAbbonamento);
            ResultSet rs=psIdTipo.executeQuery();
            if (rs.next()) {
                return rs.getInt("IdTipoAbbonamento");
            }
            else{
                return 0;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public static int prelevaDurataTipoAbbonamento(String nomeTipoAbbonamento) throws SQLException {
        String query="SELECT Durata FROM TipoAbbonamento WHERE NomeAbbonamento = ?";
        try {
            PreparedStatement psDurata=conn.prepareStatement(query);
            psDurata.setString(1, nomeTipoAbbonamento);
            ResultSet rs = psDurata.executeQuery();
            if (rs.next()) return rs.getInt("Durata");
            else return 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }
}