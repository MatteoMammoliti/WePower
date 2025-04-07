package com.wepower.wepower.Views;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class RigaDashboardAdmin extends HBox {

    private Label idCliente;
    private Label nome;
    private Label cognome;
    private Label statoAbbonamento;
    private Label dataScadenza;
    private Label dataRinnovo;
    private Label sesso;
    private Label email;
    private Label statoCertificato;
    private Button modifica;
    private Button rimuovi;

    public RigaDashboardAdmin(int id, String nome, String cognome, int statoAbbonamento, int statoCertificato, String dataRinnovo, String dataScadenza,String email, String sesso) {

        idCliente = new Label(String.valueOf(id));
        this.nome=new Label(nome);
        this.cognome=new Label(cognome);
        this.dataRinnovo=new Label(String.valueOf(dataRinnovo));
        this.dataScadenza=new Label(String.valueOf(dataScadenza));
        this.email=new Label(email);
        this.sesso=new Label(sesso);
        if (statoAbbonamento==1){
            this.statoAbbonamento=new Label("Attivo");
            this.statoAbbonamento.setStyle("-fx-font-weight: bold");
            this.statoAbbonamento.setStyle("-fx-text-fill: green");
        }
        else{
            this.statoAbbonamento=new Label("Non attivo");
            this.statoAbbonamento.setStyle("-fx-font-weight: bold");
            this.statoAbbonamento.setStyle("-fx-text-fill: red");
        }

        if (statoCertificato==1){
            this.statoCertificato=new Label("OK");
        }
        else{
            this.statoCertificato=new Label("X");
        }

        rimuovi=new Button("rimuovi");
        modifica=new Button("modifica");

        this.getChildren().addAll(
                this.idCliente,
                this.nome,
                this.cognome,
                this.email,
                this.sesso,
                this.statoAbbonamento,
                this.statoCertificato,
                this.dataRinnovo,
                this.dataScadenza,
                this.modifica,
                this.rimuovi
        );
        this.setSpacing(10);
        this.setPadding(new Insets(10));











    }



}
