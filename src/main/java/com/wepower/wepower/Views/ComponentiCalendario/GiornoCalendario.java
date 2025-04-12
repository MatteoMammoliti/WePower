package com.wepower.wepower.Views.ComponentiCalendario;
import javafx.scene.control.Button;


public class GiornoCalendario  extends Button {
    private boolean prenotato;
    private String dataPrenotazione;


    public GiornoCalendario(String giornovalore,String dataPrenotazione) {
        super(giornovalore);
        this.dataPrenotazione = dataPrenotazione;
        this.setPrefSize(70, 30);
        this.getStyleClass().add("giornoBottone");
        this.getStylesheets().add(Calendario.class.getResource("/Styles/giornoCalendario.css").toExternalForm());
        this.setOnMouseClicked(mouseEvent -> new FinestraInfoCalendario(this, mouseEvent));}

    public String getDataPrenotazione() {
        return dataPrenotazione;
    }
    public boolean prenotato() {
        return prenotato;
    }


    public void setPrenotato(boolean prenotato) {
        this.prenotato = prenotato;
    }


}
