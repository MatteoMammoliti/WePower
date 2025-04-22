package com.wepower.wepower.Controllers.Admin;

import com.wepower.wepower.Models.AdminModel.ModelDashboardAdmin;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class InserimentoNuovaOffertaController implements Initializable {
    static InserimentoNuovaOffertaController instance;
    @FXML
    private Button btnConferma;
    @FXML
    private Button btnAnnulla;
    @FXML
    private TextField textDurataAbbonamento;
    @FXML
    private TextField textPrezzoAbbonamento;
    @FXML
    private TextField textDescrizioneAbbonamento;
    @FXML
    private TextField textNomeAbbonamento;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instance = this;
        btnAnnulla.setOnAction(e -> onClickAnnulla());
        btnConferma.setOnAction(e -> {
            if(ModelDashboardAdmin.aggiungiPromozione(textNomeAbbonamento.getText(),textPrezzoAbbonamento.getText(),textDescrizioneAbbonamento.getText(),textDurataAbbonamento.getText())){
                //Chiudo la finestra
                Stage stage = (Stage) btnConferma.getScene().getWindow();
                AdminDashboardController.getInstance().setPromozioni();
                stage.close();
            }
        });
    }


    public static InserimentoNuovaOffertaController getInstance() {
        return instance;
    }


    //Permetto di far aprire la finestra dalla dashboard
    public static void apriSchermataAggiungiPromoziome() throws IOException {
        FXMLLoader loader = new FXMLLoader(InserimentoNuovaOffertaController.class.getResource("/fxml/Admin/AdminMenuView/InserimentoDatiNuovaOfferta.fxml"));
        Parent root = loader.load();
        Stage stage=new Stage();
        stage.setTitle("Inserimento Nuova Offerta");
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        Scene scene=new Scene(root);
        stage.setScene(scene);
        stage.showAndWait();
    }


    //Annullo l'operazione e chiudo la finestra
    public void onClickAnnulla(){
        //Chiudo la finestra
        Stage stage=(Stage) btnAnnulla.getScene().getWindow();
        stage.close();
    }


}
