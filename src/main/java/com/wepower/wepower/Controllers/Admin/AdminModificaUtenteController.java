package com.wepower.wepower.Controllers.Admin;

import com.wepower.wepower.Models.AdminModel.TabellaUtentiDashboardAdmin;
import com.wepower.wepower.Models.ModelValidazione;
import com.wepower.wepower.Views.AdminView.RigaDashboardAdmin;
import com.wepower.wepower.Views.AlertHelper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class AdminModificaUtenteController implements Initializable{
    @FXML
    private ComboBox<String> inputIdTipoAbbonamento;
    @FXML
    private TextField inputNuovoNome;
    @FXML
    private TextField inputNuovoCognome;
    @FXML
    private DatePicker inputNuovaDataNascita;
    @FXML
    private Button inputAnnulla;

    private Stage dialogStage;

    public void setDialogStage(Stage stage) {
        this.dialogStage = stage;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        ArrayList<String> tipiAbbonamenti=TabellaUtentiDashboardAdmin.prelevaTipiAbbonamenti();
        inputIdTipoAbbonamento.getItems().setAll(tipiAbbonamenti);
        this.inputAnnulla.setOnAction(event -> {
            onClickAnnulla();
        });
    }

    private RigaDashboardAdmin riga;
    public void setUtente(RigaDashboardAdmin riga) {
        this.riga = riga;
        inputNuovoNome.setText(riga.getNome());
        inputNuovoCognome.setText(riga.getCognome());
        inputNuovaDataNascita.setValue(LocalDate.parse(riga.getDataNascita().toString()));
        if (riga.getStatoAbbonamento().equals("Attivo")) {
            inputIdTipoAbbonamento.setDisable(true);
        }
    }

    @FXML
    private void salvaModificheUtente() throws SQLException {
        int id=riga.getIdCliente();
        String nuovoNome = inputNuovoNome.getText();
        String nuovoCognome = inputNuovoCognome.getText();
        LocalDate nuovaDataNascita=inputNuovaDataNascita.getValue();
        LocalDate nuovaDataRinnovo=LocalDate.now();
        String nomeAbbonamento = inputIdTipoAbbonamento.getValue();
        LocalDate oggi = LocalDate.now();
        LocalDate nuovaDataScadenza = null;
        int statoAbbonamento=0;
        int idTipoAbbonamento=-1;
        if (nomeAbbonamento != null){
            int mesiDaAggiungere=TabellaUtentiDashboardAdmin.prelevaDurataTipoAbbonamento(nomeAbbonamento);
            idTipoAbbonamento=TabellaUtentiDashboardAdmin.prelevaIdTipiAbbonamento(nomeAbbonamento);
            nuovaDataScadenza= nuovaDataRinnovo.plusMonths(mesiDaAggiungere);
            if ((oggi.isBefore(nuovaDataScadenza) || oggi.isEqual(nuovaDataScadenza)) && (oggi.isAfter(nuovaDataRinnovo)||oggi.isEqual(nuovaDataRinnovo)) ){
                statoAbbonamento=1;
            }
        }

        boolean modificaUtente=false;
        try {

            String dataNascita=null;
            if (nuovaDataNascita!=null){
                dataNascita=nuovaDataNascita.toString();
            }

            String dataFineAbbonamento = null;
            if (nuovaDataScadenza != null) {
                dataFineAbbonamento=nuovaDataScadenza.toString();
            }
            if(nuovoNome.equals("") || nuovoCognome.equals("")||dataNascita.equals("")){
                AlertHelper.showAlert("Errore","Compila tutti i campi", null, Alert.AlertType.ERROR);
                return;
            }
            if (!ModelValidazione.controlloNome(nuovoNome)) {
                AlertHelper.showAlert("Errore","Nome non valido", null, Alert.AlertType.ERROR);
                return;
            }
            if (!ModelValidazione.controlloNome(nuovoCognome)) {
                AlertHelper.showAlert("Errore","Cognome non valido", null, Alert.AlertType.ERROR);
                return;
            }
            if(!ModelValidazione.controlloData(nuovaDataNascita)){
                AlertHelper.showAlert("Errore","Data nascita non valida", null, Alert.AlertType.ERROR);
                return;
            }
            modificaUtente = TabellaUtentiDashboardAdmin.salvaModifiche(id, nuovoNome, nuovoCognome, inputNuovaDataNascita.getValue().toString(), nuovaDataRinnovo.toString(), dataFineAbbonamento, statoAbbonamento, idTipoAbbonamento);

        if (modificaUtente){
            riga.setNome(nuovoNome);
            riga.setCognome(nuovoCognome);
            riga.setDataNascita(dataNascita);
            if (idTipoAbbonamento!=-1){
                riga.setDataRinnovo(nuovaDataRinnovo.toString());
                riga.setDataScadenza(dataFineAbbonamento);
                if(statoAbbonamento==1){
                    riga.setStatoAbbonamento("Attivo");
                }
                else{
                    riga.setStatoAbbonamento("Non Attivo");
                }
            }
        }
        dialogStage.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void onClickAnnulla() {

        String dataN=riga.getDataNascita();
        String dataNAgg=inputNuovaDataNascita.getEditor().getText();


        boolean modificaUtente = !inputNuovoNome.getText().equals(riga.getNome()) ||  !inputNuovoCognome.getText().equals(riga.getCognome()) || !dataN.equals(dataNAgg);
        if (modificaUtente){
            Alert chiudi = new Alert(Alert.AlertType.ERROR);
            chiudi.setTitle("Attenzione");
            chiudi.setHeaderText("Sei sicuro di voler annullare le modifiche?");
            chiudi.setContentText("Questa operazione annullerà tutte le modifiche non salvate.");
            ImageView icon = new ImageView(new Image(getClass().getResourceAsStream("/Images/IconeAlert/error.png")));
            DialogPane dialogPane = chiudi.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("/Styles/alertStyle.css").toExternalForm());
            chiudi.setGraphic(icon);
            ButtonType conferma = new ButtonType("Sì", ButtonBar.ButtonData.OK_DONE);
            ButtonType annulla = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

            chiudi.getButtonTypes().setAll(conferma, annulla);
            Optional<ButtonType> risultato = chiudi.showAndWait();
            Stage stage = null;
        if (risultato.isPresent() && risultato.get() == conferma) {
            stage = (Stage) inputAnnulla.getScene().getWindow();
            stage.close();
        }
    }
        else {
            Stage stage = (Stage) inputAnnulla.getScene().getWindow();
            stage.close();
        }
    }
}