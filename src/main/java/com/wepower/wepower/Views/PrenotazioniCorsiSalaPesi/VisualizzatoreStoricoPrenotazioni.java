package com.wepower.wepower.Views.PrenotazioniCorsiSalaPesi;

import com.wepower.wepower.Models.DatiPalestra.PrenotazioneSalaPesi;
import com.wepower.wepower.Models.DatiSessioneCliente;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;


public class VisualizzatoreStoricoPrenotazioni extends VBox {
        private Label dataPrenotazione;
        private Label oraPrenotazione;
        ArrayList<PrenotazioneSalaPesi> prenotazioni = new ArrayList<>();


    public VisualizzatoreStoricoPrenotazioni(){
        this.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/Styles/Prenotazioni.css")).toExternalForm());

        this.setSpacing(10);
       aggiornaLista();
    }


    void  aggiornaLista(){
        this.getChildren().clear();
        prenotazioni= DatiSessioneCliente.getDateAllenamentiEffettuati();

        for (PrenotazioneSalaPesi prenotazioneSalaPesi : prenotazioni) {
            HBox riga = creaRigaStorico(prenotazioneSalaPesi.getDataPrenotazione(), prenotazioneSalaPesi.getOrarioPrenotazione());
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

        dataPrenotazione.getStyleClass().add("labelInfoStorico");
        oraPrenotazione.getStyleClass().add("labelInfoStorico");
        contenitoreRiga.getStyleClass().add("contenitoreRigaStorico");
        contenitoreRiga.getStyleClass().add("contenitoreRigaStoricoProssimi");

        contenitoreRiga.getChildren().addAll(dataPrenotazione,oraPrenotazione);
        return contenitoreRiga;

    }
}

