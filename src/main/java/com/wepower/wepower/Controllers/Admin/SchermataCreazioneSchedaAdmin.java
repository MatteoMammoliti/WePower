package com.wepower.wepower.Controllers.Admin;

import com.wepower.wepower.Models.AdminModel.DatiSessioneAdmin;
import com.wepower.wepower.Models.AdminModel.ModelSchermataCreazioneScheda;
import com.wepower.wepower.Models.Model;
import com.wepower.wepower.Models.SchedaAllenamento.TabellaElencoEsercizi;
import com.wepower.wepower.Views.SchedaAllenamento.RigaEsercizioListaAdmin;
import com.wepower.wepower.Views.SchedaAllenamento.RigaEsercizioSchedaAdmin;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SchermataCreazioneSchedaAdmin implements Initializable {

    @FXML
    private SplitPane splitPane;

    @FXML
    private VBox containerEsercizi;

    @FXML
    private VBox containerSchedaAllenamento;

    private int idUtente;
    private SchedeAdminController  schedeAdminController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.splitPane.setDividerPositions(0.5);
        Model.getInstance().setSchermataCreazioneSchedaAdminController(this);
        try {
            loadEsercizi();
            loadSchedaAllenamento();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void loadSchedaAllenamento() throws SQLException {
        Label titolo = new Label("Scheda allenamento");
        Button confermaScheda = new Button("Conferma scheda");
        ArrayList<RigaEsercizioSchedaAdmin> schedaAllenamento = DatiSessioneAdmin.getEserciziSchedaTemp();
        containerSchedaAllenamento.getChildren().clear();

        if (!schedaAllenamento.isEmpty()) {
            containerSchedaAllenamento.getChildren().add(titolo);
            containerSchedaAllenamento.getChildren().add(confermaScheda);
            containerSchedaAllenamento.getChildren().addAll(schedaAllenamento);
        } else {
            Label nessunEsercizio = new Label("Nessun esercizio presente nella scheda. Componila!");
            containerSchedaAllenamento.getChildren().add(nessunEsercizio);
        }

         confermaScheda.setOnAction(e -> {
             ModelSchermataCreazioneScheda.onConfermaScheda(idUtente);
             schedeAdminController.aggiornaTabbella();
             Stage stage = (Stage) containerSchedaAllenamento.getScene().getWindow();
             stage.close();
         });
    }

    public void loadEsercizi() throws SQLException {
        Label titolo = new Label("Esercizi disponibili in palestra");
        ArrayList<RigaEsercizioListaAdmin> esercizi = TabellaElencoEsercizi.riempiRigaEsercizioAdmin();
        containerEsercizi.getChildren().clear();
        containerEsercizi.getChildren().add(titolo);
        containerEsercizi.getChildren().addAll(esercizi);
    }

    public void setIdCliente(int idUtente) { this.idUtente = idUtente; }

    public void setSchedeAdminController(SchedeAdminController schedeAdminController) { this.schedeAdminController = schedeAdminController; }

    public static void visualizzaSchermataCreazioneScheda(int idUtente, SchedeAdminController chiamante) throws IOException {
        FXMLLoader loader=new FXMLLoader(VisualizzatoreCertificatoController.class.getResource("/Fxml/Admin/AdminMenuView/SchermataCreazioneScheda.fxml"));
        Parent root=loader.load();

        SchermataCreazioneSchedaAdmin controller=loader.getController();
        controller.setIdCliente(idUtente);
        controller.setSchedeAdminController(chiamante);

        Stage stage=new Stage();
        stage.setTitle("Creazione scheda allenamento");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        Scene scene=new Scene(root);
        stage.setScene(scene);
        stage.showAndWait();
    }
}
