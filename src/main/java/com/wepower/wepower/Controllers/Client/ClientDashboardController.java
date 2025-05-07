package com.wepower.wepower.Controllers.Client;
import com.wepower.wepower.APIs.Llama4_API;
import com.wepower.wepower.Models.DatiSessioneCliente;
import com.wepower.wepower.Models.Model;
import com.wepower.wepower.Views.AlertHelper;
import com.wepower.wepower.Views.Bannerini.BannerAbbonamenti;
import com.wepower.wepower.Views.ComponentiCalendario.Calendario;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.util.Pair;
import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ClientDashboardController implements Initializable {
    private static ClientDashboardController instance;

    // frase motivazionale
    @FXML private Label LabelParolaAttuale;
    @FXML private Label LabelProssimaParola;
    @FXML private StackPane sliderPane;

    private final String[] paroleRotazione = {"Resistente", "Potente", "Invincibile"};
    private int indiceParolaCorrente = 0;

    // contenitori principali (colonna di sinistra esclusa poichè il menu è un componente a parte)
    @FXML private VBox containerColonnaDestra;
    @FXML private VBox containerRoot;

    // sezione calendario
    @FXML private AnchorPane containerCalendario;

    // chatbot powerino
    @FXML private Button inviaButton;
    @FXML private TextField inputField;
    @FXML private ScrollPane scrollPaneChatArea;
    @FXML private VBox chatVBox;

    // sezione grafici
    @FXML private LineChart<String, Number> graficoMassimali;
    @FXML private BarChart<String, Number> graficoPresenzePalestra;
    @FXML private MenuButton choiceEsercizioScheda;
    @FXML private LineChart<String,Number> graficoPeso;
    @FXML private CategoryAxis xAxisMassimali;
    @FXML private NumberAxis yAxisMassimali;
    @FXML private NumberAxis yAxisPesoCorporeo;
    @FXML private HBox containerGrafici;

    @FXML private Label labelNomeUtenteSaluto;

    // sezione banner abbonamenti
    @FXML private ScrollPane scrollPaneBanner;
    private double prefWidth = 350;

    // container dei banner
    @FXML private HBox displayerBanner;

    public ClientDashboardController() {}

    public static ClientDashboardController getInstance() {
        return instance;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instance = this;
        Model.getInstance().setClientDashboardController(this);

        this.containerGrafici.maxHeightProperty().bind(containerRoot.heightProperty().multiply(0.5));

        labelNomeUtenteSaluto.setText("Ciao, "+ DatiSessioneCliente.getNomeUtente() + "👋");
        loadCalendario();
        loadBanner();
        startAutoScroll();

        /* SEZIONE FRASE MOTIVAZIONE */
        // clip per nascondere le parole ruotate
        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(sliderPane.widthProperty());
        clip.heightProperty().bind(sliderPane.heightProperty());
        sliderPane.setClip(clip);
        Platform.runLater(() -> {
            misuraLarghezzaParolaMassima();
            fraseMotivazionale();
        });

        this.scrollPaneChatArea.setFitToWidth(true);
        this.scrollPaneChatArea.setFitToHeight(false);
        this.scrollPaneChatArea.prefHeightProperty().bind(containerColonnaDestra.heightProperty().multiply(0.3));
        this.chatVBox.minHeightProperty().bind(scrollPaneChatArea.heightProperty().multiply(1));
        this.chatVBox.setFillWidth(true);

        String msg = "Ciao " + DatiSessioneCliente.getNomeUtente() + ", sono Powerino il chat bot virtuale di WePower." + "\n";
        HBox messaggioIniziale =  new HBox(10);
        Label messaggioInizialeApertura = new Label(msg);
        messaggioInizialeApertura.maxWidthProperty().bind(scrollPaneChatArea.widthProperty().subtract(50));
        VBox.setVgrow(messaggioInizialeApertura, Priority.ALWAYS);
        messaggioInizialeApertura.getStyleClass().add("rispostaBot");
        messaggioInizialeApertura.setWrapText(true);
        messaggioIniziale.getChildren().addAll(messaggioInizialeApertura);
        messaggioIniziale.setAlignment(Pos.CENTER);

        VBox.setVgrow(messaggioIniziale, Priority.ALWAYS);
        this.chatVBox.getChildren().add(messaggioIniziale);
        this.inviaButton.setOnAction(event -> onChiediPowerino());

        caricaEserciziSchedaMenuGrafico();
        loadGraficoPrenotazioni();
        loadGraficoPeso();
        Platform.runLater(this::loadAlert);
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
        ArrayList<String> esercizi = DatiSessioneCliente.getEserciziConMassimale();

        if (!esercizi.isEmpty()) {
            for (String esercizio : esercizi) {
                MenuItem menuItem = new MenuItem(esercizio);
                menuItem.setOnAction(event -> {
                    choiceEsercizioScheda.setText(menuItem.getText());
                    loadGraficoMassimali(esercizio);
                });
                choiceEsercizioScheda.getItems().add(menuItem);
            }
            return;
        }
        choiceEsercizioScheda.setVisible(false); // se non esistono esercizi con massimali, il menu di scelta non viene visualizato
    }

    public void loadGraficoMassimali(String esercizio) {
        XYChart.Series<String, Number> massimale = new XYChart.Series<>();
        massimale.setName("Andamento massimale dell'esercizio " + esercizio);

        ArrayList<Pair<String,Number>> lista = DatiSessioneCliente.caricaStoricoMassimalePerEsercizio(esercizio, DatiSessioneCliente.getIdUtente());

        if(lista.isEmpty()) return;

        Pair<String, Number> min = lista.getFirst();
        Pair<String, Number> max = lista.getLast();
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

    public void loadGraficoPrenotazioni() {
        XYChart.Series<String, Number> prenotazioni = new XYChart.Series<>();
        prenotazioni.setName("Andamento prenotazioni");

        ArrayList<Pair<String,Number>> lista = DatiSessioneCliente.caricaStoricoPrenotazioni(DatiSessioneCliente.getIdUtente());

        if(lista.isEmpty()) return;

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

            // aggiunta del punto al grafico
            peso.getData().add(new XYChart.Data<>(data, pesoValore));
        }

        if (!storico.isEmpty()) {
            Pair<String, Integer> min = storico.getFirst();
            Pair<String, Integer> max = storico.getLast();
            int minY = min.getValue();
            int maxY = max.getValue();
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
                        AlertHelper.showAlert("Attenzione", "Il tuo abbonamento sta per scadere", "Il tuo abbonamento scade tra " + giorniDifferenza + " giorni", Alert.AlertType.WARNING);
                        DatiSessioneCliente.setAlertScadenzaAbbonamento(true);
                    });
                } else if (giorniDifferenza <= 0) {
                    Platform.runLater(() -> {
                        AlertHelper.showAlert("Attenzione", "Il tuo abbonamento non è attivo", "Abbonati per continuare ad allenarti", Alert.AlertType.WARNING);
                        DatiSessioneCliente.setAlertScadenzaAbbonamento(true);
                    });
                }

                int idCertifiato = DatiSessioneCliente.caricaPresenzaCertificato(DatiSessioneCliente.getIdUtente());

                if (!DatiSessioneCliente.getAlertCertificatoMancante()) {
                    if (idCertifiato == 0) {
                        Platform.runLater(() -> {
                            AlertHelper.showAlert("Attenzione", "Certificato medico non caricato", "Caricare il certificato medico per allenarti", Alert.AlertType.WARNING);
                            DatiSessioneCliente.setAlertCertificatoMancante(true);
                        });
                    }
                }
            } else {
                Platform.runLater(() -> {
                    AlertHelper.showAlert("Attenzione", "Il tuo abbonamento non è attivo", "Abbonati per continuare ad allenarti", Alert.AlertType.WARNING);
                    DatiSessioneCliente.setAlertScadenzaAbbonamento(true);

                });
            }
        }
    }

    // -- SEZIONE POWERINO
    private void onChiediPowerino() {
        String messaggioUtente = this.inputField.getText();
        if (messaggioUtente.isEmpty()) return;

        HBox messaggio = new HBox(10);
        messaggio.setMaxWidth(Double.MAX_VALUE);
        messaggio.setAlignment(Pos.CENTER_RIGHT);

        Label messaggioLabel = new Label(messaggioUtente);
        messaggioLabel.maxWidthProperty().bind(scrollPaneChatArea.widthProperty().subtract(50));
        messaggioLabel.setWrapText(true);
        messaggioLabel.getStyleClass().add("rispostaBot");
        messaggio.getChildren().addAll(messaggioLabel);

       VBox.setVgrow(messaggio, Priority.ALWAYS);
        this.chatVBox.getChildren().add(messaggio);
        chatVBox.heightProperty().addListener((observable, oldValue, newValue) -> {
            scrollPaneChatArea.setVvalue(1.0);
        });
        this.inputField.clear();

        Platform.runLater(() -> {
            scrollPaneChatArea.layout();
            scrollPaneChatArea.setVvalue(1.0);
        });


        Llama4_API.sendMessage(messaggioUtente, risposta -> {
            Platform.runLater(() -> {

                HBox messaggioPowerino = new HBox(10);
                messaggioPowerino.setFillHeight(true);
                messaggioPowerino.setAlignment(Pos.CENTER_LEFT);

                Label rispostaPowerino = new Label(risposta);
                rispostaPowerino.setWrapText(true);
                rispostaPowerino.getStyleClass().add("rispostaBot");
                rispostaPowerino.maxWidthProperty().bind(scrollPaneChatArea.widthProperty().subtract(50));
                messaggioPowerino.getChildren().addAll(rispostaPowerino);

                VBox.setVgrow(messaggioPowerino, Priority.ALWAYS);
                this.chatVBox.getChildren().add(messaggioPowerino);
                chatVBox.heightProperty().addListener((observable, oldValue, newValue) -> {
                    scrollPaneChatArea.setVvalue(1.0);
                });
                scrollPaneChatArea.layout();
                scrollPaneChatArea.setVvalue(1.0);
            });
        });
    }

    // SEZIONE FRASE MOTIVAZIONALE

    // scorro ogni parola dell'array e ne prelevo la larghezza. la larghezza della label sarà quella massima tra queste
    // nella misuraLarghezzaParolaMassima tengo in considerazione font e font size applicato nel css
    private void misuraLarghezzaParolaMassima() {
        double maxLarghezza = 0;
        Text misura = new Text();
        misura.setFont(LabelParolaAttuale.getFont());

        for (String s : paroleRotazione) {
            misura.setText(s);
            misura.applyCss();      // forzo l'applicazione del css perchè devo tenere conto della font-size
            maxLarghezza = Math.max(maxLarghezza,
                    misura.getLayoutBounds().getWidth());
        }
        sliderPane.setPrefWidth(maxLarghezza);
        sliderPane.setMinWidth(maxLarghezza);
        sliderPane.setMaxWidth(maxLarghezza);
    }

    private void animazioneRotazione(double altezza) {
        int prossimoIndice = (indiceParolaCorrente + 1) % paroleRotazione.length;
        LabelProssimaParola.setText(paroleRotazione[prossimoIndice]);
        LabelProssimaParola.setTranslateY(altezza); // imposto la prossima parola in modo che sia appena sotto l'attuale

        // la parola attuale scorre verso l'alto fuori l'area del clip
        TranslateTransition ruotaParolaAttuale = new TranslateTransition(Duration.seconds(0.5), LabelParolaAttuale);
        ruotaParolaAttuale.setFromY(0);
        ruotaParolaAttuale.setToY(-altezza);

        // la prossima parola scorre dalla sua altezza fino allo 0 (viene visualizzata)
        TranslateTransition ruotaProssimaParola = new TranslateTransition(Duration.seconds(0.5), LabelProssimaParola);
        ruotaProssimaParola.setFromY(altezza);
        ruotaProssimaParola.setToY(0);

        ParallelTransition animazioneSlide = new ParallelTransition(ruotaParolaAttuale, ruotaProssimaParola);
        animazioneSlide.setOnFinished(event -> {
            Label tmp = LabelParolaAttuale;
            LabelParolaAttuale = LabelProssimaParola;
            LabelProssimaParola = tmp;

            LabelProssimaParola.setTranslateY(altezza);
            sliderPane.getChildren().setAll(LabelProssimaParola, LabelParolaAttuale);
            indiceParolaCorrente = prossimoIndice;
        });
        animazioneSlide.play();
    }

    private void fraseMotivazionale() {
        LabelParolaAttuale.setText(paroleRotazione[indiceParolaCorrente]);
        LabelProssimaParola.setText("");

        double altezza = LabelParolaAttuale.getFont().getSize() * 1.2;
        sliderPane.setPrefHeight(altezza);
        sliderPane.setMinHeight(altezza);
        sliderPane.setMaxHeight(altezza);

        misuraLarghezzaParolaMassima();

        Timeline rotazione = new Timeline(
                new KeyFrame(Duration.seconds(3), e -> animazioneRotazione(altezza))
        );
        rotazione.setCycleCount(Animation.INDEFINITE);
        rotazione.play();
    }
}