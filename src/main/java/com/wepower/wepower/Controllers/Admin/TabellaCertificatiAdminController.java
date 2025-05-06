package com.wepower.wepower.Controllers.Admin;

import com.wepower.wepower.Models.AdminModel.ModelTabellaCertificati;
import com.wepower.wepower.Models.Model;
import com.wepower.wepower.Views.AdminView.RigaTabellaCertificati;
import com.wepower.wepower.Views.AlertHelper;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.util.ResourceBundle;

public class TabellaCertificatiAdminController implements Initializable {
    //Qui dichiariamo che le colonne della tabella appartiene ad una tabella di ogetti RigaTabellaCertificati, e mostrerà una proprietà di tipo Sting
    @FXML
    private TableColumn<RigaTabellaCertificati,Void>colVisualizza;
    @FXML
    private TableColumn<RigaTabellaCertificati,String> colDataCaricamento;
    @FXML
    private TableColumn<RigaTabellaCertificati,String> colCognome;
    @FXML
    private TableColumn<RigaTabellaCertificati,String> colNome;
    @FXML
    private TableColumn<RigaTabellaCertificati,String> colID;

    //Qua diciamo che la tabella conterrà righe basate su oggetti di tipo RigaTabellaCertificati
    @FXML
    private TableView<RigaTabellaCertificati> tabellaCertificati;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().setTabellaCertificatiAdminController(this);
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colCognome.setCellValueFactory(new PropertyValueFactory<>("cognome"));
        colDataCaricamento.setCellValueFactory(new PropertyValueFactory<>("dataCaricamento"));

        ObservableList<RigaTabellaCertificati> utenti= ModelTabellaCertificati.caricaDati();

        tabellaCertificati.setItems(utenti);
        aggiungiBottone();
    }

    public void aggiornaTabella(){
        ObservableList<RigaTabellaCertificati> utenti=ModelTabellaCertificati.caricaDati();
        tabellaCertificati.setItems(utenti);
    }

    private void aggiungiBottone(){
        colVisualizza.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("Visualizza");
            {
                btn.setOnAction(event -> {
                    RigaTabellaCertificati riga = getTableView().getItems().get(getIndex());
                    int id=riga.id();
                    System.out.println("ID1"+id);

                    //Quando clicchiamo sul bottone, chiamiamo il metodo onClickVisualizza e gli passiamo l'id del cliente
                    try {
                        int idRiga=riga.id();
                        VisualizzatoreCertificatoController.apriSchermataVisualizzatoreCertificato(idRiga, Model.getInstance().getTabellaCertificatiAdminController());
                    } catch (Exception e) {
                        AlertHelper.showAlert("Questo non doveva succedere", "Imposibile aprire il visualizzatore dei certificati", null, Alert.AlertType.ERROR);
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