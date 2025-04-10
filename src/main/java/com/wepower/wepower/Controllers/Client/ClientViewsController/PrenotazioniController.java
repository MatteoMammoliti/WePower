package com.wepower.wepower.Controllers.Client.ClientViewsController;

import com.wepower.wepower.Views.PrenotazioniCorsiSalaPesi.SchermataPrenotazioniCliente;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;


import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class PrenotazioniController implements Initializable {
    @FXML
    private VBox corpoStorico;
    @FXML
    private ScrollPane contenitoreStoricoCliente;
    @FXML
    private HBox containerGiorniPrenotazione;


    public void initialize(URL location, ResourceBundle resources) {
        corpoStorico.setStyle("-fx-background-color: blue;");


        loadPrenotazioni();
    }




    public  void loadPrenotazioni() {
        SchermataPrenotazioniCliente schermata= new SchermataPrenotazioniCliente(LocalDate.now());



        containerGiorniPrenotazione.getChildren().add(schermata);





    }
}
