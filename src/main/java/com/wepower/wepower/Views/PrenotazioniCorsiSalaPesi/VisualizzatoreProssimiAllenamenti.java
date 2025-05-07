package com.wepower.wepower.Views.PrenotazioniCorsiSalaPesi;

import com.wepower.wepower.Models.DatiPalestra.PrenotazioneSalaPesi;
import com.wepower.wepower.Models.DatiSessioneCliente;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;


public class VisualizzatoreProssimiAllenamenti extends VBox {
    private Label dataPrenotazione;
    private Label oraPrenotazione;
    ArrayList<PrenotazioneSalaPesi> prenotazioni = new ArrayList<>();

    public VisualizzatoreProssimiAllenamenti() throws SQLException {
        this.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/Styles/Prenotazioni.css")).toExternalForm());
        aggiornaLista();
        this.setSpacing(10);
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
        HBox contenitoreRiga=new HBox(10);

        dataPrenotazione.getStyleClass().add("labelInfoStorico");
        oraPrenotazione.getStyleClass().add("labelInfoStorico");

        contenitoreRiga.getStyleClass().add("contenitoreRigaProssimiAllenamenti");
        contenitoreRiga.getStyleClass().add("contenitoreRigaStoricoProssimi");
        contenitoreRiga.setAlignment(Pos.CENTER);

        contenitoreRiga.getChildren().addAll(dataPrenotazione,oraPrenotazione);
        return contenitoreRiga;
    }
}

