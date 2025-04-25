package com.wepower.wepower.Models.SchedaAllenamento;

import com.wepower.wepower.Models.ConnessioneDatabase;
import com.wepower.wepower.Models.DatiSessioneCliente;
import com.wepower.wepower.Models.Model;
import java.sql.*;

public class ModelSchedaAllenamentoCliente {

    public static void creaScheda() {
        String creazioneScheda = "INSERT INTO SchedaAllenamento (IdCliente) VALUES (?)";
        String prelevaIDScheda = "SELECT IdScheda FROM SchedaAllenamento WHERE IdCliente = ? AND SchedaAncoraInUso = 1 LIMIT 1";

        try(Connection conn = ConnessioneDatabase.getConnection()) {
            PreparedStatement inserimentoScheda = conn.prepareStatement(creazioneScheda);
            inserimentoScheda.setInt(1, DatiSessioneCliente.getIdUtente());
            inserimentoScheda.executeUpdate();

            PreparedStatement prelevaIdScheda = conn.prepareStatement(prelevaIDScheda);
            prelevaIdScheda.setInt(1, DatiSessioneCliente.getIdUtente());
            ResultSet resultSet = prelevaIdScheda.executeQuery();
            if (resultSet.next()) {
                DatiSessioneCliente.setIdSchedaAllenamento(resultSet.getInt("IdScheda"));
                Model.getInstance().getViewFactoryClient().invalidateSchedaView();
                Model.getInstance().getViewFactoryClient().invalidateMyProfileView();
                Model.getInstance().getViewFactoryClient().getCurrentMenuView().set("Dashboard");
                Model.getInstance().getViewFactoryClient().getCurrentMenuView().set("Scheda");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void richiediScheda() {
        String creazioneScheda = "INSERT INTO SchedaAllenamento (IdCliente, IdAdmin) VALUES (?, ?)";
        String prelevaIDScheda = "SELECT IdScheda FROM SchedaAllenamento WHERE IdCliente = ? AND SchedaAncoraInUso = 1 AND IdAdmin = 1 AND SchedaCompilata = 0 LIMIT 1";

        try(Connection conn = ConnessioneDatabase.getConnection()) {
            PreparedStatement inserimentoScheda = conn.prepareStatement(creazioneScheda);
            inserimentoScheda.setInt(1, DatiSessioneCliente.getIdUtente());
            inserimentoScheda.setInt(2, 1);
            inserimentoScheda.executeUpdate();

            PreparedStatement prelevaIdScheda = conn.prepareStatement(prelevaIDScheda);
            prelevaIdScheda.setInt(1, DatiSessioneCliente.getIdUtente());
            ResultSet resultSet = prelevaIdScheda.executeQuery();
            if (resultSet.next()) {
                DatiSessioneCliente.setIdSchedaAllenamento(resultSet.getInt("IdScheda"));
                DatiSessioneCliente.setSeSchedaRichiesta(true);
                Model.getInstance().getViewFactoryClient().invalidateSchedaView();
                Model.getInstance().getViewFactoryClient().invalidateMyProfileView();
                Model.getInstance().getViewFactoryClient().getCurrentMenuView().set("Dashboard");
                Model.getInstance().getViewFactoryClient().getCurrentMenuView().set("Scheda");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void inserisciEsercizioScheda(String nomeEsercizio, int idSchedaAllenamento, String numeroRipetizioni, String numeroSerie) {
        String inserimentoEsercizio = "INSERT INTO ComposizioneSchedaAllenamento (NomeEsercizio, IdSchedaAllenamento, NumeroRipetizioni, NumeroSerie) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnessioneDatabase.getConnection()) {
            PreparedStatement inserimentoScheda = conn.prepareStatement(inserimentoEsercizio);
            inserimentoScheda.setString(1, nomeEsercizio);
            inserimentoScheda.setInt(2, idSchedaAllenamento);
            inserimentoScheda.setString(3, numeroRipetizioni);
            inserimentoScheda.setString(4, numeroSerie);
            inserimentoScheda.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void onRimuoviEsercizio(String nomeEsercizio, int idSchedaAllenamento) {
        String eliminaEsercizio = "DELETE FROM ComposizioneSchedaAllenamento WHERE NomeEsercizio = ? AND IdSchedaAllenamento = ?";

        try (Connection conn = ConnessioneDatabase.getConnection()) {
            PreparedStatement eliminazione = conn.prepareStatement(eliminaEsercizio);
            eliminazione.setString(1, nomeEsercizio);
            eliminazione.setInt(2, idSchedaAllenamento);
            eliminazione.executeUpdate();

            Model.getInstance().getSchedaController().loadSchedaAllenamento();
            Model.getInstance().getSchedaController().loadEsercizi();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void onAggiungiNuovoMassimale(int idUtente, String nomeEsercizio, String dataInserimento, double peso) throws SQLException {
        String massimale = "INSERT INTO MassimaleImpostatoCliente (IdCliente, NomeEsercizio, DataInserimento, Peso) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnessioneDatabase.getConnection()) {
            PreparedStatement inserimento = conn.prepareStatement(massimale);
            inserimento.setInt(1, idUtente);
            inserimento.setString(2, nomeEsercizio);
            inserimento.setString(3, dataInserimento);
            inserimento.setDouble(4, peso);
            inserimento.executeUpdate();

            Model.getInstance().getSchedaController().loadSchedaAllenamento();
            Model.getInstance().getSchedaController().loadEsercizi();
            Model.getInstance().getViewFactoryClient().invalidateDashboard();
            DatiSessioneCliente.aggiungiEsercizioConMassimale(nomeEsercizio);
        }
    }

    public static void onUpdateMassimale(double nuovoMassimale, int idCliente, String nomeEsercizio) throws SQLException {
        String updateMassimale = "UPDATE MassimaleImpostatoCliente SET Peso = ? WHERE IdCliente = ? AND NomeEsercizio = ?";
        try (Connection conn = ConnessioneDatabase.getConnection()) {
            PreparedStatement inserimento = conn.prepareStatement(updateMassimale);
            inserimento.setDouble(1, nuovoMassimale);
            inserimento.setInt(2, idCliente);
            inserimento.setString(3, nomeEsercizio);
            inserimento.executeUpdate();

            Model.getInstance().getSchedaController().loadSchedaAllenamento();
            Model.getInstance().getViewFactoryClient().invalidateDashboard();
        }
    }

    public static void eliminaSchedaAllenamento(int idScheda) throws SQLException {
        String eliminazioneScheda = "DELETE FROM SchedaAllenamento WHERE IdScheda = ?";

        try (Connection conn = ConnessioneDatabase.getConnection()) {
            PreparedStatement eliminazione = conn.prepareStatement(eliminazioneScheda);
            eliminazione.setInt(1, idScheda);
            eliminazione.executeUpdate();
        }

        DatiSessioneCliente.setIdSchedaAllenamento(0);
        DatiSessioneCliente.setSeSchedaRichiesta(false);
        Model.getInstance().getViewFactoryClient().invalidateSchedaView();
        Model.getInstance().getViewFactoryClient().invalidateMyProfileView();
        Model.getInstance().getViewFactoryClient().getCurrentMenuView().setValue("Dashboard");
        Model.getInstance().getViewFactoryClient().getCurrentMenuView().setValue("Scheda");
    }
}