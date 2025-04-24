package com.wepower.wepower.Controllers.Admin;

import com.wepower.wepower.Models.DatiPalestra.DatiSessionePalestra;
import com.wepower.wepower.Views.AdminView.RigaTabellaCertificati;
import com.wepower.wepower.Views.AdminView.RigaTabellaRichiesteScheda;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class SchedeAdminController implements Initializable {
    @FXML
    private TableView<RigaTabellaRichiesteScheda> tabellaSchede;
    @FXML
    private TableColumn<RigaTabellaRichiesteScheda, Void> colCrea;
    @FXML
    private TableColumn<RigaTabellaRichiesteScheda, String> colSesso;
    @FXML
    private TableColumn<RigaTabellaRichiesteScheda, Integer> colPeso;
    @FXML
    private TableColumn<RigaTabellaRichiesteScheda, String> colCognome;
    @FXML
    private TableColumn<RigaTabellaRichiesteScheda, String> colNome;
    @FXML
    private TableColumn<RigaTabellaRichiesteScheda, Integer> colID;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colID.setCellValueFactory(new PropertyValueFactory<>("idUtente"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colCognome.setCellValueFactory(new PropertyValueFactory<>("cognome"));
        colPeso.setCellValueFactory(new PropertyValueFactory<>("peso"));
        colSesso.setCellValueFactory(new PropertyValueFactory<>("sesso"));
        //Carico i dati delle richieste di scheda
        aggiornaTabbella();
        aggiungiBottone();

    }

    public void aggiornaTabbella() {
        ObservableList<RigaTabellaRichiesteScheda> utenti = DatiSessionePalestra.getRichiesteScheda();
        tabellaSchede.setItems(utenti);
    }

    private void aggiungiBottone() {
        SchedeAdminController controller = this;
        colCrea.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("Visualizza");

            {
                btn.setOnAction(event -> {
                    RigaTabellaRichiesteScheda riga = getTableView().getItems().get(getIndex());
                    int id = riga.getIdUtente();
                    System.out.println("ID" + id);
                    //Quando clicchiamo sul bottone, chiamiamo il metodo onClickVisualizza e gli passiamo l'id del cliente
                    try {

                    } catch (Exception e) {
                        throw new RuntimeException(e);
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
