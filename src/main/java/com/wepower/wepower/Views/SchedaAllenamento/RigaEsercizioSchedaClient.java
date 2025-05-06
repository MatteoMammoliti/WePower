package com.wepower.wepower.Views.SchedaAllenamento;

import com.wepower.wepower.Models.DatiSessioneCliente;
import com.wepower.wepower.Models.SchedaAllenamento.ModelSchedaAllenamentoCliente;
import com.wepower.wepower.Views.AlertHelper;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.io.InputStream;
import java.sql.SQLException;
import java.time.LocalDate;

public class RigaEsercizioSchedaClient extends HBox {
    private Label nomeEsercizio;
    private Label numeroSerie;
    private Label numeroRipetizioni;
    private ImageView imageEsercizio;
    private Label massimaleAttuale;
    private Label dataImpostazioneMassimale;
    private TextField aggiungiNuovoMassimale;
    private Button aggiungiMassimale;
    private Button rimuoviSchedaEsercizio;

    public RigaEsercizioSchedaClient(String nomeEsercizio, String numeroSerie, String numeroRipetizioni, String percorsoImmagine, String massimaleAttuale, String dataImpostazioneMassimale) {

        this.getStyleClass().add("rigaEsercizio");
        this.nomeEsercizio = new Label(nomeEsercizio);
        this.nomeEsercizio.setMaxWidth(250);
        this.nomeEsercizio.setPrefWidth(250);
        this.numeroSerie = new Label("Numero serie: " + numeroSerie);
        this.numeroRipetizioni = new Label("Numero ripetizioni: " + numeroRipetizioni);

        this.massimaleAttuale = new Label("Massimale attuale: " + massimaleAttuale);
        this.dataImpostazioneMassimale = new Label("Data: " + dataImpostazioneMassimale);
        this.dataImpostazioneMassimale.setWrapText(true);

        this.aggiungiNuovoMassimale = new TextField();
        this.aggiungiNuovoMassimale.setPromptText("Aggiungi nuovo massimale");
        this.aggiungiNuovoMassimale.setVisible(false);

        this.nomeEsercizio.getStyleClass().add("label_testo_scuro");
        this.nomeEsercizio.getStyleClass().add("titoloEsercizio");
        this.numeroRipetizioni.getStyleClass().add("label_testo_scuro");
        this.numeroSerie.getStyleClass().add("label_testo_scuro");
        this.numeroSerie.getStyleClass().add("stile_predefinito");
        this.numeroRipetizioni.getStyleClass().add("label_testo_scuro");
        this.numeroRipetizioni.getStyleClass().add("stile_predefinito");
        this.massimaleAttuale.getStyleClass().add("dettagliMassimale");
        this.massimaleAttuale.getStyleClass().add("label_testo_chiaro");
        this.dataImpostazioneMassimale.getStyleClass().add("dettagliMassimale");
        this.dataImpostazioneMassimale.getStyleClass().add("label_testo_chiaro");

        InputStream is = getClass().getResourceAsStream("/" + percorsoImmagine);
        Image image = new Image(is);
        this.imageEsercizio = new ImageView(image);
        this.imageEsercizio.setFitHeight(140);
        this.imageEsercizio.setFitWidth(140);
        this.imageEsercizio.setPreserveRatio(true);

        this.aggiungiMassimale = new Button("Aggiungi massimale");
        this.aggiungiMassimale.setOnAction(event -> {
            this.aggiungiNuovoMassimale.setVisible(true);
            if(Integer.parseInt(this.massimaleAttuale.getText().replace("Massimale attuale: ", "")) > Integer.parseInt(this.aggiungiNuovoMassimale.getText())) {
                AlertHelper.showAlert("Errore", "Il nuovo massimale è minore di quello già presente", null, Alert.AlertType.ERROR );
                this.aggiungiNuovoMassimale.clear();
            } else onAggiungiNuovoMassimale();
        });
        aggiungiMassimale.getStyleClass().add("btnScheda");

        this.rimuoviSchedaEsercizio = new Button("Rimuovi esercizio");
        rimuoviSchedaEsercizio.getStyleClass().add("btnScheda");
        if(DatiSessioneCliente.getSeSchedaRichiesta()) this.rimuoviSchedaEsercizio.setVisible(false);
        this.rimuoviSchedaEsercizio.setOnAction(event -> ModelSchedaAllenamentoCliente.onRimuoviEsercizio(this.nomeEsercizio.getText(), DatiSessioneCliente.getIdSchedaAllenamento()));

        VBox sinistra = new VBox();
        sinistra.getChildren().addAll(this.nomeEsercizio, this.numeroSerie, this.numeroRipetizioni);
        sinistra.setSpacing(10);
        sinistra.setPadding(new Insets(10));

        HBox centro = new HBox();
        VBox centroSinistra = new VBox();
        centroSinistra.getChildren().add(this.imageEsercizio);

        VBox centroDestra = new VBox();
        centroDestra.getChildren().addAll(this.massimaleAttuale, this.dataImpostazioneMassimale);
        centroDestra.setSpacing(10);
        centroDestra.setPadding(new Insets(10));
        centro.getChildren().addAll(centroSinistra, centroDestra);

        VBox destra = new VBox();
        destra.getChildren().addAll(this.aggiungiMassimale, this.aggiungiNuovoMassimale, this.rimuoviSchedaEsercizio);
        destra.setSpacing(10);
        destra.setPadding(new Insets(10));

        this.getChildren().addAll(sinistra, centro, destra);
        this.setSpacing(10);
        this.setPadding(new Insets(10));
    }

    private void onAggiungiNuovoMassimale () {
        try {
            ModelSchedaAllenamentoCliente.onAggiungiNuovoMassimale(DatiSessioneCliente.getIdUtente(),
                    this.nomeEsercizio.getText(), LocalDate.now().toString(), Double.parseDouble(this.aggiungiNuovoMassimale.getText()));
        } catch (SQLException e) {
            ModelSchedaAllenamentoCliente.onUpdateMassimale(Double.parseDouble(this.aggiungiNuovoMassimale.getText()), DatiSessioneCliente.getIdUtente(), this.nomeEsercizio.getText());
        }
    }
}