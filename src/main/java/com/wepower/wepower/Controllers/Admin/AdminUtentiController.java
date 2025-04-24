package com.wepower.wepower.Controllers.Admin;

import com.wepower.wepower.Models.AdminModel.ModelDashboardAdmin;
import com.wepower.wepower.Models.AdminModel.TabellaUtentiDashboardAdmin;
import com.wepower.wepower.Views.AdminView.RigaDashboardAdmin;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AdminUtentiController implements Initializable {

    public TableColumn colResetPassword;
    @FXML
    private TableView<RigaDashboardAdmin> tableUtenti;
    @FXML
    private TableColumn<RigaDashboardAdmin, Number> colId;
    @FXML
    private TableColumn<RigaDashboardAdmin, String> colNome;
    @FXML
    private TableColumn<RigaDashboardAdmin, String> colCognome;
    @FXML
    private TableColumn<RigaDashboardAdmin,String> colDataNascita;
    @FXML
    private TableColumn<RigaDashboardAdmin, String> colEmail;
    @FXML
    private TableColumn<RigaDashboardAdmin, String> colStatoAbbonamento;

    private static AdminUtentiController istanza;

    @FXML
    private TableColumn<RigaDashboardAdmin, String> colDataRinnovo;
    @FXML
    private TableColumn<RigaDashboardAdmin, String> colDataScadenza;
    @FXML
    private TableColumn<RigaDashboardAdmin, String> colSesso;
    @FXML
    private TableColumn<RigaDashboardAdmin, String> colCertificato;
    @FXML
    private TableColumn<RigaDashboardAdmin, Void> colModifica;
    @FXML
    private TableColumn<RigaDashboardAdmin, Void> colElimina;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        istanza=this;
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
        colModifica.setCellFactory(column -> new TableCell<RigaDashboardAdmin, Void>() {
            private final Button btnModifica = new Button("Modifica");
            {
                btnModifica.setOnAction(event ->  {
                    try {
                        RigaDashboardAdmin riga = getTableView().getItems().get(getIndex());
                        onModifica(riga, btnModifica);


                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                });}

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



        colResetPassword.setCellFactory(column -> new TableCell<RigaDashboardAdmin, Void>() {
            private final Button btnResetPassword = new Button("Resetta");
            {
                btnResetPassword.setOnAction(event ->  {
                    try {
                        RigaDashboardAdmin riga = getTableView().getItems().get(getIndex());
                        onResetta(riga, btnResetPassword);

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                });}

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btnResetPassword);
                }
            }
        });

        colElimina.setCellFactory(column -> new TableCell<RigaDashboardAdmin, Void>() {
            private final Button btnElimina = new Button("Elimina");

            {
                btnElimina.setOnAction(event -> {
                    RigaDashboardAdmin riga = getTableView().getItems().get(getIndex());
                    getTableView().getItems().remove(riga);
                    int id=riga.idClienteProperty().get();
                    try {
                        TabellaUtentiDashboardAdmin.eliminaRiga(id);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    }
                );
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


        ObservableList<RigaDashboardAdmin> utentiData = FXCollections.observableArrayList();


        try {
            utentiData.addAll(TabellaUtentiDashboardAdmin.riempiRiga());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        tableUtenti.setItems(utentiData);

    }

    public void aggiornaUtenti() {
        ObservableList<RigaDashboardAdmin> utentiData = FXCollections.observableArrayList();
        try {
            utentiData.addAll(TabellaUtentiDashboardAdmin.riempiRiga());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        tableUtenti.setItems(utentiData);}


    public static AdminUtentiController getIstanza() {
        return istanza;
    }

    private void onModifica(RigaDashboardAdmin riga, Button bottoneModifica) throws IOException {
        System.out.println("passoriga");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Admin/ModificaUtente.fxml"));
        Parent root = loader.load();
        AdminModificaUtenteController controller = loader.getController();
        controller.setUtente(riga);
        Stage stage = new Stage();
        stage.setTitle("Modifica");
        stage.setResizable(false);
        stage.setScene(new Scene(root));
        stage.initModality(Modality.WINDOW_MODAL);
        Stage finestraCorrente = (Stage) bottoneModifica.getScene().getWindow();
        stage.initOwner(finestraCorrente);
        controller.setDialogStage(stage);
        stage.showAndWait();
    }

    private void onResetta(RigaDashboardAdmin riga, Button bottoneResetta) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Admin/ResettaPassword.fxml"));
        Parent root = loader.load();
        AdminResettaPasswordController controller = loader.getController();
        controller.setIdCliente(riga.getIdCliente());
        Stage stage = new Stage();
        stage.setTitle("Resetta");
        stage.setResizable(false);
        stage.setScene(new Scene(root));
        stage.initModality(Modality.WINDOW_MODAL);
        Stage finestraCorrente = (Stage) bottoneResetta.getScene().getWindow();
        stage.initOwner(finestraCorrente);
        stage.showAndWait();
    }

}



