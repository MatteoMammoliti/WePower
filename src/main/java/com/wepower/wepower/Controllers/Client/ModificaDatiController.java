package com.wepower.wepower.Controllers.Client;

import com.wepower.wepower.Controllers.Client.ClientViewsController.ProfiloController;
import com.wepower.wepower.Models.DatiSessioneCliente;
import com.wepower.wepower.Models.Model;
import com.wepower.wepower.Models.ModelModificaDati;
import com.wepower.wepower.Models.ModelValidazione;
import com.wepower.wepower.Views.AlertHelper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;



import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ModificaDatiController implements Initializable {

    @FXML private TextField textNome;
    @FXML private TextField textTelefono;
    @FXML private TextField textAltezza;
    @FXML private TextField textCognome;
    @FXML private TextField textEmail;
    @FXML private TextField textPesoAttuale;
    @FXML private Button btnAggiorna;
    @FXML private Button btnAnnulla;
    @FXML private RadioButton btnUomo;
    @FXML private RadioButton btnDonna;
    @FXML private RadioButton btnAltro;
    @FXML private DatePicker textDataNascita;

    public ModificaDatiController() {}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ToggleGroup genere = new ToggleGroup();
        btnUomo.setToggleGroup(genere);
        btnDonna.setToggleGroup(genere);
        btnAltro.setToggleGroup(genere);
        textNome.setText(DatiSessioneCliente.getNomeUtente());

        textCognome.setText(DatiSessioneCliente.getCognome());

        textEmail.setText(DatiSessioneCliente.getEmail());

        textDataNascita.setValue(LocalDate.parse(DatiSessioneCliente.getDataNascita()));
        textDataNascita.setEditable(false);

        String telefono = DatiSessioneCliente.getTelefono();

        if (telefono != null) {
            textTelefono.setText(telefono);
        } else {
            textTelefono.setText("");
        }

        String altezza = DatiSessioneCliente.getAltezza();
        if (altezza != null) {
            textAltezza.setText(altezza);
        } else {
            textAltezza.setText("");
        }

        Integer peso = DatiSessioneCliente.getPesoAttuale();
        if (peso != null) {
            textPesoAttuale.setText(peso.toString());
        } else {
            textPesoAttuale.setText("");

        }

        String genereSelezionato = DatiSessioneCliente.getGenere();
        if ("Uomo".equals(genereSelezionato)) {
            btnUomo.setSelected(true);
        } else if ("Donna".equals(genereSelezionato)) {
            btnDonna.setSelected(true);
        } else if ("Altro".equals(genereSelezionato)) {
            btnAltro.setSelected(true);
        }

        btnAggiorna.setOnAction(event -> {
            try {
                onClickAggiorna();
            } catch (SQLException | IOException e) {
                AlertHelper.showAlert("Questo non doveva succedere", "Qualcosa è andato storto nell'aggiornamento", null, Alert.AlertType.ERROR);
            }
        });

        btnAnnulla.setOnAction(event -> onClickAnnulla());
    }

    public void onClickAggiorna() throws SQLException, IOException {
        String nome = textNome.getText();
        String cognome = textCognome.getText();
        String email = textEmail.getText();
        String telefono = textTelefono.getText();
        String altezza = textAltezza.getText();
        String pesoStr = textPesoAttuale.getText();

        if(!ModelValidazione.controlloPeso(pesoStr)){
            AlertHelper.showAlert("Errore", "Peso non valido", null,  Alert.AlertType.ERROR);
            return;
        }

        Integer peso= null;
        if(!pesoStr.isEmpty()){
            peso=Integer.parseInt(pesoStr);
        }

        if(!ModelValidazione.controlloNome(nome)){
            AlertHelper.showAlert("Errore", "Nome non valido", null,  Alert.AlertType.ERROR);
            return;
        }
        if(!ModelValidazione.controlloCognome(cognome)){
            AlertHelper.showAlert("Errore", "Cognome non valido", null,  Alert.AlertType.ERROR);
            return;
        }
        if(!ModelValidazione.controlloEmailvalida(email)){
            AlertHelper.showAlert("Errore", "Email non valida", null,  Alert.AlertType.ERROR);
            return;
        }
        if(ModelValidazione.controlloEmailEsistente(email)){
            AlertHelper.showAlert("Errore", "Email già esistente", null,  Alert.AlertType.ERROR);
            return;

        }
        if(altezza!=null && !ModelValidazione.controlloAltezza(altezza)){
            AlertHelper.showAlert("Errore", "Altezza non valida", null,  Alert.AlertType.ERROR);
            return;
        }

        if(!ModelValidazione.controlloData(textDataNascita.getValue())){
            AlertHelper.showAlert("Errore", "Data non valida", null,  Alert.AlertType.ERROR);
            return;
        }

        if(telefono!=null && !ModelValidazione.controlloNumeroTelefono(telefono)){
            AlertHelper.showAlert("Errore", "Telefono non valido", null,  Alert.AlertType.ERROR);
            return;
        }

        String data=textDataNascita.getValue().toString();
        String genere = null;
        if (btnUomo.isSelected()) {
            genere = "Uomo";
        } else if (btnDonna.isSelected()) {
            genere = "Donna";
        } else if (btnAltro.isSelected()) {
            genere = "Altro";
        }

        if (altezza != null && altezza.trim().isEmpty()) altezza = null;
        if (telefono != null && telefono.trim().isEmpty()) telefono = null;

        if(ModelModificaDati.onClickModificaDati(DatiSessioneCliente.getIdUtente(),nome,cognome,data,genere,altezza,email,telefono,peso)){
            AlertHelper.showAlert("Dati modificati con successo", "Dati modificati con successo", null,  Alert.AlertType.INFORMATION);
            Model.getInstance().getClientMenuController().caricaMenu();
            ProfiloController.getInstance().caricaInterfacciaDatiUtente();

            Stage stage= (Stage) btnAggiorna.getScene().getWindow();
            stage.close();
        }
    }

    public void onClickAnnulla(){
        Stage stage= (Stage) btnAggiorna.getScene().getWindow();
        stage.close();
    }
}
