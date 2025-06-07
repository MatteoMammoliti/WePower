package com.wepower.wepower.Controllers.Client.ClientViewsController;

import com.wepower.wepower.ControlloTemi;
import com.wepower.wepower.Models.AdminModel.TabellaUtentiDashboardAdmin;
import com.wepower.wepower.Models.DatiSessioneCliente;
import com.wepower.wepower.Models.Model;
import com.wepower.wepower.Models.SchedaAllenamento.ModelSchedaAllenamentoCliente;
import com.wepower.wepower.Models.SchedaAllenamento.TabellaElencoEsercizi;
import com.wepower.wepower.Views.AdminView.RigaUtentiAdmin;
import com.wepower.wepower.Views.AlertHelper;
import com.wepower.wepower.Views.SchedaAllenamento.RigaEsercizioListaClient;
import com.wepower.wepower.Views.SchedaAllenamento.RigaEsercizioSchedaClient;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class SchedaController implements Initializable {

    @FXML private SplitPane splitPane;
    @FXML private Button eliminaSchedaButton;
    @FXML private VBox containerSchedaAllenamento;
    @FXML private VBox containerEsercizi;

    public SchedaController() {}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().setSchedaController(this);

        this.splitPane.setDividerPositions(0.5);

        if(DatiSessioneCliente.getSeSchedaRichiesta()) {
            // se l'utente ha richiesto la scheda non gli sarà possibile aggiungere esercizi ad essa
            this.containerEsercizi.setVisible(false);
            this.containerEsercizi.setManaged(false);
        }

        // carico gli esercizi e la scheda dal db
        try {
            if(!DatiSessioneCliente.getSeSchedaRichiesta())  loadEsercizi();
            loadSchedaAllenamento();
        } catch (SQLException e) {
            AlertHelper.showAlert("Questo non doveva succedere", "Qualcosa è andato storto :(", null, Alert.AlertType.ERROR);
        }

        this.eliminaSchedaButton.setOnAction(e -> {
            Alert conferma = new Alert(Alert.AlertType.CONFIRMATION);
            conferma.setTitle("Conferma eliminazione");
            conferma.setHeaderText("Sei sicuro di voler eliminare questa scheda?");
            ImageView icon = new ImageView(new Image(DatiSessioneCliente.class.getResourceAsStream("/Images/IconeAlert/question.png")));
            DialogPane dialogPane = conferma.getDialogPane();
            dialogPane.getStylesheets().add(DatiSessioneCliente.class.getResource("/Styles/alertStyle.css").toExternalForm());
            Stage alertStage = (Stage) dialogPane.getScene().getWindow();
            alertStage.getIcons().add(new Image(getClass().getResource("/Images/favicon.png").toExternalForm()));
            dialogPane.getStylesheets().add(ControlloTemi.getInstance().getCssTemaCorrente());
            conferma.setGraphic(icon);

            Optional<ButtonType> resultConferma = conferma.showAndWait();
            if (resultConferma.isPresent() && resultConferma.get() == ButtonType.OK) {
                try {
                    ModelSchedaAllenamentoCliente.eliminaSchedaAllenamento(DatiSessioneCliente.getIdSchedaAllenamento());
                } catch (SQLException ex) {
                    AlertHelper.showAlert("Questo non doveva succedere", "Qualcosa è andato storto :(", null, Alert.AlertType.ERROR);
                }
            }
        });
    }

    public void loadSchedaAllenamento() throws SQLException {
        Label titolo = new Label("Scheda allenamento");
        titolo.getStyleClass().add("label_testo_scuro");
        titolo.getStyleClass().add("titoloContenitore");

        ArrayList<RigaEsercizioSchedaClient> schedaAllenamento = TabellaElencoEsercizi.riempiRigaEsercizioScheda();
        containerSchedaAllenamento.getChildren().clear();
        containerSchedaAllenamento.setAlignment(Pos.CENTER);

        if (!schedaAllenamento.isEmpty()) {
            containerSchedaAllenamento.getChildren().add(titolo);
            containerSchedaAllenamento.getChildren().addAll(schedaAllenamento);
        } else {
            Label nessunEsercizio = new Label("");
            nessunEsercizio.setWrapText(true);
            nessunEsercizio.getStyleClass().add("label_testo_scuro");
            nessunEsercizio.getStyleClass().add("titoloContenitore");
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
        titolo.getStyleClass().add("label_testo_scuro");
        titolo.getStyleClass().add("titoloContenitore");
        ArrayList<RigaEsercizioListaClient> esercizi = TabellaElencoEsercizi.riempiRigaEsercizio();
        containerEsercizi.getChildren().clear();
        containerEsercizi.getChildren().add(titolo);
        containerEsercizi.getChildren().addAll(esercizi);
        containerEsercizi.setAlignment(Pos.CENTER);
    }
}