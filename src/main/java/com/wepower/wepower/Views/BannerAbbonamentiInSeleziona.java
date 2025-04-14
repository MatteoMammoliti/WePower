package com.wepower.wepower.Views;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class BannerAbbonamentiInSeleziona extends HBox {
    private String nomeAbbonamento;
    private String descrizioneAbbonamento;
    private int prezzoAbbonamento;
    private String durataAbbonamento;

    public BannerAbbonamentiInSeleziona(String nome,String descrizione,int prezzo,String durata) {
        nomeAbbonamento = nome;
        descrizioneAbbonamento = descrizione;
        prezzoAbbonamento = prezzo;
        durataAbbonamento = durata;

        VBox informazioniAbbonamento = new VBox(10);
        informazioniAbbonamento.setPrefWidth(500);

        Label nomeAbb=new Label(nomeAbbonamento);
        VBox.setMargin(nomeAbb,new Insets(20,0,0,20));
        nomeAbb.getStyleClass().add("labelNome");

        Label descrizioneAbb=new Label(descrizioneAbbonamento);
        VBox.setMargin(descrizioneAbb,new Insets(10,0,0,20));
        descrizioneAbb.getStyleClass().add("labelDescrizione");

        informazioniAbbonamento.getChildren().addAll(nomeAbb,descrizioneAbb);
        this.getChildren().addAll(informazioniAbbonamento);

        VBox prezzoEBtn = new VBox(10);
        prezzoEBtn.setPrefWidth(500);
        Label prezzoAbb=new Label("Prezzo:"+prezzoAbbonamento+"€");
        VBox.setMargin(prezzoAbb,new Insets(20,0,0,70));
        prezzoEBtn.getStyleClass().add("labelPrezzo");

        Button abbonatiBtn=new Button("Abbonati");
        VBox.setMargin(abbonatiBtn,new Insets(15,0,0,90));

        prezzoEBtn.getChildren().addAll(prezzoAbb,abbonatiBtn);

        this.getChildren().addAll(prezzoEBtn);
        this.getStyleClass().add("contenitoreRiga");


    }


}
