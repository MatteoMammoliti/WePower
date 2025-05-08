package com.wepower.wepower.Models.SchedaAllenamento;

import com.wepower.wepower.Models.ConnessioneDatabase;
import com.wepower.wepower.Models.DatiSessioneCliente;
import com.wepower.wepower.Models.Model;
import com.wepower.wepower.Views.AlertHelper;
import javafx.scene.control.Alert;

import java.sql.*;

public class ModelSchedaAllenamentoCliente {

    public static void creaScheda() {
        Connection conn = ConnessioneDatabase.getConnection();

        String creazioneScheda = "INSERT INTO SchedaAllenamento (IdCliente) VALUES (?)";
        String prelevaIDScheda = "SELECT IdScheda FROM SchedaAllenamento WHERE IdCliente = ? AND SchedaAncoraInUso = 1 LIMIT 1";

        PreparedStatement inserimentoScheda = null;
        PreparedStatement prelevaIdScheda = null;
        try{
            inserimentoScheda = conn.prepareStatement(creazioneScheda);
            inserimentoScheda.setInt(1, DatiSessioneCliente.getIdUtente());
            inserimentoScheda.executeUpdate();

            prelevaIdScheda = conn.prepareStatement(prelevaIDScheda);
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
            AlertHelper.showAlert("Questo non doveva succedere", "Qualcosa è andato storto nella crezione della scheda", null, Alert.AlertType.ERROR);
        } finally {
            if (inserimentoScheda != null) {
                try { inserimentoScheda.close(); } catch (SQLException ignored) {}
            }
            if (prelevaIdScheda != null) {
                try { prelevaIdScheda.close(); } catch (SQLException ignored) {}
            }
        }
    }

    public static void richiediScheda() {
        Connection conn = ConnessioneDatabase.getConnection();

        String creazioneScheda = "INSERT INTO SchedaAllenamento (IdCliente, IdAdmin) VALUES (?, ?)";
        String prelevaIDScheda = "SELECT IdScheda FROM SchedaAllenamento WHERE IdCliente = ? AND SchedaAncoraInUso = 1 AND IdAdmin = 1 AND SchedaCompilata = 0 LIMIT 1";

        PreparedStatement inserimentoScheda = null;
        PreparedStatement prelevaIdScheda = null;
        try {
            inserimentoScheda = conn.prepareStatement(creazioneScheda);
            inserimentoScheda.setInt(1, DatiSessioneCliente.getIdUtente());
            inserimentoScheda.setInt(2, 1);
            inserimentoScheda.executeUpdate();

            prelevaIdScheda = conn.prepareStatement(prelevaIDScheda);
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
            AlertHelper.showAlert("Questo non doveva succedere", "Qualcosa è andato storto nella richiesta della scheda", null, Alert.AlertType.ERROR);
        } finally {
            if (inserimentoScheda != null) {
                try { inserimentoScheda.close(); } catch (SQLException ignored) {}
            }
            if (prelevaIdScheda != null) {
                try { prelevaIdScheda.close(); } catch (SQLException ignored) {}
            }
        }
    }

    public static void inserisciEsercizioScheda(String nomeEsercizio, int idSchedaAllenamento, String numeroRipetizioni, String numeroSerie) {
        Connection conn = ConnessioneDatabase.getConnection();

        String inserimentoEsercizio = "INSERT INTO ComposizioneSchedaAllenamento (NomeEsercizio, IdSchedaAllenamento, NumeroRipetizioni, NumeroSerie) " +
                "VALUES (?, ?, ?, ?)";

        PreparedStatement inserimentoScheda = null;
        try {
            inserimentoScheda = conn.prepareStatement(inserimentoEsercizio);
            inserimentoScheda.setString(1, nomeEsercizio);
            inserimentoScheda.setInt(2, idSchedaAllenamento);
            inserimentoScheda.setString(3, numeroRipetizioni);
            inserimentoScheda.setString(4, numeroSerie);
            inserimentoScheda.executeUpdate();
        } catch (SQLException e) {
            AlertHelper.showAlert("Questo non doveva succedere", "Qualcosa è andato storto nell'inserimento", null, Alert.AlertType.ERROR);
        } finally {
            if (inserimentoScheda != null) {
                try { inserimentoScheda.close(); } catch (SQLException ignored) {}
            }
        }
    }

    public static void onRimuoviEsercizio(String nomeEsercizio, int idSchedaAllenamento) {
        Connection conn = ConnessioneDatabase.getConnection();

        String eliminaEsercizio = "DELETE FROM ComposizioneSchedaAllenamento WHERE NomeEsercizio = ? AND IdSchedaAllenamento = ?";

        PreparedStatement eliminazione = null;
        try {
            eliminazione = conn.prepareStatement(eliminaEsercizio);
            eliminazione.setString(1, nomeEsercizio);
            eliminazione.setInt(2, idSchedaAllenamento);
            eliminazione.executeUpdate();

            Model.getInstance().getSchedaController().loadSchedaAllenamento();
            Model.getInstance().getSchedaController().loadEsercizi();
        } catch (SQLException e) {
            AlertHelper.showAlert("Questo non doveva succedere", "Qualcosa è andato storto nella rimozione", null, Alert.AlertType.ERROR);
        } finally {
            if (eliminazione != null) {
                try { eliminazione.close(); } catch (SQLException ignored) {}
            }
        }
    }

    public static void onAggiungiNuovoMassimale(int idUtente, String nomeEsercizio, String dataInserimento, double peso) throws SQLException {
        Connection conn = ConnessioneDatabase.getConnection();

        String massimale = "INSERT INTO MassimaleImpostatoCliente (IdCliente, NomeEsercizio, DataInserimento, Peso) VALUES (?, ?, ?, ?)";

        PreparedStatement inserimento = null;
        try {
            inserimento = conn.prepareStatement(massimale);
            inserimento.setInt(1, idUtente);
            inserimento.setString(2, nomeEsercizio);
            inserimento.setString(3, dataInserimento);
            inserimento.setDouble(4, peso);
            inserimento.executeUpdate();

            Model.getInstance().getSchedaController().loadSchedaAllenamento();
            Model.getInstance().getSchedaController().loadEsercizi();
            Model.getInstance().getViewFactoryClient().invalidateDashboard();
            DatiSessioneCliente.aggiungiEsercizioConMassimale(nomeEsercizio);
        } catch (SQLException e) {
            AlertHelper.showAlert("Questo non doveva succedere", "Qualcosa è andato storto nell'aggiunta di un nuovo massimale", null, Alert.AlertType.ERROR);
        } finally {
            if (inserimento != null) {
                try { inserimento.close(); } catch (SQLException ignored) {}
            }
        }
    }

    public static void onUpdateMassimale(double nuovoMassimale, int idCliente, String nomeEsercizio) {
        Connection conn = ConnessioneDatabase.getConnection();

        String updateMassimale = "UPDATE MassimaleImpostatoCliente SET Peso = ? WHERE IdCliente = ? AND NomeEsercizio = ?";

        PreparedStatement inserimento = null;
        try {
            inserimento = conn.prepareStatement(updateMassimale);
            inserimento.setDouble(1, nuovoMassimale);
            inserimento.setInt(2, idCliente);
            inserimento.setString(3, nomeEsercizio);
            inserimento.executeUpdate();

            Model.getInstance().getSchedaController().loadSchedaAllenamento();
            Model.getInstance().getViewFactoryClient().invalidateDashboard();
        } catch (SQLException e) {
            AlertHelper.showAlert("Questo non doveva succedere", "Qualcosa è andato storto nell'aggiornamento del massimale", null, Alert.AlertType.ERROR);
        } finally {
            if (inserimento != null) {
                try { inserimento.close(); } catch (SQLException ignored) {}
            }
        }
    }

    public static void eliminaSchedaAllenamento(int idScheda) throws SQLException {
        Connection conn = ConnessioneDatabase.getConnection();

        String eliminazioneScheda = "DELETE FROM SchedaAllenamento WHERE IdScheda = ?";
        int statusQuery = 0;

        PreparedStatement eliminazione = null;
        try {
            eliminazione = conn.prepareStatement(eliminazioneScheda);
            eliminazione.setInt(1, idScheda);
            statusQuery = eliminazione.executeUpdate();
        } catch (SQLException e) {
            AlertHelper.showAlert("Questo non doveva succedere", "Qualcosa è andato storto nell'eliminazione della scheda", null, Alert.AlertType.ERROR);
        } finally {
            if (eliminazione != null) {
                try { eliminazione.close(); } catch (SQLException ignored) {}
            }
        }

        if(statusQuery == 0) return;
        DatiSessioneCliente.setIdSchedaAllenamento(0);
        DatiSessioneCliente.setSeSchedaRichiesta(false);
        Model.getInstance().getViewFactoryClient().invalidateSchedaView();
        Model.getInstance().getViewFactoryClient().invalidateMyProfileView();
        Model.getInstance().getViewFactoryClient().getCurrentMenuView().setValue("Dashboard");
        Model.getInstance().getViewFactoryClient().getCurrentMenuView().setValue("Scheda");
    }
}