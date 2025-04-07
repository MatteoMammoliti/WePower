package com.wepower.wepower.Models;

import com.wepower.wepower.Views.RigaDashboardAdmin;

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
                t.NomeAbbonamento
                FROM Cliente c LEFT JOIN AbbonamentoCliente a
                ON c.IdCliente = a.IdCliente
                LEFT JOIN TipoAbbonamento t ON a.IdTipoAbbonamento = t.IdTipoAbbonamento
                """;
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
                String DataInizioAbbonamento = risultatoTuttiClienti.getString("DataInizioAbbonamento");
                String DataFineAbbonamento = risultatoTuttiClienti.getString("DataFineAbbonamento");
                String NomeAbbonamento = risultatoTuttiClienti.getString("NomeAbbonamento");
                String email = "test@test.com";
                String sesso = risultatoTuttiClienti.getString("Sesso");

                RigaDashboardAdmin A = new RigaDashboardAdmin(IdCliente, Nome, Cognome, StatoAbbonamento, CertificatoValido, DataInizioAbbonamento, DataFineAbbonamento, email, sesso);
                ris.add(A);
            }

        } catch (Exception e) {
            // Se gestisci transazioni, puoi eventualmente chiamare conn.rollback(),
            // ma qui non sembra necessario.
            throw new RuntimeException(e);
        }

        return ris;
    }
}
