package com.wepower.wepower.Controllers.Admin;
import com.wepower.wepower.Models.AdminModel.ModelDashboardAdmin;
import com.wepower.wepower.Views.AdminView.RigaVisualizzatoreOfferteAttive;
import com.wepower.wepower.Views.AlertHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;

public class AdminDashboardController implements Initializable {
    static AdminDashboardController instance;

    @FXML private BarChart graficoAnnuale;
    @FXML private VBox containerPromozioniAttive;
    @FXML private Label labelPrenotatiOggi;
    @FXML private Label labelCertificatiAttesa;
    @FXML private Label labelRichiesteSchede;
    @FXML private Label labelTotaleAbbonamenti;
    @FXML private PieChart tortaGenere;
    @FXML private ComboBox<Integer> annoGraficoTendina;

    public AdminDashboardController() {}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instance = this;
        setDatiPalestra();
        setPromozioni();
        loadGraficoGenere();
        setTendinaAnnoGrafico();
        graficoAnnuale.setAnimated(false);
    }

    public  static AdminDashboardController getInstance() {return instance;}

    //Carico i dati della palestra visibili nella dashboard (Totale abbonamenti,richieste schede,certificati in attesa,ecc..)
    public void setDatiPalestra(){
        labelTotaleAbbonamenti.setText(ModelDashboardAdmin.numeroAbbonamentiAttivi()+"");
        labelCertificatiAttesa.setText(ModelDashboardAdmin.numeroCertificatiAttesa()+"");
        labelPrenotatiOggi.setText(ModelDashboardAdmin.numeroPrenotatiOggi()+"");
        labelRichiesteSchede.setText(ModelDashboardAdmin.getNumeroSchedeRichieste()+"");
    }

    //Carico nella tendina gli anni per cui è disponibile un grafico
    public void setTendinaAnnoGrafico(){
        //Prelevo solo gli anni per i quali è disponibile un grafico
        ArrayList<Integer> lista= ModelDashboardAdmin.getAnniTendinaGrafico();

        //Converto lista in una lista osservabile, in modo tale che si aggiorni
        ObservableList<Integer> anniObs = FXCollections.observableArrayList(lista);

        //Carico i dati nel grafico
        annoGraficoTendina.setItems(anniObs);

        //Verifico che ci sia almeno un anno disponibile e chiamo la funzione epr visualizzare il grafico dell'anno più recente
        if (!anniObs.isEmpty()) {
            annoGraficoTendina.getSelectionModel().selectFirst();
            loadGraficoAnnuale(annoGraficoTendina.getValue());
        }

        //Pulizia e aggiornamento del grafico
        annoGraficoTendina.setOnAction(e -> {
            Integer anno = annoGraficoTendina.getValue();
            if (anno != null) {
                graficoAnnuale.getData().clear();
                loadGraficoAnnuale(anno);
            }
        });
    }

    // Carico le promozioni attive
    public void setPromozioni(){
        //Svuoto il container
        containerPromozioniAttive.getChildren().clear();

        //Prelevo le promozioni attive
        ArrayList<Pair<String,String>> promozioni;
        promozioni=ModelDashboardAdmin.promozioniAttive();

        for (Pair<String, String> stringStringPair : promozioni) {
            String nome = stringStringPair.getKey();
            String costo = stringStringPair.getValue();

            //Aggiungo il banner
            containerPromozioniAttive.getChildren().add(new RigaVisualizzatoreOfferteAttive(nome, costo));
        }

        //Aggiungo il pulsante per le modifiche
        Button modifica=new Button("Aggiungi promozione");
        modifica.getStyleClass().add("bottoni_offerte");
        modifica.setOnAction(event -> {

            //Apro la finestra per l'inserimento di una nuova offerta
            try {
                InserimentoNuovaOffertaController.apriSchermataAggiungiPromoziome();
            } catch (IOException e) {
                AlertHelper.showAlert("Questo non doveva succeda", "Errore durante l'apertura della schermata di inserimento offerte", null, Alert.AlertType.ERROR);
            }
        });
        modifica.getStyleClass().add("bottone_scheda");
        containerPromozioniAttive.getChildren().add(modifica);
    }

    //Carico il grafico principale della dashboard admin
    public void loadGraficoAnnuale(int anno){

        //Converto anno in stringa
        String annoStringa=String.valueOf(anno);

        //Prelevo i dati dal database in una map per mantenere l'ordine da 1 a 12
        Map<String,Integer> dati= ModelDashboardAdmin.getDatiGraficoAnnuale(annoStringa);

        //Setto i futuri valori dell'asse x con i mesi dell'anno
        String[] mesi = { "Gen", "Feb", "Mar", "Apr", "Mag", "Giu", "Lug", "Ago", "Set", "Ott", "Nov", "Dic" };

        //Inizializzo e carico i dati
        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        serie.setName(annoStringa);
        int i=0;
        for (Integer totale : dati.values()) {
            serie.getData().add(new XYChart.Data<>(mesi[i++], totale));
        }

        //Aggiorno il grafico
        graficoAnnuale.getData().clear();
        graficoAnnuale.getData().add(serie);
    }

    // Carico il grafico per genere dashboard admin
    public void loadGraficoGenere(){
        //Prelevo i dati
        ArrayList<Pair<String,Integer>> dati=ModelDashboardAdmin.getSessoUtentiPalestra();

        //Creo una lista osservabile per tenere aggiornato il grafico
        ObservableList<PieChart.Data> observableList= FXCollections.observableArrayList();

        //Carico i dati nella lista
        for(Pair<String,Integer> i:dati){
            String nome=i.getKey();
            Integer numero=i.getValue();
            if (nome==null){
                nome="Non Specificato";
            }

            observableList.add(new PieChart.Data(nome,numero));
        }
        //Aggiorno il grafico
        tortaGenere.setTitle("Percentuale iscritti per genere");
        tortaGenere.setData(observableList);
    }
}