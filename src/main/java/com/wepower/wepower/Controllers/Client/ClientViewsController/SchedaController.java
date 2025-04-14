package com.wepower.wepower.Controllers.Client.ClientViewsController;

import com.wepower.wepower.Models.ConnessioneDatabase;
import com.wepower.wepower.Models.DatiSessioneCliente;
import com.wepower.wepower.Models.Model;
import com.wepower.wepower.Models.SchedaAllenamento.TabellaElencoEsercizi;
import com.wepower.wepower.Views.SchedaAllenamento.RigaEsercizioLista;
import com.wepower.wepower.Views.SchedaAllenamento.RigaEsercizioScheda;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SchedaController implements Initializable {

    @FXML
    private Button eliminaSchedaButton;

    @FXML
    private VBox containerSchedaAllenamento;

    @FXML
    private VBox containerEsercizi;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().setSchedaController(this);
        try {
            loadEsercizi();
            loadSchedaAllenamento();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        this.eliminaSchedaButton.setOnAction(e -> {
            try {
                onClickEliminaSchedaButton();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    public void loadSchedaAllenamento() throws SQLException {
        ArrayList<RigaEsercizioScheda> schedaAllenamento = TabellaElencoEsercizi.riempiRigaEsercizioScheda();
        containerSchedaAllenamento.getChildren().clear();

        if (!schedaAllenamento.isEmpty()) {
            containerSchedaAllenamento.getChildren().addAll(schedaAllenamento);
        } else {
            Label label = new Label("Nessun esercizio presente nella scheda. Componila!");
            containerSchedaAllenamento.getChildren().add(label);
        }
    }

    public void loadEsercizi() throws SQLException {
        ArrayList<RigaEsercizioLista> esercizi = TabellaElencoEsercizi.riempiRigaEsercizio();
        containerEsercizi.getChildren().clear();
        containerEsercizi.getChildren().addAll(esercizi);
    }

    public void onClickEliminaSchedaButton() throws SQLException {

        String eliminazioneScheda = "UPDATE SchedaAllenamento SET SchedaAncoraInUso = 0 WHERE IdScheda = ? AND IdCliente = ?";

        try (Connection conn = ConnessioneDatabase.getConnection()) {
            PreparedStatement eliminazione = conn.prepareStatement(eliminazioneScheda);
            eliminazione.setInt(1, DatiSessioneCliente.getIdSchedaAllenamento());
            eliminazione.setInt(2, DatiSessioneCliente.getIdUtente());
            eliminazione.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        DatiSessioneCliente.setIdSchedaAllenamento(0);
        Model.getInstance().getViewFactoryClient().invalidateSchedaView();
        Model.getInstance().getViewFactoryClient().invalidateMyProfileView();
        Model.getInstance().getViewFactoryClient().getCurrentMenuView().setValue("Dashboard");
        Model.getInstance().getViewFactoryClient().getCurrentMenuView().setValue("Scheda");
    }
}
