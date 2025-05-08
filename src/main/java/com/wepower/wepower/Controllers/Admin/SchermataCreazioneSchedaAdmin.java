package com.wepower.wepower.Controllers.Admin;

import com.wepower.wepower.ControlloTemi;
import com.wepower.wepower.Models.AdminModel.DatiSessioneAdmin;
import com.wepower.wepower.Models.AdminModel.ModelSchermataCreazioneScheda;
import com.wepower.wepower.Models.Model;
import com.wepower.wepower.Models.SchedaAllenamento.TabellaElencoEsercizi;
import com.wepower.wepower.Views.AlertHelper;
import com.wepower.wepower.Views.SchedaAllenamento.RigaEsercizioListaAdmin;
import com.wepower.wepower.Views.SchedaAllenamento.RigaEsercizioSchedaAdmin;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SchermataCreazioneSchedaAdmin implements Initializable {

    @FXML private SplitPane splitPane;
    @FXML private VBox containerEsercizi;
    @FXML private VBox containerSchedaAllenamento;

    private int idUtente;
    private SchedeAdminController schedeAdminController;

    public SchermataCreazioneSchedaAdmin() {}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.splitPane.setDividerPositions(0.5);
        Model.getInstance().setSchermataCreazioneSchedaAdminController(this);

        try {
            loadEsercizi();
            loadSchedaAllenamento();
        } catch (SQLException ex) {
            AlertHelper.showAlert("Questo non doveva succedere", "Errore durante il caricamento degli esercizi", null, Alert.AlertType.ERROR);
        }
    }

    // carichiamo la scheda di allenamento dell'utente
    public void loadSchedaAllenamento() throws SQLException {
        Label titolo = new Label("Scheda allenamento");
        titolo.getStyleClass().add("label_testo_scuro");
        titolo.getStyleClass().add("titoloContenitore");

        Button confermaScheda = new Button("Conferma scheda");
        confermaScheda.getStyleClass().add("btnScheda");
        ArrayList<RigaEsercizioSchedaAdmin> schedaAllenamento = DatiSessioneAdmin.getEserciziSchedaTemp();
        containerSchedaAllenamento.getChildren().clear();
        containerSchedaAllenamento.setAlignment(Pos.CENTER);

        if (!schedaAllenamento.isEmpty()) {
            containerSchedaAllenamento.getChildren().add(titolo);
            containerSchedaAllenamento.getChildren().add(confermaScheda);
            containerSchedaAllenamento.getChildren().addAll(schedaAllenamento);
        } else {
            Label nessunEsercizio = new Label("Nessun esercizio presente nella scheda. Componila!");
            nessunEsercizio.getStyleClass().add("label_testo_scuro");
            nessunEsercizio.getStyleClass().add("titoloContenitore");
            containerSchedaAllenamento.getChildren().add(nessunEsercizio);
        }

         confermaScheda.setOnAction(e -> {
             try {
                 ModelSchermataCreazioneScheda.onConfermaScheda(idUtente);
             } catch (SQLException ex) {
                 AlertHelper.showAlert("Questo non doveva succedere", "Errroe durante la conferma della scheda", null, Alert.AlertType.ERROR);
             }
             schedeAdminController.aggiornaTabella();
             Stage stage = (Stage) containerSchedaAllenamento.getScene().getWindow();
             stage.close();
         });
    }

    // carichiamo gli esercizi disponibili in palestra
    public void loadEsercizi() throws SQLException {
        Label titolo = new Label("Esercizi disponibili in palestra");
        titolo.getStyleClass().add("label_testo_scuro");
        titolo.getStyleClass().add("titoloContenitore");

        ArrayList<RigaEsercizioListaAdmin> esercizi = TabellaElencoEsercizi.riempiRigaEsercizioAdmin();
        containerEsercizi.getChildren().clear();
        containerEsercizi.getChildren().add(titolo);
        containerEsercizi.getChildren().addAll(esercizi);
        containerEsercizi.setAlignment(Pos.CENTER);
    }

    public void setIdCliente(int idUtente) { this.idUtente = idUtente; }

    public void setSchedeAdminController(SchedeAdminController schedeAdminController) { this.schedeAdminController = schedeAdminController; }

    public static void visualizzaSchermataCreazioneScheda(int idUtente, SchedeAdminController chiamante) throws IOException {
        FXMLLoader loader=new FXMLLoader(SchermataCreazioneSchedaAdmin.class.getResource("/Fxml/Admin/AdminMenuView/SchermataCreazioneScheda.fxml"));
        Parent root=loader.load();

        // mi salvo un'istanza di questo controller in modo tale che possa invocare su di esso i metodi di settaggio del chiamante e dell'id utente
        // l'id utente servirà nella query, il chiamante servirà per chiamare la funzione di aggiornamento della tabella post conferma scheda
        // non posso fare this poichè la funzione è statica
        SchermataCreazioneSchedaAdmin controller=loader.getController();
        controller.setIdCliente(idUtente);
        controller.setSchedeAdminController(chiamante);

        Stage stage=new Stage();
        stage.setTitle("Creazione scheda allenamento");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        Scene scene=new Scene(root);

        String cssTema= SchermataCreazioneSchedaAdmin.class.getResource("/Styles/scheda.css").toExternalForm();
        ControlloTemi.getInstance().aggiungiScena(scene,cssTema);

        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();

        double pct = 0.8;
        stage.setWidth(bounds.getWidth() * pct);
        stage.setHeight(bounds.getHeight() * pct);
        stage.getIcons().add(new Image(SchermataCreazioneSchedaAdmin.class.getResourceAsStream("/Images/favicon.png")));

        stage.setScene(scene);
        stage.centerOnScreen();
        stage.showAndWait();
    }
}