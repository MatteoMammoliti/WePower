package com.wepower.wepower.Controllers.Client;

import com.wepower.wepower.Models.DatiSessioneCliente;
import com.wepower.wepower.Models.PrenotazioneSalaPesi;
import com.wepower.wepower.Views.BannerAbbonamenti;
import com.wepower.wepower.Views.Calendario;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.net.URL;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ClientDashboardController implements Initializable {
    public AnchorPane containerCalendario;
    @FXML
    private Label labelNomeUtenteSaluto;


    // container del displayer dei banner
    public ScrollPane scrollPaneBanner;

    @FXML
    // container dei banner
    private HBox displayerBanner;

    private void loadBanner() {


        // imposta la larghezza del conter dei banner tenendo conto degli spazi tra loro
        scrollPaneBanner.setPrefWidth((100 * 200));

        // crea i banner
        ArrayList<BannerAbbonamenti> bannerini = new  ArrayList<>();
        bannerini=BannerAbbonamenti.getBannerAbbonamentiDB();
        for(int i=0; i<bannerini.size(); i++){
            // aggiungo i banner all'HBox
            displayerBanner.getChildren().add(bannerini.get(i));
        }
        //Creo il calendario
        VBox calendario= Calendario.creaCalendario();
        containerCalendario.getChildren().add(calendario);
    }

    // funzione per lo scroll automatico dei banner
    private void startAutoScroll() {

        // oggetto che esegue animazioni ad intervalli regolari fissati
        Timeline timeline = new Timeline(

                // azione eseguita ad ogni intervallo di 3 secondi
                new KeyFrame(Duration.millis(20), event -> {
                    double posizioneBanner = scrollPaneBanner.getHvalue(); // posizione attuale dei 3 banner su cui c'è il focus
                    double posizioneScrollataBanner = posizioneBanner + 0.002; // posizione aumentata di 1/3 (per visualizzare un banner nuovo sui prossimi 3)
                    if (posizioneScrollataBanner > 1) posizioneScrollataBanner = 0; // se si ha raggiunto la fine dei banner, si torna indietro
                    scrollPaneBanner.setHvalue(posizioneScrollataBanner); // applica lo scroll
                })
        );
        timeline.setCycleCount(Animation.INDEFINITE); // ripete l'animazione all'infinito
        timeline.play();

        // ferma lo scroll quando il mouse entra nel banner
        scrollPaneBanner.setOnMouseEntered(event -> timeline.stop());

        // riprende lo scroll quando il mouse esce dal banner
        scrollPaneBanner.setOnMouseExited(event -> timeline.play());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        labelNomeUtenteSaluto.setText("Ciao, "+ DatiSessioneCliente.getNomeUtente() + "👋");
        loadBanner();
        startAutoScroll();
    }


}