package com.wepower.wepower.Views.ComponentiCalendario;

import com.wepower.wepower.Models.DatiSessioneCliente;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;

import javafx.scene.input.MouseEvent;

public class FinestraInfoCalendario {
    Popup popup;
    Scene scene;
    Label oraPrenotazione;
    Label informazione;
    VBox layout;
    public FinestraInfoCalendario(GiornoCalendario giorno, MouseEvent event) {
        if (giorno.prenotato()){
            String orario=DatiSessioneCliente.getOrarioPrenotazione(giorno.getDataPrenotazione());
            oraPrenotazione = new Label(orario);
            Label informazione = new Label("Prenotato per l'ora: " );
            layout = new VBox(10,informazione,oraPrenotazione);
            scene = new Scene(layout);
        }else{
            informazione= new Label("Non prenotato");
            layout = new VBox(10,informazione);
            scene = new Scene(layout);

        }
        popup = new Popup();
        popup.getContent().add(layout);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-pref-width: 200; -fx-pref-height: 100; -fx-background-color: white; -fx-padding: 10; -fx-background-radius: 50; -fx-border-color: black; -fx-border-radius: 50; -fx-opacity: 0.9;");
        popup.setAutoHide(true);
        popup.show(giorno.getScene().getWindow(), event.getScreenX(), event.getScreenY());
    }
}
