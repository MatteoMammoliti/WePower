package com.wepower.wepower.Views.SchedaAllenamento;

import com.wepower.wepower.Models.DatiSessioneCliente;
import com.wepower.wepower.Models.Model;
import com.wepower.wepower.Models.SchedaAllenamento.ModelSchedaAllenamentoCliente;
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

public class RigaEsercizioListaClient extends HBox {
    private Label nomeEsercizio;
    private Label descrizioneEsercizio;
    private ImageView imageEsercizio;
    private TextField numeroSerie;
    private TextField numeroRipetizioni;
    private Button aggiungiEsercizioScheda;

    public RigaEsercizioListaClient(String nomeEsercizio, String descrizioneEsercizio, String percorsoImmagine) {

        this.nomeEsercizio = new Label(nomeEsercizio);
        this.descrizioneEsercizio = new Label(descrizioneEsercizio);
        this.descrizioneEsercizio.setWrapText(true);

        this.nomeEsercizio.getStyleClass().add("titoloEsercizio");
        this.nomeEsercizio.setMaxWidth(250);
        this.nomeEsercizio.setPrefWidth(250);
        this.descrizioneEsercizio.setMaxWidth(250);
        this.descrizioneEsercizio.setPrefWidth(250);
        this.descrizioneEsercizio.getStyleClass().add("descrizioneEsercizio");

        this.getStyleClass().add("rigaEsercizio");
        InputStream is = getClass().getResourceAsStream("/" + percorsoImmagine);
        Image image = new Image(is);
        this.imageEsercizio = new ImageView(image);
        this.imageEsercizio.setFitWidth(140);
        this.imageEsercizio.setFitHeight(140);
        this.imageEsercizio.setPreserveRatio(true);
        this.imageEsercizio.setSmooth(true);

        this.numeroSerie = new TextField();
        this.numeroSerie.setPromptText("Numero serie");

        this.numeroRipetizioni = new TextField();
        this.numeroRipetizioni.setPromptText("Numero ripetizioni");

        this.aggiungiEsercizioScheda = new Button("Aggiungi Esercizio");
        this.aggiungiEsercizioScheda.setOnAction(e -> {
            try {
                onInserisci();
            } catch (Exception ex) {
                AlertHelper.showAlert("Errore", "Esercizio già presente nella scheda", null, Alert.AlertType.ERROR );
            }
            aggiornaUI();
        });
        aggiungiEsercizioScheda.getStyleClass().add("btnScheda");

        VBox containerSinistra = new VBox();
        VBox sopra = new VBox();
        sopra.getChildren().add(this.nomeEsercizio);

        VBox sotto = new VBox();
        sotto.getChildren().addAll(this.numeroSerie,this.numeroRipetizioni, this.aggiungiEsercizioScheda);
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
        ModelSchedaAllenamentoCliente.inserisciEsercizioScheda(this.nomeEsercizio.getText(), DatiSessioneCliente.getIdSchedaAllenamento(), this.numeroRipetizioni.getText(), this.numeroSerie.getText());
    }

    private void aggiornaUI() {
        try {
            Model.getInstance().getSchedaController().loadSchedaAllenamento();
            Model.getInstance().getSchedaController().loadEsercizi();
        } catch (SQLException e) {
            AlertHelper.showAlert("Questo non doveva succedere", "Qualcosa è andato storto :(", null, Alert.AlertType.ERROR );
        }
    }
}