package com.wepower.wepower.Views.SchedaAllenamento;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.io.InputStream;

public class EsercizioPerScheda extends HBox {
    private Label nomeEsercizio;
    private Label descrizioneEsercizio;
    private Label muscoloAllenato;
    private Label numeroSerie;
    private Label numeroRipetizioni;
    private ImageView imageEsercizio;
    private Button rimuoviSchedaEsercizio;


    public EsercizioPerScheda(String nomeEsercizio, String descrizioneEsercizio, String muscoloAllenato, String numeroSerie, String numeroRipetizioni, String percorsoImmagine) {
        this.nomeEsercizio = new Label(nomeEsercizio);
        this.descrizioneEsercizio = new Label(descrizioneEsercizio);
        this.muscoloAllenato = new Label("Muscolo allenato: " + muscoloAllenato);
        this.numeroSerie = new Label(numeroSerie);
        this.numeroRipetizioni = new Label(numeroRipetizioni);

        InputStream is = getClass().getResourceAsStream("/" + percorsoImmagine);

        Image image = new Image(is);
        this.imageEsercizio = new ImageView(image);
        this.imageEsercizio.setFitHeight(100);
        this.imageEsercizio.setFitWidth(100);
        this.imageEsercizio.setPreserveRatio(true);

        this.rimuoviSchedaEsercizio = new Button("Rimuovi esercizio dalla scheda");

        this.getChildren().addAll(this.nomeEsercizio, this.descrizioneEsercizio, this.muscoloAllenato, this.numeroSerie, this.numeroRipetizioni, this.imageEsercizio, this.rimuoviSchedaEsercizio);
        this.setSpacing(10);
        this.setPadding(new Insets(10));
    }
}