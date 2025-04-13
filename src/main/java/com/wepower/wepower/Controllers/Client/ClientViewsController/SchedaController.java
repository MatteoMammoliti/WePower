package com.wepower.wepower.Controllers.Client.ClientViewsController;

import com.wepower.wepower.Models.Model;
import com.wepower.wepower.Models.SchedaAllenamento.TabellaElencoEsercizi;
import com.wepower.wepower.Views.SchedaAllenamento.RigaEsercizioLista;
import com.wepower.wepower.Views.SchedaAllenamento.RigaEsercizioScheda;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SchedaController implements Initializable {

    @FXML
    private VBox containerSchedaAllenamento;

    @FXML
    private VBox containerEsercizi;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().setSchedaController(this);
        try {
            loadEsercizi();
            loadSchedaAllenamento();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
