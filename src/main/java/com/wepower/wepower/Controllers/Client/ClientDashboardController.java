package com.wepower.wepower.Controllers.Client;

import com.wepower.wepower.APIs.OpenRouter_AI;
import com.wepower.wepower.Models.ConnessioneDatabase;
import com.wepower.wepower.Models.DatiSessioneCliente;
import com.wepower.wepower.Views.Bannerini.BannerAbbonamenti;
import com.wepower.wepower.Views.ComponentiCalendario.Calendario;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import javafx.util.Pair;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ClientDashboardController implements Initializable {
    private static ClientDashboardController instance;
    public AnchorPane containerCalendario;

    // chatbot
    public Button inviaButton;
    public TextField inputField;
    public TextArea chatArea;


    @FXML
    private  RadioButton btnPrenotato;
    @FXML
    private RadioButton btnPalestraChiusa;


    // grafici
    @FXML
    private LineChart<String, Number> graficoMassimali;
    @FXML
    private BarChart<String, Number> graficoPresenzePalestra;
    @FXML
    private MenuButton choiceEsercizioScheda;
    @FXML
    private LineChart<String,Number> graficoPeso;

    @FXML
    private Label labelNomeUtenteSaluto;
    // private double prefHieght = 200;  altezza del banner
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

    public void caricaEserciziSchedaMenuGrafico() throws SQLException {
        ArrayList<String> temp = DatiSessioneCliente.getEserciziConMassimale();

        if (temp != null) {
            for (String esercizio : temp) {
                MenuItem menuItem = new MenuItem(esercizio);
                menuItem.setOnAction(event -> {
                    try {
                        choiceEsercizioScheda.setText(menuItem.getText());
                        loadGraficoMassimali(esercizio);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
                choiceEsercizioScheda.getItems().add(menuItem);
            }
        }
    }

    public void loadGraficoMassimali(String esercizio) throws SQLException {
        XYChart.Series<String, Number> massimale = new XYChart.Series<>();
        massimale.setName("Andamento massimale dell'esercizio " + esercizio);

        String prelevaMassimale = "SELECT Peso, DataInserimento FROM MassimaleImpostatoCliente WHERE IDCliente = ? AND NomeEsercizio = ? ORDER BY DataInserimento";

        try (Connection conn = ConnessioneDatabase.getConnection()) {
            PreparedStatement prelevamento = conn.prepareStatement(prelevaMassimale);
            prelevamento.setInt(1, DatiSessioneCliente.getIdUtente());
            prelevamento.setString(2, esercizio);
            ResultSet rs = prelevamento.executeQuery();

            while(rs.next()) {
                long dataInserimento = rs.getLong("DataInserimento");
                double peso = rs.getDouble("Peso");

                // Converti il timestamp in una stringa formattata
                String data = new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date(dataInserimento));

                // Aggiungi il punto al grafico
                massimale.getData().add(new XYChart.Data<>(data, peso));
            }
            graficoMassimali.getData().clear();
            graficoMassimali.getData().add(massimale);
        }
    }

    private void loadGraficoPrenotazioni() throws SQLException {
        XYChart.Series<String, Number> prenotazioni = new XYChart.Series<>();
        prenotazioni.setName("Andamento prenotazioni");

        String prelevaPrenotazioni = "SELECT SUBSTR(DataPrenotazione, 1, 7) AS Mese, COUNT(*) AS numeroPrenotazioni FROM PrenotazioneSalaPesi WHERE IdCliente = ? GROUP BY Mese ORDER BY Mese";

        try (Connection conn = ConnessioneDatabase.getConnection()) {
            PreparedStatement prelevamento = conn.prepareStatement(prelevaPrenotazioni);
            prelevamento.setInt(1, DatiSessioneCliente.getIdUtente());
            ResultSet rs = prelevamento.executeQuery();

            while(rs.next()) {
                String mese = rs.getString("Mese");
                int cont = rs.getInt("numeroPrenotazioni");

                prenotazioni.getData().add(new XYChart.Data<>(mese, cont));
            }
            graficoPresenzePalestra.getData().clear();
            graficoPresenzePalestra.getData().add(prenotazioni);
        }
    }

    public void loadGraficoPeso(){
        ArrayList<Pair<String,Integer>> storico=DatiSessioneCliente.caricaStroicoPesi(DatiSessioneCliente.getIdUtente());
        XYChart.Series<String, Number> peso = new XYChart.Series<>();

        peso.setName("Andamento peso corporeo");
        for(int i=0;i<storico.size();i++){
            String data = storico.get(i).getKey();
            int pesoValore = storico.get(i).getValue();

            // Aggiungi il punto al grafico
            peso.getData().add(new XYChart.Data<>(data, pesoValore));
        }
        graficoPeso.getData().clear();
        graficoPeso.getData().add(peso);


    }

    private void loadAlert() {

        String fetchDataScadenza = "SELECT DataFineAbbonamento FROM AbbonamentoCliente WHERE IdCliente = ? AND StatoAbbonamento = 1";
        String fetchCertificato = "SELECT IdCertificato FROM Certificato WHERE IdCliente = ?";

        try (Connection conn = ConnessioneDatabase.getConnection()) {
            PreparedStatement prelevamento = conn.prepareStatement(fetchDataScadenza);
            prelevamento.setInt(1, DatiSessioneCliente.getIdUtente());
            ResultSet rs = prelevamento.executeQuery();

            if (!DatiSessioneCliente.getAlertScadenzaAbbonamento()) {
                if (rs.next()) {
                    LocalDate dataFineAbbonamento = LocalDate.parse(rs.getString("DataFineAbbonamento"));
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

            PreparedStatement prelevamentoCertificato = conn.prepareStatement(fetchCertificato);
            prelevamentoCertificato.setInt(1, DatiSessioneCliente.getIdUtente());
            ResultSet rsCertificato = prelevamentoCertificato.executeQuery();

            if (rsCertificato.next() && !DatiSessioneCliente.getAlertCertificatoMancante()) {
                int idCertificato = rsCertificato.getInt("IdCertificato");
                System.out.println("ID Certificato: " + idCertificato);
                if (idCertificato == 0) {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instance = this;
        labelNomeUtenteSaluto.setText("Ciao, "+ DatiSessioneCliente.getNomeUtente() + "👋");
        btnPalestraChiusa.setDisable(true);
        btnPrenotato.setDisable(true);

        loadCalendario();
        loadBanner();
        startAutoScroll();
        onChiediPowerino();
        try {
            caricaEserciziSchedaMenuGrafico();
            loadGraficoPrenotazioni();
            loadGraficoPeso();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        loadAlert();
    }
}