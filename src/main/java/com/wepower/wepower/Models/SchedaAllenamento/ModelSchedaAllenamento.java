package com.wepower.wepower.Models.SchedaAllenamento;

import com.wepower.wepower.Models.ConnessioneDatabase;
import com.wepower.wepower.Models.DatiSessioneCliente;
import com.wepower.wepower.Models.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ModelSchedaAllenamento {

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
        String prelevaIDScheda = "SELECT IdScheda FROM SchedaAllenamento WHERE IdCliente = ? AND SchedaAncoraInUso = 1 LIMIT 1";

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
}
