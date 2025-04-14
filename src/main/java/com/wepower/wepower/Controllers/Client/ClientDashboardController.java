package com.wepower.wepower.Controllers.Client;

import com.wepower.wepower.APIs.OpenRouter_AI;
import com.wepower.wepower.Models.ConnessioneDatabase;
import com.wepower.wepower.Models.DatiSessioneCliente;
import com.wepower.wepower.Views.BannerAbbonamenti;
import com.wepower.wepower.Views.ComponentiCalendario.Calendario;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ClientDashboardController implements Initializable {
    private static ClientDashboardController instance;
    public AnchorPane containerCalendario;

    // cbhatbot
    public Button inviaButton;
    public TextField inputField;
    public TextArea chatArea;

    @FXML
    private LineChart<String, Number> graficoMassimali;

    @FXML
    private Label labelNomeUtenteSaluto;
    private double prefHieght = 200; // altezza del banner
    private double prefWidth = 350;

    // container del displayer dei banner
    public ScrollPane scrollPaneBanner;

    @FXML
    // container dei banner
    private HBox displayerBanner;

    public static ClientDashboardController getInstance() {
        return instance;
    }
    private void loadBanner() {
        // max banner visibili per volta
        int maxBannerVisibili = 3;

        // larghezza del banner e spazio tra i banner
        int bannerWidth = (int) prefWidth;

        // imposta la larghezza del conter dei banner tenendo conto degli spazi tra loro
        scrollPaneBanner.setPrefWidth((bannerWidth * maxBannerVisibili));

        // crea i banner
        ArrayList<BannerAbbonamenti> bannerini = new  ArrayList<>();
        bannerini=BannerAbbonamenti.getBannerAbbonamentiDB();
        for(int i=0; i<bannerini.size(); i++){
            // aggiungo i banner all'HBox
            displayerBanner.getChildren().add(bannerini.get(i));
        }
    }

    public void loadCalendario() {
        containerCalendario.getChildren().clear();
        VBox calendario = Calendario.creaCalendario();
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

    public void onChiediPowerino() {
        inviaButton.setOnAction(event -> {
            String userInput = inputField.getText().trim();
            if (!userInput.isEmpty()) {
                chatArea.appendText("Tu: " + userInput + "\n");
                inputField.clear();

                // Avvia un nuovo thread per lo streaming
                new Thread(() -> {
                    try {
                        OpenRouter_AI.chiediPowerinoStreaming(userInput, token -> {
                            Platform.runLater(() -> {
                                chatArea.appendText(token);
                            });
                        });
                        Platform.runLater(() -> chatArea.appendText("\n")); // Vai a capo alla fine
                    } catch (IOException | SQLException e) {
                        Platform.runLater(() -> chatArea.appendText("\n⚠ Errore di connessione con Powerino\n"));
                        e.printStackTrace();
                    }
                }).start();
            }
        });
    }

    public void controllerGraficoMassimali() throws SQLException {
        XYChart.Series<String, Number> massimale = new XYChart.Series<>();
        massimale.setName("Massimale");

        String prelevaMassimale = "SELECT Peso, DataInserimento FROM MassimaleImpostatoCliente WHERE IDCliente = ? AND NomeEsercizio = ? ORDER BY DataInserimento";

        try (Connection conn = ConnessioneDatabase.getConnection()) {
            PreparedStatement prelevamento = conn.prepareStatement(prelevaMassimale);
            prelevamento.setInt(1, DatiSessioneCliente.getIdUtente());
            prelevamento.setString(2, "Squat");
            ResultSet rs = prelevamento.executeQuery();

            while(rs.next()) {
                long dataInserimento = rs.getLong("DataInserimento");
                double peso = rs.getDouble("Peso");

                // Converti il timestamp in una stringa formattata
                String data = new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date(dataInserimento));

                // Aggiungi il punto al grafico
                massimale.getData().add(new XYChart.Data<>(data, peso));
            }
            graficoMassimali.getData().add(massimale);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instance = this;
        labelNomeUtenteSaluto.setText("Ciao, "+ DatiSessioneCliente.getNomeUtente() + "👋");
        loadCalendario();
        loadBanner();
        startAutoScroll();
        onChiediPowerino();
        try {
            controllerGraficoMassimali();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}