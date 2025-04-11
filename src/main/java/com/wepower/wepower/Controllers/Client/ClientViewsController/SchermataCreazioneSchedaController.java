package com.wepower.wepower.Controllers.Client.ClientViewsController;

import com.wepower.wepower.Models.ConnessioneDatabase;
import com.wepower.wepower.Models.DatiSessioneCliente;
import com.wepower.wepower.Models.Model;
import javafx.scene.control.Button;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SchermataCreazioneSchedaController {
    public Button createScheda;
    public Button askScheda;

    public void initialize() {
        createScheda.setOnAction(event -> {

            String creazioneScheda = "INSERT INTO SchedaAllenamento (IdCliente) VALUES (?)";
            String prelevaIDScheda = "SELECT IdScheda FROM SchedaAllenamento WHERE IdCliente = ?";

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
                    Model.getInstance().getViewFactoryClient().getCurrentMenuView().set("Dashboard"); // o un'altra qualsiasi temporanea
                    Model.getInstance().getViewFactoryClient().getCurrentMenuView().set("Scheda");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        askScheda.setOnAction(event -> {});
    }
}
