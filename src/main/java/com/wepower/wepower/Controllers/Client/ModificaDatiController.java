package com.wepower.wepower.Controllers.Client;

import com.wepower.wepower.Controllers.Client.ClientViewsController.ProfiloController;
import com.wepower.wepower.Models.DatiSessioneCliente;
import com.wepower.wepower.Models.Model;
import com.wepower.wepower.Models.ModelModificaDati;
import com.wepower.wepower.Models.ModelValidazione;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;


import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ModificaDatiController implements Initializable {
    public TextField textNome;
    public TextField textTelefono;
    public TextField textAltezza;
    public TextField textCognome;
    public TextField textEmail;
    public TextField textPesoAttuale;
    public Button btnAggiorna;
    public Button btnAnnulla;
    public RadioButton btnUomo;
    public RadioButton btnDonna;
    public RadioButton btnAltro;
    public DatePicker textDataNascita;

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
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
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
        Alert alert;

        if(!ModelValidazione.controlloPeso(pesoStr)){
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setHeaderText("Peso non valido");
            ImageView icon = new ImageView(new Image(getClass().getResourceAsStream("/Images/IconeAlert/error.png")));
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("/Styles/alertStyle.css").toExternalForm());
            alert.setGraphic(icon);
            alert.showAndWait();
            return;
        }

        Integer peso= null;
        if(!pesoStr.isEmpty()){
            peso=Integer.parseInt(pesoStr);
        }

        if(!ModelValidazione.controlloNome(nome)){
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setHeaderText("Nome non valido");
            ImageView icon = new ImageView(new Image(getClass().getResourceAsStream("/Images/IconeAlert/error.png")));
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("/Styles/alertStyle.css").toExternalForm());
            alert.setGraphic(icon);
            alert.showAndWait();
            return;
        }
        if(!ModelValidazione.controlloCognome(cognome)){
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setHeaderText("Cognome non valido");
            ImageView icon = new ImageView(new Image(getClass().getResourceAsStream("/Images/IconeAlert/error.png")));
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("/Styles/alertStyle.css").toExternalForm());
            alert.setGraphic(icon);
            alert.showAndWait();
            return;
        }
        if(!ModelValidazione.controlloEmailvalida(email)){
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setHeaderText("Email non valida");
            ImageView icon = new ImageView(new Image(getClass().getResourceAsStream("/Images/IconeAlert/error.png")));
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("/Styles/alertStyle.css").toExternalForm());
            alert.setGraphic(icon);
            alert.showAndWait();
            return;
        }
        if(ModelValidazione.controlloEmailEsistente(email)){
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setHeaderText("Email già esistente");
            ImageView icon = new ImageView(new Image(getClass().getResourceAsStream("/Images/IconeAlert/error.png")));
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("/Styles/alertStyle.css").toExternalForm());
            alert.setGraphic(icon);
            alert.showAndWait();
            return;

        }
        if(altezza!=null && !ModelValidazione.controlloAltezza(altezza)){
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setHeaderText("Altezza non valido");
            ImageView icon = new ImageView(new Image(getClass().getResourceAsStream("/Images/IconeAlert/error.png")));
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("/Styles/alertStyle.css").toExternalForm());
            alert.setGraphic(icon);
            alert.showAndWait();
            return;
        }
        if(!ModelValidazione.controlloData(textDataNascita.getValue())){
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setHeaderText("Data non valido");
            ImageView icon = new ImageView(new Image(getClass().getResourceAsStream("/Images/IconeAlert/error.png")));
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("/Styles/alertStyle.css").toExternalForm());
            alert.setGraphic(icon);
            alert.showAndWait();
            return;
        }


        if(telefono!=null && !ModelValidazione.controlloNumeroTelefono(telefono)){
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setHeaderText("Telefono non valido");
            ImageView icon = new ImageView(new Image(getClass().getResourceAsStream("/Images/IconeAlert/error.png")));
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("/Styles/alertStyle.css").toExternalForm());
            alert.setGraphic(icon);
            alert.showAndWait();
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
            Alert success = new Alert(Alert.AlertType.INFORMATION);
            success.setTitle("Dati modificati con successo");
            success.setHeaderText("Dati modificati con successo");
            ImageView icon = new ImageView(new Image(getClass().getResourceAsStream("/Images/IconeAlert/info.png")));
            DialogPane dialogPane = success.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("/Styles/alertStyle.css").toExternalForm());
            success.setGraphic(icon);
            success.showAndWait();
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
