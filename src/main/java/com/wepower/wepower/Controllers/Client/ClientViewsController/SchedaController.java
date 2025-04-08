package com.wepower.wepower.Controllers.Client.ClientViewsController;

import com.wepower.wepower.Models.SchedaAllenamento.TabellaElencoEsercizi;
import com.wepower.wepower.Views.SchedaAllenamento.EsercizioPerLista;
import com.wepower.wepower.Views.SchedaAllenamento.EsercizioPerScheda;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

    private void loadSchedaAllenamento() throws SQLException {
        ArrayList<EsercizioPerScheda> schedaAllenamento = TabellaElencoEsercizi.riempiRigaEsercizioScheda();
        containerSchedaAllenamento.getChildren().clear();
        containerSchedaAllenamento.getChildren().addAll(schedaAllenamento);
    }

    private void loadEsercizi() throws SQLException {
        ArrayList<EsercizioPerLista> esercizi = TabellaElencoEsercizi.riempiRigaEsercizio();
        containerEsercizi.getChildren().clear();
        containerEsercizi.getChildren().addAll(esercizi);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadEsercizi();
            loadSchedaAllenamento();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
