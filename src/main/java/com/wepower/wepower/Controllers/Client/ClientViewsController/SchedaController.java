package com.wepower.wepower.Controllers.Client.ClientViewsController;

import com.wepower.wepower.Models.ConnessioneDatabase;
import com.wepower.wepower.Models.DatiSessioneCliente;
import com.wepower.wepower.Models.Model;
import com.wepower.wepower.Models.SchedaAllenamento.ModelSchedaAllenamentoCliente;
import com.wepower.wepower.Models.SchedaAllenamento.TabellaElencoEsercizi;
import com.wepower.wepower.Views.SchedaAllenamento.RigaEsercizioLista;
import com.wepower.wepower.Views.SchedaAllenamento.RigaEsercizioScheda;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SchedaController implements Initializable {

    @FXML
    private SplitPane splitPane;

    @FXML
    private Button eliminaSchedaButton;

    @FXML
    private VBox containerSchedaAllenamento;

    @FXML
    private VBox containerEsercizi;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().setSchedaController(this);

        this.splitPane.setDividerPositions(0.5);

        if(DatiSessioneCliente.getSeSchedaRichiesta()) {
            this.containerEsercizi.setVisible(false);
            this.containerEsercizi.setManaged(false);
        }

        try {
            if(!DatiSessioneCliente.getSeSchedaRichiesta())  loadEsercizi();
            loadSchedaAllenamento();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        this.eliminaSchedaButton.setOnAction(e -> {
            try {
                ModelSchedaAllenamentoCliente.eliminaSchedaAllenamento(DatiSessioneCliente.getIdSchedaAllenamento());
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    public void loadSchedaAllenamento() throws SQLException {
        Label titolo = new Label("Scheda allenamento");
        ArrayList<RigaEsercizioScheda> schedaAllenamento = TabellaElencoEsercizi.riempiRigaEsercizioScheda();
        containerSchedaAllenamento.getChildren().clear();

        if (!schedaAllenamento.isEmpty()) {
            containerSchedaAllenamento.getChildren().add(titolo);
            containerSchedaAllenamento.getChildren().addAll(schedaAllenamento);
        } else {

            Label nessunEsercizio = new Label("");
            if(!DatiSessioneCliente.getSeSchedaRichiesta()) {
                nessunEsercizio.setText("Nessun esercizio presente nella scheda. Componila!");
            } else {
                nessunEsercizio.setText("La tua scheda non è stata ancora processata dall'admin. Attendi!");
            }
            containerSchedaAllenamento.getChildren().add(nessunEsercizio);
        }
    }

    public void loadEsercizi() throws SQLException {
        Label titolo = new Label("Esercizi disponibili in palestra");
        ArrayList<RigaEsercizioLista> esercizi = TabellaElencoEsercizi.riempiRigaEsercizio();
        containerEsercizi.getChildren().clear();
        containerEsercizi.getChildren().add(titolo);
        containerEsercizi.getChildren().addAll(esercizi);
    }
}