package com.wepower.wepower.Controllers.Admin;

import com.wepower.wepower.Models.DatiPalestra.DatiSessionePalestra;
import com.wepower.wepower.Models.Model;
import com.wepower.wepower.Views.AdminView.RigaTabellaRichiesteScheda;
import com.wepower.wepower.Views.AlertHelper;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.util.ResourceBundle;

public class SchedeAdminController implements Initializable {

    @FXML private TableView<RigaTabellaRichiesteScheda> tabellaSchede;
    @FXML private TableColumn<RigaTabellaRichiesteScheda, Void> colCrea;
    @FXML private TableColumn<RigaTabellaRichiesteScheda, String> colSesso;
    @FXML private TableColumn<RigaTabellaRichiesteScheda, Integer> colPeso;
    @FXML private TableColumn<RigaTabellaRichiesteScheda, String> colCognome;
    @FXML private TableColumn<RigaTabellaRichiesteScheda, String> colNome;
    @FXML private TableColumn<RigaTabellaRichiesteScheda, Integer> colID;

    public SchedeAdminController() {}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().setSchedeAdminController(this);
        colID.setCellValueFactory(new PropertyValueFactory<>("idUtente"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colCognome.setCellValueFactory(new PropertyValueFactory<>("cognome"));
        colPeso.setCellValueFactory(new PropertyValueFactory<>("peso"));
        colSesso.setCellValueFactory(new PropertyValueFactory<>("sesso"));

        //Carico i dati delle richieste di scheda
        aggiornaTabella();
        aggiungiBottone();
    }

    public void aggiornaTabella() {
        ObservableList<RigaTabellaRichiesteScheda> utenti = DatiSessionePalestra.getRichiesteScheda();
        tabellaSchede.setItems(utenti);
    }

    private void aggiungiBottone() {
        colCrea.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("Visualizza");
            {
                btn.setOnAction(event -> {
                    RigaTabellaRichiesteScheda riga = getTableView().getItems().get(getIndex());
                    int id = riga.getIdUtente();

                    //Quando clicchiamo sul bottone, chiamiamo il metodo onClickVisualizza e gli passiamo l'id del cliente
                    try {
                        SchermataCreazioneSchedaAdmin.visualizzaSchermataCreazioneScheda(id,  Model.getInstance().getSchedeAdminController());
                    } catch (Exception e) {
                        AlertHelper.showAlert("Questo non doveva succedere", "Errore durante l'apertura della pagina", null, Alert.AlertType.ERROR);
                    }
                });
            }

            //Sovrascriviamo un metodo della classe TableCell.
            //Questo metodo viene chiamato automaticamante da javaFX ogni volta che la tabella si aggiorna.
            //empty è true se la riga è vuota.
            //In questo modo ogni cella della colonna colVisualizza sarà vuota se la riga non ha dati

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null); // nessun bottone nelle righe vuote
                } else {
                    setGraphic(btn); // bottone nelle righe con dati
                }
            }
        });
    }
}
