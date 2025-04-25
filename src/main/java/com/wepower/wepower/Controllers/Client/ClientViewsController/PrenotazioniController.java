package com.wepower.wepower.Controllers.Client.ClientViewsController;

import com.wepower.wepower.Views.PrenotazioniCorsiSalaPesi.SchermataPrenotazioniCliente;
import com.wepower.wepower.Views.PrenotazioniCorsiSalaPesi.VisualizzatoreProssimiAllenamenti;
import com.wepower.wepower.Views.PrenotazioniCorsiSalaPesi.VisualizzatoreStoricoPrenotazioni;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.ResourceBundle;

public class PrenotazioniController implements Initializable {
    @FXML
    private ScrollPane scrollPrenotazione;
    @FXML
    private ScrollPane scrollProssimiAllenamenti;
    @FXML
    private ScrollPane scrollStoricoCliente;
    @FXML
    private Label labelStorico;
    @FXML
    private Label labelProssimiAllenamenti;
    @FXML
    private VBox corpoProssimiAllenamenti;
    @FXML
    private VBox corpoStorico;

    @FXML
    private HBox containerGiorniPrenotazione;


    public void initialize(URL location, ResourceBundle resources) {


        try {
            loadPrenotazioni();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }




    public  void loadPrenotazioni() throws SQLException {
        VisualizzatoreStoricoPrenotazioni storico=new VisualizzatoreStoricoPrenotazioni();
        VisualizzatoreProssimiAllenamenti prossimiAllenamenti=new VisualizzatoreProssimiAllenamenti();
        SchermataPrenotazioniCliente schermata= new SchermataPrenotazioniCliente(LocalDate.now(),storico,prossimiAllenamenti);



        containerGiorniPrenotazione.getChildren().add(schermata);
        corpoProssimiAllenamenti.getChildren().add(prossimiAllenamenti);
        corpoStorico.getChildren().add(storico);





    }


}
