package com.wepower.wepower.Views.ComponentiCalendario;

import com.wepower.wepower.ControlloTemi;
import com.wepower.wepower.Models.DatiSessioneCliente;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;

import javafx.scene.input.MouseEvent;

public class FinestraInfoCalendario {
    private final GiornoCalendario giorno;
    private final MouseEvent event;
    Popup popup;
    Scene scene;
    Label oraPrenotazione;
    Label informazione;
    VBox layout;
    public FinestraInfoCalendario(GiornoCalendario giorno, MouseEvent event) {
        this.giorno=giorno;
        this.event=event;

        if (giorno.prenotato()){
            String orario=DatiSessioneCliente.getOrarioPrenotazione(giorno.getDataPrenotazione());
            oraPrenotazione = new Label(orario);
            Label informazione = new Label("Prenotato alle ore: " );
            layout = new VBox(5,informazione,oraPrenotazione);
            scene = new Scene(layout);
        }else if(giorno.isChiuso()){
            informazione = new Label("Chiuso");
            layout = new VBox(5,informazione);
            scene = new Scene(layout);
        }

    }
    public void mostra(){
        if (layout==null){
            return;
        }
        popup = new Popup();
        popup.getContent().add(layout);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #DDE6ED; -fx-padding: 8; -fx-background-radius: 30;   -fx-opacity: 0.9;-fx-text-fill: #27374D;");
        popup.setAutoHide(true);
        popup.show(giorno.getScene().getWindow(), event.getScreenX(), event.getScreenY());
    }


}
