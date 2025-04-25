package com.wepower.wepower.Views.PrenotazioniCorsiSalaPesi;

import com.wepower.wepower.Models.DatiPalestra.PrenotazioneSalaPesi;
import com.wepower.wepower.Models.DatiSessioneCliente;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;


public class VisualizzatoreProssimiAllenamenti extends VBox {
    private Label dataPrenotazione;
    private Label oraPrenotazione;
    ArrayList<PrenotazioneSalaPesi> prenotazioni = new ArrayList<>();


    public VisualizzatoreProssimiAllenamenti() throws SQLException {
        aggiornaLista();
    }


    void  aggiornaLista() throws SQLException {
        this.getChildren().clear();
        prenotazioni= DatiSessioneCliente.getDateAllenamentiDaFare();

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
        HBox contenitoreRiga=new HBox(5);

        contenitoreRiga.setStyle("-fx-background-color: green;-fx-text-fill: white; -fx-alignment: center;");

        contenitoreRiga.getChildren().addAll(dataPrenotazione,oraPrenotazione);
        return contenitoreRiga;

    }
}

