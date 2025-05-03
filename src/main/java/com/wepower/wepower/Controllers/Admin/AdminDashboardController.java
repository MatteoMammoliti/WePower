package com.wepower.wepower.Controllers.Admin;

import com.wepower.wepower.Models.AdminModel.ModelDashboardAdmin;
import com.wepower.wepower.Views.AdminView.RigaVisualizzatoreOfferteAttive;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AdminDashboardController implements Initializable {
    static AdminDashboardController instance;
    @FXML
    private VBox containerPromozioniAttive;
    @FXML
    private Label labelPrenotatiOggi;
    @FXML
    private Label labelCertificatiAttesa;
    @FXML
    private Label labelRichiesteSchede;
    @FXML
    private Label labelTotaleAbbonamenti;
    @FXML
    private PieChart tortaGenere;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instance = this;
        setDatiPalestra();
        setPromozioni();
        loadGraficoGenere();
    }

    public  static AdminDashboardController getInstance() {return instance;}
    //Carico i dati della palestra visibili nella dashboard(Totale abbonamenti,richieste schede,certificati in attesa,ecc..)
    public void setDatiPalestra(){
        labelTotaleAbbonamenti.setText(ModelDashboardAdmin.numeroAbbonamentiAttivi()+"");
        labelCertificatiAttesa.setText(ModelDashboardAdmin.numeroCertificatiAttesa()+"");
        labelPrenotatiOggi.setText(ModelDashboardAdmin.numeroPrenotatiOggi()+"");
        labelRichiesteSchede.setText(ModelDashboardAdmin.getNumeroSchedeRichieste()+"");
    }

    //Carico le promozioni attive
    public void setPromozioni(){
        //Svuoto il container
        containerPromozioniAttive.getChildren().clear();
        //Prelevo le promozioni attive
        ArrayList<Pair<String,String>> promozioni=new ArrayList<>();
        promozioni=ModelDashboardAdmin.promozioniAttive();

        for(int i=0;i<promozioni.size();i++){
            String nome=promozioni.get(i).getKey();
            String costo=promozioni.get(i).getValue();

            //Aggiungo il banner
            containerPromozioniAttive.getChildren().add(new RigaVisualizzatoreOfferteAttive(nome,costo));
        }
        //Aggiungo il pulsante per le modifiche
        Button modifica=new Button("Aggiungi promozione");
        modifica.setOnAction(event -> {
            //Apro la finestra per l'inserimento di una nuova offerta
            try {
                InserimentoNuovaOffertaController.apriSchermataAggiungiPromoziome();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        modifica.getStyleClass().add("bottone_scheda");
        containerPromozioniAttive.getChildren().add(modifica);
    }

    public void loadGraficoGenere(){
        ArrayList<Pair<String,Integer>> dati=ModelDashboardAdmin.getSessoUtentiPalestra();
        ObservableList<PieChart.Data> observableList= FXCollections.observableArrayList();
        for(Pair<String,Integer> i:dati){
            String nome=i.getKey();
            Integer numero=i.getValue();
            if (nome==null){
                nome="Non Specificato";
            }

            observableList.add(new PieChart.Data(nome,numero));


        }
        tortaGenere.setTitle("Percentuale iscritti per genere");
        tortaGenere.setData(observableList);

    }

}
