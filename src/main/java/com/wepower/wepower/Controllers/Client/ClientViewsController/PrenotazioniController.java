package com.wepower.wepower.Controllers.Client.ClientViewsController;

import com.wepower.wepower.Views.SchermataPrenotazioniCliente;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;


import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class PrenotazioniController implements Initializable {
    @FXML
    private AnchorPane containerGiorniPrenotazione;

    public void initialize(URL location, ResourceBundle resources) {
        loadPrenotazioni();
    }




    public  void loadPrenotazioni() {
        SchermataPrenotazioniCliente schermata= new SchermataPrenotazioniCliente(LocalDate.now());
        containerGiorniPrenotazione.getChildren().add(schermata);


    }
}
