package com.wepower.wepower.Controllers.Admin;

import com.wepower.wepower.ControlloTemi;
import com.wepower.wepower.Models.AdminModel.TabellaUtentiDashboardAdmin;
import com.wepower.wepower.Models.DatiSessioneCliente;
import com.wepower.wepower.Views.AdminView.RigaUtentiAdmin;
import com.wepower.wepower.Views.AlertHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class AdminUtentiController implements Initializable {

    @FXML private Button btnGestisciCapienza;
    @FXML private TableView<RigaUtentiAdmin> tableUtenti;
    @FXML private TableColumn<RigaUtentiAdmin, Number> colId;
    @FXML private TableColumn<RigaUtentiAdmin, String> colNome;
    @FXML private TableColumn<RigaUtentiAdmin, String> colCognome;
    @FXML private TableColumn<RigaUtentiAdmin,String> colDataNascita;
    @FXML private TableColumn<RigaUtentiAdmin, String> colEmail;
    @FXML private TableColumn<RigaUtentiAdmin, String> colStatoAbbonamento;
    @FXML private TableColumn<RigaUtentiAdmin, String> colDataRinnovo;
    @FXML private TableColumn<RigaUtentiAdmin, String> colDataScadenza;
    @FXML private TableColumn<RigaUtentiAdmin, String> colSesso;
    @FXML private TableColumn<RigaUtentiAdmin, String> colCertificato;
    @FXML private TableColumn<RigaUtentiAdmin, Void> colModifica;
    @FXML private TableColumn<RigaUtentiAdmin, Void> colElimina;

    public AdminUtentiController() {}

    // TUTTO ????? (GESTIONE DELLE ECCEZIONI CON ALERT (classe nostra) O STAMPE)
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(cellData -> cellData.getValue().idClienteProperty());
        colNome.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        colCognome.setCellValueFactory(cellData -> cellData.getValue().cognomeProperty());
        colDataNascita.setCellValueFactory(cellData -> cellData.getValue().dataNascitaProperty());
        colEmail.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        colStatoAbbonamento.setCellValueFactory(cellData -> cellData.getValue().statoAbbonamentoProperty());
        colCertificato.setCellValueFactory(cellData -> cellData.getValue().statoCertificatoProperty());
        colDataRinnovo.setCellValueFactory(cellData -> cellData.getValue().dataRinnovoProperty());
        colDataScadenza.setCellValueFactory(cellData -> cellData.getValue().dataScadenzaProperty());
        colSesso.setCellValueFactory(cellData -> cellData.getValue().sessoProperty());
        colModifica.setCellFactory(column -> new TableCell<RigaUtentiAdmin, Void>() {

            private final Button btnModifica = new Button("Modifica");
            {
                btnModifica.setOnAction(event ->  {
                    try {
                        RigaUtentiAdmin riga = getTableView().getItems().get(getIndex());
                        onModifica(riga, btnModifica);
                    } catch (IOException e) {
                        AlertHelper.showAlert("Questo non doveva succedere", "Qualcosa è andato storto", null, Alert.AlertType.ERROR);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btnModifica);
                }
            }
        });

        colElimina.setCellFactory(column -> new TableCell<RigaUtentiAdmin, Void>() {
            private final Button btnElimina = new Button("Elimina");

            {
                btnElimina.setOnAction(event -> {
                    Alert conferma = new Alert(Alert.AlertType.CONFIRMATION);
                    conferma.setTitle("Conferma eliminazione");
                    conferma.setHeaderText("Sei sicuro di voler eliminare questo cliente?");
                    ImageView icon = new ImageView(new Image(DatiSessioneCliente.class.getResourceAsStream("/Images/IconeAlert/question.png")));
                    DialogPane dialogPane = conferma.getDialogPane();
                    dialogPane.getStylesheets().add(DatiSessioneCliente.class.getResource("/Styles/alertStyle.css").toExternalForm());
                    Stage alertStage = (Stage) dialogPane.getScene().getWindow();
                    alertStage.getIcons().add(new Image(getClass().getResource("/Images/favicon.png").toExternalForm()));
                    dialogPane.getStylesheets().add(ControlloTemi.getInstance().getCssTemaCorrente());
                    conferma.setGraphic(icon);

                    Optional<ButtonType> resultConferma = conferma.showAndWait();
                    if (resultConferma.isPresent() && resultConferma.get() == ButtonType.OK) {
                        RigaUtentiAdmin riga = getTableView().getItems().get(getIndex());
                        getTableView().getItems().remove(riga);
                        int id=riga.idClienteProperty().get();

                        try {
                            TabellaUtentiDashboardAdmin.eliminaRiga(id);
                        } catch (SQLException e) {
                            AlertHelper.showAlert("Questo non doveva succedere", "Qualcosa è andato storto", null, Alert.AlertType.ERROR);
                        }
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btnElimina);
                }
            }
        });


        ObservableList<RigaUtentiAdmin> utentiData = FXCollections.observableArrayList();

        btnGestisciCapienza.setOnAction(event -> {
            try{
               onGestisciCapienza();
           }catch (Exception e){
                AlertHelper.showAlert("Questo non doveva succedere", "Qualcosa è andato storto nell'aggiornamento della capienza", null, Alert.AlertType.ERROR);
            }
        });

        try {
            utentiData.addAll(TabellaUtentiDashboardAdmin.riempiRiga());
        } catch (SQLException e) {
            AlertHelper.showAlert("Questo non doveva succedere", "Qualcosa è andato storto", null, Alert.AlertType.ERROR);
        }
        tableUtenti.setItems(utentiData);
    }

    private void onGestisciCapienza() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Admin/AdminMenuView/MassimoPrenotazioni.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Gestisci capienza");
        Scene scene = new Scene(root);
        stage.setResizable(false);
        stage.setScene(scene);
        scene.getStylesheets().add(ControlloTemi.getInstance().getCssTemaCorrente());
        stage.initModality(Modality.WINDOW_MODAL);
        Stage finestraCorrente = (Stage) btnGestisciCapienza.getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/Images/favicon.png")));
        stage.initOwner(finestraCorrente);
        stage.showAndWait();
    }

    private void onModifica(RigaUtentiAdmin riga, Button bottoneModifica) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Admin/ModificaUtente.fxml"));
        Parent root = loader.load();
        AdminModificaUtenteController controller = loader.getController();
        controller.setUtente(riga);
        Stage stage = new Stage();
        stage.setTitle("Modifica");
        stage.setResizable(false);
        Scene scene = new Scene(root);
        scene.getStylesheets().add(ControlloTemi.getInstance().getCssTemaCorrente());
        stage.setScene(scene);
        scene.getStylesheets().add(ControlloTemi.getInstance().getCssTemaCorrente());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/Images/favicon.png")));
        Stage finestraCorrente = (Stage) bottoneModifica.getScene().getWindow();
        stage.initOwner(finestraCorrente);
        controller.setDialogStage(stage);
        stage.showAndWait();
    }
}