package com.wepower.wepower.Views.SchedaAllenamento;

import com.wepower.wepower.Models.AdminModel.DatiSessioneAdmin;
import com.wepower.wepower.Models.AdminModel.ModelSchermataCreazioneScheda;
import com.wepower.wepower.Models.Model;
import com.wepower.wepower.Views.AlertHelper;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import java.io.InputStream;
import java.sql.SQLException;

public class RigaEsercizioListaAdmin extends HBox {
    private Label nomeEsercizio;
    private Label descrizioneEsercizio;
    private ImageView imageEsercizio;
    private TextField numeroSerie;
    private TextField numeroRipetizioni;
    private Button aggiungiEsercizioScheda;

    public RigaEsercizioListaAdmin(String nomeEsercizio, String descrizioneEsercizio, String percorsoImmagine) {

        this.nomeEsercizio = new Label(nomeEsercizio);
        this.descrizioneEsercizio = new Label(descrizioneEsercizio);
        this.descrizioneEsercizio.setWrapText(true);

        InputStream is = getClass().getResourceAsStream("/" + percorsoImmagine);
        Image image = new Image(is);
        this.imageEsercizio = new ImageView(image);
        this.imageEsercizio.setFitWidth(200);
        this.imageEsercizio.setFitHeight(200);
        this.imageEsercizio.setPreserveRatio(true);
        this.imageEsercizio.setSmooth(true);

        this.nomeEsercizio.getStyleClass().add("label_testo_scuro");
        this.nomeEsercizio.getStyleClass().add("titoloEsercizio");

        this.descrizioneEsercizio.getStyleClass().add("stile_predefinito");
        this.descrizioneEsercizio.getStyleClass().add("label_testo_chiaro");

        this.getStyleClass().add("rigaEsercizio");

        this.numeroSerie = new TextField();
        this.numeroSerie.setPromptText("Numero serie");

        this.numeroRipetizioni = new TextField();
        this.numeroRipetizioni.setPromptText("Numero ripetizioni");

        this.aggiungiEsercizioScheda = new Button("Aggiungi Esercizio");
        this.aggiungiEsercizioScheda.setOnAction(e -> onInserisci());

        aggiungiEsercizioScheda.getStyleClass().add("btnScheda");

        VBox containerSinistra = new VBox();
        VBox sopra = new VBox();
        sopra.getChildren().add(this.nomeEsercizio);

        VBox sotto = new VBox();
        sotto.getChildren().addAll(this.numeroSerie, this.numeroRipetizioni ,this.aggiungiEsercizioScheda);
        sotto.setSpacing(10);
        sotto.setPadding(new Insets(10));

        containerSinistra.getChildren().addAll(sopra, sotto);

        VBox containerCentrale = new VBox();
        containerCentrale.getChildren().add(imageEsercizio);

        VBox containerDestra = new VBox();
        HBox.setHgrow(containerDestra, Priority.ALWAYS);

        containerDestra.getChildren().add(this.descrizioneEsercizio);

        this.getChildren().addAll(containerSinistra, containerCentrale, containerDestra);
        this.setSpacing(10);
        this.setPadding(new Insets(10));
    }

    public void onInserisci() {

        if (this.numeroSerie.getText().isEmpty() || this.numeroRipetizioni.getText().isEmpty()) {
            AlertHelper.showAlert("Errore", "Compila tutti i campi", null, Alert.AlertType.ERROR );
            return;
        }

        if (!this.numeroSerie.getText().matches("\\d+") || Integer.parseInt(this.numeroSerie.getText()) <= 0) {
            AlertHelper.showAlert("Errore", "Campo serie non valido", null, Alert.AlertType.ERROR );
            return;
        }

        if (!this.numeroRipetizioni.getText().matches("\\d+") || Integer.parseInt(this.numeroRipetizioni.getText()) <= 0) {
            AlertHelper.showAlert("Errore", "Campo ripetizioni non valido", null, Alert.AlertType.ERROR );
            return;
        }

        String percorsoImmagine = ModelSchermataCreazioneScheda.ottieniPercorsoImmagine(this.nomeEsercizio.getText());
        RigaEsercizioSchedaAdmin temp = new RigaEsercizioSchedaAdmin(this.nomeEsercizio.getText(), this.numeroSerie.getText(), this.numeroRipetizioni.getText(), percorsoImmagine);

        if (!DatiSessioneAdmin.addEsercizioInScheda(temp)){
            AlertHelper.showAlert("Errore", "Esercizio già presente nella scheda", null, Alert.AlertType.ERROR );
            return;
        }
        aggiornaUI();
    }

    private void aggiornaUI() {
        try {
            Model.getInstance().getSchermataCreazioneSchedaAdminController().loadSchedaAllenamento();
            Model.getInstance().getSchermataCreazioneSchedaAdminController().loadEsercizi();
        } catch (SQLException e) {
            AlertHelper.showAlert("Questo non doveva succedere", "Qualcosa è andato storto :(", null, Alert.AlertType.ERROR );
        }
    }
}