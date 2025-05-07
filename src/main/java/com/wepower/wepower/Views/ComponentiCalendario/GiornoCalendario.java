package com.wepower.wepower.Views.ComponentiCalendario;
import javafx.scene.control.Button;

import java.util.concurrent.atomic.AtomicReference;


public class GiornoCalendario  extends Button {
    private boolean prenotato;
    private String dataPrenotazione;
    private boolean chiuso;
    private FinestraInfoCalendario finestra;

    public GiornoCalendario(String giornovalore,String dataPrenotazione) {
        super(giornovalore);
        this.dataPrenotazione = dataPrenotazione;
        this.setPrefSize(70, 30);
        this.getStyleClass().add("button_calendario");
        this.getStylesheets().add(Calendario.class.getResource("/Styles/Dashboard.css").toExternalForm());



        this.setOnMouseClicked(mouseEvent -> {
            finestra=new FinestraInfoCalendario(this, mouseEvent);
            finestra.mostra();
        });
    }

    public String getDataPrenotazione() {
        return dataPrenotazione;
    }
    public boolean prenotato() {
        return prenotato;
    }
    public boolean isChiuso() {
        return chiuso;
    }

    public void setPrenotato(boolean prenotato) {
        this.prenotato = prenotato;
    }
    public void setChiusura(boolean chiuso) {this.chiuso = chiuso;}
}











