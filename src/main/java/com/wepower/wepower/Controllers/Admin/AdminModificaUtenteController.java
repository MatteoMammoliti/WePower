package com.wepower.wepower.Controllers.Admin;

import com.wepower.wepower.ControlloTemi;
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

public class AdminModificaUtenteController implements Initializable {

    @FXML private ComboBox<String> inputIdTipoAbbonamento;
    @FXML private TextField inputNuovoNome;
    @FXML private TextField inputNuovoCognome;
    @FXML private DatePicker inputNuovaDataNascita;
    @FXML private Button inputAnnulla;
    private Stage dialogStage;

    //Inizializzo una RigaDashboardAdmin
    private RigaDashboardAdmin riga;

    public AdminModificaUtenteController() {}

    //Pagina utente admin
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Prelevo i tipi di abbonamento disponibili e li carico nella comboBox
        ArrayList<String> tipiAbbonamenti = TabellaUtentiDashboardAdmin.prelevaTipiAbbonamenti();
        inputIdTipoAbbonamento.getItems().setAll(tipiAbbonamenti);
        this.inputAnnulla.setOnAction(event -> {
            onClickAnnulla();
        });
    }

    public void setDialogStage(Stage stage) {
        this.dialogStage = stage;
    }

    //Inizializzo i dati con quelli attuali dell'utente da modificare
    public void setUtente(RigaDashboardAdmin riga) {
        //Setto la riga nella quale è stata chiamata la funzione onModifica
        this.riga = riga;
        //Inizializzo i vari campi
        inputNuovoNome.setText(riga.getNome());
        inputNuovoCognome.setText(riga.getCognome());
        inputNuovaDataNascita.setValue(LocalDate.parse(riga.getDataNascita()));
        //Disabilito la comboBox per attivare l'abbonamento se ne ha già uno attivo
        if (riga.getStatoAbbonamento().equals("Attivo")) {
            inputIdTipoAbbonamento.setDisable(true);
        }
    }

    //Verifico e salvo le modifiche dell'utente
    @FXML
    private void salvaModificheUtente() throws SQLException {
        //Prelevo l'id dell'utente interessato mediante la riga dal quale è stato cliccato il bottone modifica
        int id = riga.getIdCliente();
        //Prelevo i dati dai vari campi
        String nuovoNome = inputNuovoNome.getText();
        String nuovoCognome = inputNuovoCognome.getText();
        LocalDate nuovaDataNascita = inputNuovaDataNascita.getValue();
        //Imposto la data di rinnovo ad oggi
        LocalDate nuovaDataRinnovo = LocalDate.now();
        String nomeAbbonamento = inputIdTipoAbbonamento.getValue();
        LocalDate oggi = LocalDate.now();
        //Inizializzo
        LocalDate nuovaDataScadenza = null;
        int statoAbbonamento = 0;
        int idTipoAbbonamento = -1;

        //Verifico se è stato assegnato un nuovo abbonamento
        if (nomeAbbonamento != null) {
            //Prelevo i vari dati in base al nome dell'abbonamento
            int mesiDaAggiungere = TabellaUtentiDashboardAdmin.prelevaDurataTipoAbbonamento(nomeAbbonamento);
            idTipoAbbonamento = TabellaUtentiDashboardAdmin.prelevaIdTipoAbbonamento(nomeAbbonamento);
            //Calcolo la scadenza
            nuovaDataScadenza = nuovaDataRinnovo.plusMonths(mesiDaAggiungere);

            //Setto lo stato dell'abbonamento
            if ((oggi.isBefore(nuovaDataScadenza) || oggi.isEqual(nuovaDataScadenza)) && (oggi.isAfter(nuovaDataRinnovo) || oggi.isEqual(nuovaDataRinnovo))) {
                statoAbbonamento = 1;
            }
        }

        //Inizializzo
        boolean modificaUtente;
        try {
            //Salvo le nuove date in una Stringa
            String dataNascita = null;
            if (nuovaDataNascita != null) {
                dataNascita = nuovaDataNascita.toString();
            }
            String dataFineAbbonamento = null;
            if (nuovaDataScadenza != null) {
                dataFineAbbonamento = nuovaDataScadenza.toString();
            }

            //Verifica eccezioni
            if (nuovoNome.isEmpty() || nuovoCognome.isEmpty() || dataNascita.equals("")) {
                AlertHelper.showAlert("Errore", "Compila tutti i campi", null, Alert.AlertType.ERROR);
                return;
            }
            if (!ModelValidazione.controlloNome(nuovoNome)) {
                AlertHelper.showAlert("Errore", "Nome non valido", null, Alert.AlertType.ERROR);
                return;
            }
            if (!ModelValidazione.controlloNome(nuovoCognome)) {
                AlertHelper.showAlert("Errore", "Cognome non valido", null, Alert.AlertType.ERROR);
                return;
            }
            if (!ModelValidazione.controlloData(nuovaDataNascita)) {
                AlertHelper.showAlert("Errore", "Data nascita non valida", null, Alert.AlertType.ERROR);
                return;
            }

            //Salvo le modifiche
            modificaUtente = TabellaUtentiDashboardAdmin.salvaModifiche(id, nuovoNome, nuovoCognome, inputNuovaDataNascita.getValue().toString(), nuovaDataRinnovo.toString(), dataFineAbbonamento, statoAbbonamento, idTipoAbbonamento);

            //Verifico l'esito del salvataggi
            if (modificaUtente) {
                riga.setNome(nuovoNome);
                riga.setCognome(nuovoCognome);
                riga.setDataNascita(dataNascita);

                if (idTipoAbbonamento != -1) {
                    riga.setDataRinnovo(nuovaDataRinnovo.toString());
                    riga.setDataScadenza(dataFineAbbonamento);
                    if (statoAbbonamento == 1) {
                        riga.setStatoAbbonamento("Attivo");
                    } else {
                        riga.setStatoAbbonamento("Non Attivo");
                    }
                }
            }

            //Chiudo la finestra
            dialogStage.close();
        } catch (SQLException e) {
            AlertHelper.showAlert("Questo non doveva succedere", "Qualcosa è andato storto durante il salvataggio", null, Alert.AlertType.ERROR);
        }
    }

    //Chiudo la finestra e mostro gli avvertimenti solo se ci sono modifiche
    public void onClickAnnulla() {
        //Creo una copia della data di nascita nella tabella e della data di nascita nella finestra modifica utente
        String dataN = riga.getDataNascita();
        String dataNAgg = inputNuovaDataNascita.getEditor().getText();

        //Verifico se almeno un campo è stato aggiornato
        boolean modificaUtente = !inputNuovoNome.getText().equals(riga.getNome()) || !inputNuovoCognome.getText().equals(riga.getCognome()) || !dataN.equals(dataNAgg);

        //Se almento un campo è stato aggiornato chiedo all'utente se è sicuro di voler annullare le modifiche
        if (modificaUtente) {
            Alert chiudi = new Alert(Alert.AlertType.ERROR);
            chiudi.setTitle("Attenzione");
            chiudi.setHeaderText("Sei sicuro di voler annullare le modifiche?");
            chiudi.setContentText("Questa operazione annullerà tutte le modifiche non salvate.");
            ImageView icon = new ImageView(new Image(getClass().getResourceAsStream("/Images/IconeAlert/error.png")));
            DialogPane dialogPane = chiudi.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("/Styles/alertStyle.css").toExternalForm());
            Stage alertStage = (Stage) dialogPane.getScene().getWindow();
            alertStage.getIcons().add(new Image(getClass().getResource("/Images/favicon.png").toExternalForm()));
            dialogPane.getStylesheets().add(ControlloTemi.getInstance().getCssTemaCorrente());
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
        } else {
            Stage stage = (Stage) inputAnnulla.getScene().getWindow();
            stage.close();
        }
    }
}