package com.wepower.wepower.Controllers.Client;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wepower.wepower.APIs.Llama4_API;
import com.wepower.wepower.Models.DatiSessioneCliente;
import com.wepower.wepower.Models.Model;
import com.wepower.wepower.Views.Bannerini.BannerAbbonamenti;
import com.wepower.wepower.Views.ComponentiCalendario.Calendario;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import javafx.util.Pair;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class ClientDashboardController implements Initializable {
    private static ClientDashboardController instance;

    @FXML
    private VBox containerRoot;

    // sezione calendario
    @FXML
    private AnchorPane containerCalendario;
    @FXML
    private  RadioButton btnPrenotato;
    @FXML
    private RadioButton btnPalestraChiusa;

    // chatbot powerino
    @FXML
    private Button inviaButton;
    @FXML
    private TextField inputField;
    @FXML
    private TextArea chatArea;

    private final Llama4_API powerino = new Llama4_API();
    private final List<Map<String, String>> chatHistory = new ArrayList<>();



    // sezione grafici
    @FXML
    private LineChart<String, Number> graficoMassimali;
    @FXML
    private BarChart<String, Number> graficoPresenzePalestra;
    @FXML
    private MenuButton choiceEsercizioScheda;
    @FXML
    private LineChart<String,Number> graficoPeso;
    @FXML
    private CategoryAxis xAxisMassimali;
    @FXML
    private NumberAxis yAxisMassimali;
    @FXML
    private NumberAxis yAxisPesoCorporeo;
    @FXML
    private HBox containerGrafici;

    @FXML
    private Label labelNomeUtenteSaluto;

    // sezione banner abbonamenti
    @FXML
    private ScrollPane scrollPaneBanner;
    private double prefWidth = 350;
    @FXML
    // container dei banner
    private HBox displayerBanner;

    public static ClientDashboardController getInstance() {
        return instance;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instance = this;
        Model.getInstance().setClientDashboardController(this);
        this.containerGrafici.maxHeightProperty().bind(containerRoot.heightProperty().multiply(0.5));
        labelNomeUtenteSaluto.setText("Ciao, "+ DatiSessioneCliente.getNomeUtente() + "👋");
        btnPalestraChiusa.setDisable(true);
        btnPrenotato.setDisable(true);
        loadCalendario();
        loadBanner();
        startAutoScroll();

        this.inviaButton.setOnAction(event -> onChiediPowerino());

        caricaEserciziSchedaMenuGrafico();
        loadGraficoPrenotazioni();
        loadGraficoPeso();
        loadAlert();
    }

    // -- FUNZIONI DISPLAYER COMPONENTI DELLA DASHBOARD

    // SEZIONE BANNER
    private void loadBanner() {
        // max banner visibili per volta
        int maxBannerVisibili = 3;

        // larghezza del banner e spazio tra i banner
        int bannerWidth = (int) prefWidth;

        // imposta la larghezza del conter dei banner tenendo conto degli spazi tra loro
        scrollPaneBanner.setPrefWidth((bannerWidth * maxBannerVisibili));

        // crea i banner
        ArrayList<BannerAbbonamenti> bannerini = BannerAbbonamenti.getBannerAbbonamentiDB();
        displayerBanner.getChildren().addAll(bannerini);
    }

    private void startAutoScroll() {
        // oggetto che esegue animazioni ad intervalli regolari fissati
        Timeline timeline = new Timeline(

                // azione eseguita ad ogni intervallo di 3 secondi
                new KeyFrame(Duration.millis(40), event -> {
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

    // SEZIONE CALENDARIO
    public void loadCalendario() {
        containerCalendario.getChildren().clear();
        VBox calendario = Calendario.creaCalendario();
        containerCalendario.getChildren().add(calendario);
    }

    // SEZIONE GRAFICI
    public void caricaEserciziSchedaMenuGrafico() {
        ArrayList<String> temp = DatiSessioneCliente.getEserciziConMassimale();

        if (!temp.isEmpty()) {
            for (String esercizio : temp) {
                MenuItem menuItem = new MenuItem(esercizio);
                menuItem.setOnAction(event -> {
                    choiceEsercizioScheda.setText(menuItem.getText());
                    loadGraficoMassimali(esercizio);
                });
                choiceEsercizioScheda.getItems().add(menuItem);
            }
        }
        else {
            choiceEsercizioScheda.setVisible(false);
        }
    }

    public void loadGraficoMassimali(String esercizio) {
        XYChart.Series<String, Number> massimale = new XYChart.Series<>();
        massimale.setName("Andamento massimale dell'esercizio " + esercizio);

        ArrayList<Pair<String,Number>> lista = DatiSessioneCliente.caricaStoricoMassimalePerEsercizio(esercizio, DatiSessioneCliente.getIdUtente());
        Pair<String, Number> min = lista.get(0);
        Pair<String, Number> max = lista.get(lista.size()-1);
        int minY = min.getValue().intValue();
        int maxY = max.getValue().intValue();

        for (Pair<String, Number> p : lista) {
            massimale.getData().add(new XYChart.Data<>(p.getKey(), p.getValue()));
        }
        yAxisMassimali.setLowerBound(minY - 5);
        yAxisMassimali.setUpperBound(maxY + 5);
        yAxisMassimali.setAutoRanging(false);
        yAxisMassimali.setTickUnit(1);
        xAxisMassimali.setTickLabelRotation(90);
        xAxisMassimali.setTickLabelGap(10);
        xAxisMassimali.setAutoRanging(true);
        xAxisMassimali.setAnimated(false);

        graficoMassimali.getData().clear();
        graficoMassimali.getData().add(massimale);
    }

    private void loadGraficoPrenotazioni() {
        XYChart.Series<String, Number> prenotazioni = new XYChart.Series<>();
        prenotazioni.setName("Andamento prenotazioni");

        ArrayList<Pair<String,Number>> lista = DatiSessioneCliente.caricaStoricoPrenotazioni(DatiSessioneCliente.getIdUtente());

        for(Pair<String, Number> p : lista) {
            prenotazioni.getData().add(new XYChart.Data<>(p.getKey(), p.getValue()));
        }

        graficoPresenzePalestra.getData().clear();
        graficoPresenzePalestra.getData().add(prenotazioni);
    }

    public void loadGraficoPeso(){
        ArrayList<Pair<String,Integer>> storico = DatiSessioneCliente.caricaStroicoPesi(DatiSessioneCliente.getIdUtente());
        XYChart.Series<String, Number> peso = new XYChart.Series<>();


        peso.setName("Andamento peso corporeo");
        for(int i=0;i<storico.size();i++){
            String data = storico.get(i).getKey();
            int pesoValore = storico.get(i).getValue();

            // Aggiungi il punto al grafico
            peso.getData().add(new XYChart.Data<>(data, pesoValore));
        }

        if (!storico.isEmpty()) {
            Pair<String, Integer> min = storico.get(0);
            Pair<String, Integer> max = storico.get(storico.size()-1);
            int minY = min.getValue().intValue();
            int maxY = max.getValue().intValue();
            yAxisPesoCorporeo.setLowerBound(minY - 5);
            yAxisPesoCorporeo.setUpperBound(maxY + 5);
        }

        yAxisPesoCorporeo.setAutoRanging(false);
        yAxisPesoCorporeo.setTickUnit(1);

        graficoPeso.getData().clear();
        graficoPeso.getData().add(peso);
    }

    // SEZIONE ALERT
    private void loadAlert() {
        String dataScadenza = DatiSessioneCliente.caricaDataScadenzaAbbonamento(DatiSessioneCliente.getIdUtente());

            if (!DatiSessioneCliente.getAlertScadenzaAbbonamento()) {
                if (dataScadenza != null) {
                    LocalDate dataFineAbbonamento = LocalDate.parse(dataScadenza);
                    LocalDate dataCorrente = LocalDate.now();
                    long giorniDifferenza = ChronoUnit.DAYS.between(dataCorrente, dataFineAbbonamento);

                    if (giorniDifferenza <= 7 && giorniDifferenza > 0) {
                        Platform.runLater(() -> {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Attenzione");
                            alert.setHeaderText("Il tuo abbonamento sta per scadere!");
                            alert.setContentText("Il tuo abbonamento scade tra " + giorniDifferenza + " giorni.");
                            alert.showAndWait();
                            DatiSessioneCliente.setAlertScadenzaAbbonamento(true);
                        });
                    } else if (giorniDifferenza <= 0) {
                        Platform.runLater(() -> {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Attenzione");
                            alert.setHeaderText("Il tuo abbonamento non è attivo!");
                            alert.setContentText("Abbonati per continuare ad allenarti.");
                            alert.showAndWait();
                            DatiSessioneCliente.setAlertScadenzaAbbonamento(true);
                        });
                    }
                } else {
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Attenzione");
                        alert.setHeaderText("Il tuo abbonamento non è attivo!");
                        alert.setContentText("Abbonati per continuare ad allenarti.");
                        alert.showAndWait();
                        DatiSessioneCliente.setAlertScadenzaAbbonamento(true);
                    });
                }
            }

        int idCertifiato = DatiSessioneCliente.caricaPresenzaCertificato(DatiSessioneCliente.getIdUtente());

        if (!DatiSessioneCliente.getAlertCertificatoMancante()) {
            if (idCertifiato == 0) {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Attenzione");
                    alert.setHeaderText("Certificato medico mancante!");
                    alert.setContentText("Devi caricare il certificato medico per continuare ad allenarti.");
                    alert.showAndWait();
                    DatiSessioneCliente.setAlertCertificatoMancante(true);
                });
            }
        }
    }

    // -- SEZIONE POWERINO
    private void estraiEserciziPalestra() {

    }
    private void onChiediPowerino() {
        String messaggioUtente = this.inputField.getText();
        this.inputField.clear();
        this.chatArea.appendText("Tu: " + messaggioUtente + "\n");
        Llama4_API.sendMessage(messaggioUtente, this.chatArea);
    }
}