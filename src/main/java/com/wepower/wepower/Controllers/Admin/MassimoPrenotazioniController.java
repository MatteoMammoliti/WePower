package com.wepower.wepower.Controllers.Admin;

import com.wepower.wepower.Models.AdminModel.ModelMassimoPrenotazioni;
import com.wepower.wepower.Views.AlertHelper;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class MassimoPrenotazioniController implements Initializable {
    public Button btnSalva;
    public Button btnAnnulla;
    public TextField inputNumeroMax;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnSalva.setOnAction(e -> {
           onSalva();
        });
        btnAnnulla.setOnAction(e -> {
            onAnnulla();
        });

    }

    private void onSalva(){
        String input = inputNumeroMax.getText();
        boolean valido = Pattern.matches("\\d+",input);
        int capienza = Integer.parseInt(input);
        if(valido && capienza > 0){
            ModelMassimoPrenotazioni.salvaNuovaCapienza(capienza);
            ((Stage) btnSalva.getScene().getWindow()).close();
        }
        else{
            AlertHelper.showAlert("Errore", "Valore non valido", null, Alert.AlertType.ERROR);
        }
    }

    private void onAnnulla(){
        ((Stage) btnAnnulla.getScene().getWindow()).close();
    }











}
