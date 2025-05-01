package com.wepower.wepower.Views.Bannerini;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class BannerAbbonamentiInSeleziona extends HBox {
    private String nomeAbbonamento;
    private String descrizioneAbbonamento;
    private int prezzoAbbonamento;
    private String durataAbbonamento;

    public BannerAbbonamentiInSeleziona(String nome, String descrizione, int prezzo, String durata, EventHandler<ActionEvent> clickAbbonati) {
        nomeAbbonamento = nome;
        descrizioneAbbonamento = descrizione;
        prezzoAbbonamento = prezzo;
        durataAbbonamento = durata;

        VBox informazioniAbbonamento = new VBox(10);
        informazioniAbbonamento.setPrefWidth(500);

        Label nomeAbb=new Label(nomeAbbonamento);
        nomeAbb.getStyleClass().add("labelNome");

        Label descrizioneAbb=new Label(descrizioneAbbonamento);
        descrizioneAbb.getStyleClass().add("labelDescrizione");

        Label prezzoAbb=new Label("Prezzo: "+prezzoAbbonamento+"€");
        prezzoAbb.getStyleClass().add("labelPrezzo");

        informazioniAbbonamento.setSpacing(15);
        informazioniAbbonamento.setPadding(new Insets(10, 10, 10, 10));
        informazioniAbbonamento.getChildren().addAll(nomeAbb,descrizioneAbb, prezzoAbb);
        this.getChildren().addAll(informazioniAbbonamento);

        VBox tastoAbbonati = new VBox(10);
        tastoAbbonati.setPrefWidth(500);
        tastoAbbonati.setAlignment(Pos.CENTER);

        Button abbonatiBtn=new Button("Abbonati");
        abbonatiBtn.setOnAction(clickAbbonati);
        abbonatiBtn.getStyleClass().add("buttonAbbonati");
        VBox.setMargin(abbonatiBtn,new Insets(15,0,0,90));

        tastoAbbonati.getChildren().addAll(abbonatiBtn);

        this.getChildren().addAll(tastoAbbonati);
        this.getStyleClass().add("contenitoreRiga");
    }
}