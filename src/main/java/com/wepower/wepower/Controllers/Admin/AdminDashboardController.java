package com.wepower.wepower.Controllers.Admin;

import com.wepower.wepower.Models.AdminModel.ModelDashboardAdmin;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminDashboardController implements Initializable {
    @FXML
    private Label labelPrenotatiOggi;
    @FXML
    private Label labelCertificatiAttesa;
    @FXML
    private Label labelRichiesteSchede;
    @FXML
    private Label labelTotaleAbbonamenti;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setDatiPalestra();
    }

    //Carico i dati della palestra visibili nella dashboard(Totale abbonamenti,richieste schede,certificati in attesa,ecc..)
    private void setDatiPalestra(){
        labelTotaleAbbonamenti.setText(ModelDashboardAdmin.numeroAbbonamentiAttivi()+"");
    }
}
