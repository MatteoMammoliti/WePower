package com.wepower.wepower.Views.PrenotazioniCorsiSalaPesi;

import com.wepower.wepower.Models.DatiPalestra.PrenotazioneSalaPesi;
import com.wepower.wepower.Models.DatiSessioneCliente;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class VisualizzatoreStoricoPrenotazioni extends VBox {
        private Label dataPrenotazione;
        private Label oraPrenotazione;
        ArrayList<PrenotazioneSalaPesi> prenotazioni = new ArrayList<>();


    public VisualizzatoreStoricoPrenotazioni(){
       aggiornaLista();
    }


    void  aggiornaLista(){
        this.getChildren().clear();
        prenotazioni= DatiSessioneCliente.getDateOrariPrenotazioni();

        for (int i=0;i<prenotazioni.size();i++){
            HBox riga=creaRigaStorico(prenotazioni.get(i).getDataPrenotazione(),prenotazioni.get(i).getOrarioPrenotazione());
            this.getChildren().add(riga);
        }
    }
    public HBox creaRigaStorico(String data,String ora){
        LocalDate dataConvertita = LocalDate.parse(data);
        dataPrenotazione=new Label(data);
        oraPrenotazione=new Label(ora);
        dataPrenotazione.setPrefHeight(100);
        dataPrenotazione.setPrefWidth(100);
        oraPrenotazione.setPrefWidth(100);
        oraPrenotazione.setPrefHeight(100);

        if(dataConvertita.isBefore(LocalDate.now())){
            oraPrenotazione.setStyle("-fx-background-color: red;-fx-text-fill: white; -fx-alignment: center;");
            dataPrenotazione.setStyle("-fx-background-color: red;-fx-text-fill: white; -fx-alignment: center;");
        }
        else{
            dataPrenotazione.setStyle("-fx-background-color: green;-fx-text-fill: white; -fx-alignment: center;");
            oraPrenotazione.setStyle("-fx-background-color: green;-fx-text-fill: white; -fx-alignment: center;");
        }
        HBox contenitoreRiga=new HBox(5);

        contenitoreRiga.getChildren().addAll(dataPrenotazione,oraPrenotazione);
        return contenitoreRiga;

    }
}

