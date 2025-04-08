package com.wepower.wepower.Views.SchedaAllenamento;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import java.io.InputStream;

public class EsercizioPerLista extends HBox {
    private Label nomeEsercizio;
    private Label descrizioneEsercizio;
    private Label muscoloAllenato;
    private ImageView imageEsercizio;
    private Button aggiungiEsercizioScheda;

    public EsercizioPerLista(String nomeEsercizio, String descrizioneEsercizio, String muscoloAllenato, String percorsoImmagine) {
        this.nomeEsercizio = new Label(nomeEsercizio);
        this.descrizioneEsercizio = new Label(descrizioneEsercizio);
        this.muscoloAllenato = new Label("Muscolo allenato: " + muscoloAllenato);

        InputStream is = getClass().getResourceAsStream("/" + percorsoImmagine);

        Image image = new Image(is);
        this.imageEsercizio = new ImageView(image);
        this.imageEsercizio.setFitHeight(100);
        this.imageEsercizio.setFitWidth(100);
        this.imageEsercizio.setPreserveRatio(true);

        this.aggiungiEsercizioScheda = new Button("Aggiungi EsercizioPerLista");
        this.aggiungiEsercizioScheda.setOnAction(e -> onAggiungiEsercizioScheda());

        this.getChildren().addAll(this.nomeEsercizio, this.descrizioneEsercizio, this.muscoloAllenato, this.imageEsercizio, this.aggiungiEsercizioScheda);
        this.setSpacing(10);
        this.setPadding(new Insets(10));
    }

    private void onAggiungiEsercizioScheda() {
    }
}

