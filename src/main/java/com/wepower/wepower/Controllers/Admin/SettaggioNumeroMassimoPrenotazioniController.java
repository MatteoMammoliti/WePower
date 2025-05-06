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


//Setto la capienza massima giornaliera della palestra per gestire le prenotazioni
public class SettaggioNumeroMassimoPrenotazioniController implements Initializable {
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

    //Verifico se il valore sia valido e lo salvo
    private void onSalva(){
        //Salvo l'input in una stringa
        String input = inputNumeroMax.getText();

        //Controllo se sia un numero di almeno una cifra
        boolean valido = Pattern.matches("\\d+",input);

        //Converto l'input in int
        int capienza = Integer.parseInt(input);

        //Verifico che sia valido e positivo ed in seguito chiamo la funzione per aggiornare il database
        if(valido && capienza > 0){
            ModelMassimoPrenotazioni.salvaNuovaCapienza(capienza);
            //Chiudo la finestra
            ((Stage) btnSalva.getScene().getWindow()).close();
        }
        else{
            //Avviso l'utente che il valore inserito non è valido
            AlertHelper.showAlert("Errore", "Valore non valido", null, Alert.AlertType.ERROR);
        }
    }

    //Chiudo la finestra
    private void onAnnulla(){
        ((Stage) btnAnnulla.getScene().getWindow()).close();
    }
}