package com.wepower.wepower.Controllers.Client;

import com.wepower.wepower.Views.Banner;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import java.net.URL;
import java.util.ResourceBundle;

public class ClientDashboardController implements Initializable {

    // container del displayer dei banner
    public ScrollPane scrollPaneBanner;

    @FXML
    // container dei banner
    private HBox displaybannerini;

    private final Banner banner = new Banner();

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
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // max banner visibili per volta
        int maxBannerVisibili = 3;

        // larghezza del banner e spazio tra i banner
        int bannerWidth = (int) banner.getLarghezza();
        int spacing = (int) displaybannerini.getSpacing();

        // imposta la larghezza del conter dei banner tenendo conto degli spazi tra loro
        scrollPaneBanner.setPrefWidth((bannerWidth * maxBannerVisibili) + (spacing * (maxBannerVisibili - 1)));

        // Crea i banner
        Banner b1 = new Banner(getClass().getResource("/Images/LOGO.png").toExternalForm(), "Smartphone", 599.99);
        Banner b2 = new Banner(getClass().getResource("/Images/LOGO.png").toExternalForm(), "Laptop", 1299.99);
        Banner b3 = new Banner(getClass().getResource("/Images/LOGO.png").toExternalForm(), "Smartwatch", 299.99);
        Banner b4 = new Banner(getClass().getResource("/Images/LOGO.png").toExternalForm(), "Smartwatch", 299.99);
        Banner b5 = new Banner(getClass().getResource("/Images/LOGO.png").toExternalForm(), "Smartwatch", 299.99);

        // Aggiungo i banner all'HBox
        displaybannerini.getChildren().addAll(b1, b2, b3, b4, b5);
        startAutoScroll();
    }
}
