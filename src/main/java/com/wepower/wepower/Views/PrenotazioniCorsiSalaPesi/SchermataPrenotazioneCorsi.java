package com.wepower.wepower.Views.PrenotazioniCorsiSalaPesi;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class SchermataPrenotazioneCorsi extends VBox {
    private VBox contenitoreCorsi;
    private Label titoloContenitore;


    public SchermataPrenotazioneCorsi() {
        this.titoloContenitore = new Label("Prenota il tuo corso!");
        this.getChildren().add(this.titoloContenitore);
    }


    public HBox creaRigaCorso(String nomeCorso,String durataCorso,int prezzo){
        HBox rigaCorso = new HBox();
        Label nomeCorsoLabel = new Label(nomeCorso);
        Label durataCorsoLabel = new Label(durataCorso);
        Label prezzoCorsoLabel = new Label(String.valueOf(prezzo));

        rigaCorso.getChildren().addAll(nomeCorsoLabel, durataCorsoLabel, prezzoCorsoLabel);
        return rigaCorso;
    }
}
