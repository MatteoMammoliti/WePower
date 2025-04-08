package com.wepower.wepower.Views.SchedaAllenamento;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.io.InputStream;

public class Esercizio extends HBox {
    private Label nomeEsercizio;
    private Label descrizioneEsercizio;
    private Label muscoloAllenato;
    private ImageView imageEsercizio;

    public Esercizio(String nomeEsercizio, String descrizioneEsercizio, String muscoloAllenato, String percorsoImmagine) {
        this.nomeEsercizio = new Label(nomeEsercizio);
        this.descrizioneEsercizio = new Label(descrizioneEsercizio);
        this.muscoloAllenato = new Label("Muscolo allenato: " + muscoloAllenato);

        // Aggiungi debug per il percorso
        System.out.println("Caricamento immagine: /" + percorsoImmagine);
        InputStream is = getClass().getResourceAsStream("/" + percorsoImmagine);
        if (is == null) {
            System.err.println("Immagine non trovata: /" + percorsoImmagine);
        }
        Image image = new Image(is);
        this.imageEsercizio = new ImageView(image);
        this.imageEsercizio.setFitHeight(100);
        this.imageEsercizio.setFitWidth(100);
        this.imageEsercizio.setPreserveRatio(true);

        this.getChildren().addAll(this.nomeEsercizio, this.descrizioneEsercizio, this.muscoloAllenato, this.imageEsercizio);
        this.setSpacing(10);
        this.setPadding(new Insets(10));
    }
}

