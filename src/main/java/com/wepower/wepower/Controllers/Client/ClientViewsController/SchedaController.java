package com.wepower.wepower.Controllers.Client.ClientViewsController;

import com.wepower.wepower.Models.TabellaElencoEsercizi;
import com.wepower.wepower.Views.SchedaAllenamento.Esercizio;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SchedaController implements Initializable {

    @FXML
    private VBox containerEsercizi;

    private void loadEsercizi() throws SQLException {
        ArrayList<Esercizio> esercizi = TabellaElencoEsercizi.riempiRigaEsercizio();
        containerEsercizi.getChildren().clear();
        containerEsercizi.getChildren().addAll(esercizi);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadEsercizi();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
