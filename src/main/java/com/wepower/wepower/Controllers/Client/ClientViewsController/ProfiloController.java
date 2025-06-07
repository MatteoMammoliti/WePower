package com.wepower.wepower.Controllers.Client.ClientViewsController;

import com.wepower.wepower.ControlloTemi;
import com.wepower.wepower.Models.DatiSessioneCliente;
import com.wepower.wepower.Models.Model;
import com.wepower.wepower.Views.AlertHelper;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ProfiloController implements Initializable {
    private static ProfiloController instance;

    @FXML private VBox contenitoreDati1;
    @FXML private VBox contenitoreDati2;
    @FXML private Label infoNome;
    @FXML private Label infoDataNascita;
    @FXML private Label infoEmail;
    @FXML private Label infoTelefono;
    @FXML private Label infoTipoAbbonamento;
    @FXML private Label infoDataInizio;
    @FXML private Label infoDataFine;
    @FXML private Label infoStatoPagamento;
    @FXML private Label infoPeso;
    @FXML private Label infoAltezza;
    @FXML private Label infoCertificatoMedico;
    @FXML private Label infoScheda;
    @FXML private Label titoloPrincipale;
    @FXML private Label titoloDatiAbbonamento;
    @FXML private Label titoloDatiFisici;
    @FXML private Label titoloGenerale;
    @FXML private Label titoloDatiPersonali;
    @FXML private Label labelGenere;
    @FXML private ImageView contenitoreImmagine;
    @FXML private Button pulsanteCambiaImmagine;
    @FXML private Label labelNomeCognomeSuperiore;
    @FXML private Label labelNomeCognomeInferiore;
    @FXML private Label labelDataNascita;
    @FXML private Label labelEmail;
    @FXML private Label labelTellefono;
    @FXML private Label labelStatoCertificato;
    @FXML private Label labelSchedaAllenamento;
    @FXML private Label labelTipoAbbonamento;
    @FXML private Label labelDataInizioAbbonamento;
    @FXML private Label labelDataFineAbbonamento;
    @FXML private Label labelStatoPagamento;
    @FXML private Label labelPesoAttuale;
    @FXML private Label labelAltezza;
    @FXML private Button btnModificaDati;
    @FXML private Button btnPulsanteEliminaUtente;
    @FXML private BorderPane contenitoreMioProfilo;
    @FXML private File imgProfilo;

    private File imgCertificato;

    public ProfiloController() {}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instance=this;
        Model.getInstance().setProfiloController(this);
        contenitoreMioProfilo.setFocusTraversable(false);
        contenitoreDati1.prefWidthProperty().bind(contenitoreMioProfilo.widthProperty().multiply(0.50));
        contenitoreDati2.prefWidthProperty().bind(contenitoreMioProfilo.widthProperty().multiply(0.50));

        try {
            caricaInterfacciaDatiUtente();
            testoProfiloResponsive();
        } catch (SQLException e) {
            AlertHelper.showAlert("Questo non doveva succedere", "Qualcosa è andato storto durante il caricamento della pagina", null, Alert.AlertType.ERROR);
        }
    }

    public static ProfiloController getInstance(){
        return instance;
    }

    public void caricaInterfacciaDatiUtente() throws SQLException {
        String nome = DatiSessioneCliente.getNomeUtente();
        String cognome = DatiSessioneCliente.getCognome();
        String email = DatiSessioneCliente.getEmail();
        String dataNascita = DatiSessioneCliente.getDataNascita();
        String telefono = DatiSessioneCliente.getTelefono();
        String altezza = DatiSessioneCliente.getAltezza();
        Integer pesoAttuale = DatiSessioneCliente.getPesoAttuale();
        boolean statoAbbonamento = DatiSessioneCliente.getStatoAbbonamento();
        String genere=DatiSessioneCliente.getGenere();
        //Resetto gli eventi per un eventuale reload
        labelStatoCertificato.setOnMouseClicked(null);
        labelStatoCertificato.getStyleClass().clear();
        labelStatoPagamento.setOnMouseClicked(null);
        labelStatoPagamento.getStyleClass().clear();
        labelSchedaAllenamento.getStyleClass().clear();

        labelGenere.setText(genere);
        labelNomeCognomeSuperiore.setText(nome + " " + cognome);
        labelNomeCognomeInferiore.setText(nome + " " + cognome);
        labelDataNascita.setText(dataNascita);
        labelEmail.setText(email);
        contenitoreImmagine.setPreserveRatio(true);
        Image fotoProfilo = DatiSessioneCliente.caricaImmagineProfiloUtente(DatiSessioneCliente.getIdUtente());

        if (fotoProfilo != null) {
            contenitoreImmagine.setImage(fotoProfilo);
        }

        pulsanteCambiaImmagine.setOnAction(event -> {
            try {
                cambiaImmagineProfilo();
            } catch (SQLException | IOException e) {
                AlertHelper.showAlert("Questo non doveva succedere", "Errore durante il caricamento dell'immagine profilo", null, Alert.AlertType.ERROR);
            }
        });

        if (statoAbbonamento) {
            labelTipoAbbonamento.setText(DatiSessioneCliente.getTipoAbbonamentoAttivo());
            labelDataInizioAbbonamento.setText(DatiSessioneCliente.getDataInizioAbbonamentoAttivo());
            labelDataFineAbbonamento.setText(DatiSessioneCliente.getDataFineAbbonamentoAttivo());
            labelStatoPagamento.setText("Attivo");
            labelStatoPagamento.getStyleClass().add("label_testo_scuro");
            labelStatoPagamento.getStyleClass().add("stilePredefinito");
        } else {
            labelStatoPagamento.setText("Riattiva il tuo abbonamento");
            labelStatoPagamento.getStyleClass().add("bottoni_dinamici_profilo");
            labelStatoPagamento.setOnMouseClicked(event -> {
                try {
                    onClickLabelAbbonamenti();
                } catch (IOException e) {
                    AlertHelper.showAlert("Questo non doveva succedere", "Qualcosa è andato storto", null, Alert.AlertType.ERROR);
                }
            });

            labelTipoAbbonamento.setText("Non attivo");
            labelDataInizioAbbonamento.setText("Non attivo");
            labelDataFineAbbonamento.setText("Non attivo");
        }
        labelSchedaAllenamento.getStyleClass().add("bottoni_dinamici_profilo");
        if (DatiSessioneCliente.getIdSchedaAllenamento() == 0) {
            labelSchedaAllenamento.setText("Gestisci la tua scheda");
        } else {
            labelSchedaAllenamento.setText("Visualizza la tua scheda");
            labelSchedaAllenamento.getStyleClass().add("label_testo_scuro");
            labelSchedaAllenamento.getStyleClass().add("stilePredefinito");
        }
        labelSchedaAllenamento.setOnMouseClicked(event -> onClickLabelScheda());

        if(telefono!=null) labelTellefono.setText(telefono);
        else labelTellefono.setText("Nessun numero di telefono");

        if(DatiSessioneCliente.getCertificato()==2) {
            labelStatoCertificato.setText("Certificato valido");
            labelStatoCertificato.getStyleClass().add("label_testo_scuro");
            labelStatoCertificato.getStyleClass().add("stilePredefinito");
        }else if(DatiSessioneCliente.getCertificato()==1){
            labelStatoCertificato.setText("Certificato in attesa di approvazione");
            labelStatoCertificato.getStyleClass().add("label_testo_scuro");
            labelStatoCertificato.getStyleClass().add("stilePredefinito");
        }else{
            labelStatoCertificato.setText("Carica il tuo certificato");
            labelStatoCertificato.getStyleClass().add("bottoni_dinamici_profilo");
            labelStatoCertificato.setOnMouseClicked(event -> {
                try {
                    caricaCertificato();
                } catch (SQLException | IOException e) {
                    AlertHelper.showAlert("Questo non doveva succedere", "Qualcosa è andato storto nel caricamento del certificato", null, Alert.AlertType.ERROR);
                }
            });
        }

        if(altezza!=null){
            labelAltezza.setText(altezza+" cm");
        }
        else{
            labelAltezza.setText("Nessuna altezza registrata");
        }

        if(pesoAttuale!=null){
            labelPesoAttuale.setText(pesoAttuale +" kg");
        }
        else{
            labelPesoAttuale.setText("Nessun peso registrato");
        }

        btnModificaDati.setOnAction(event -> {
            try {
                onClickModificaDati();
            } catch (IOException e) {
                AlertHelper.showAlert("Questo non doveva succedere", "Errore durante la modifica dei dati", null, Alert.AlertType.ERROR);
            }
        });

        btnPulsanteEliminaUtente.setOnAction(event -> {
            try {
                if (DatiSessioneCliente.onClickEliminaUtente(DatiSessioneCliente.getIdUtente())) {
                    Stage temp = (Stage) btnPulsanteEliminaUtente.getScene().getWindow();
                    Model.getInstance().getViewFactoryClient().closeStage(temp);
                }
            } catch (SQLException e) {
                AlertHelper.showAlert("Questo non doveva succedere", "Errore durante l'eliminazione dell'utente", null, Alert.AlertType.ERROR);
            }
        });
    }

    private void adatta_testo(Label label, double percentuale) {
        label.setStyle("-fx-font-size: " +1500*percentuale + "px;");
    }

    public void cambiaImmagineProfilo() throws SQLException, IOException {
        //Creo una finestra per la selezione dell'immagine dal computer
        FileChooser selezioneFile = new FileChooser();
        selezioneFile.setTitle("Selezione immagine profilo");
        //Creo un filtro per le immagini,possono essere selezionati solo file con queste estensioni
        selezioneFile.getExtensionFilters().add(new FileChooser.ExtensionFilter("Immagini", "*.png", "*.jpg", "*.jpeg"));
        //Apro la finestra di dialogo per la scelta del file, se l'utente annulla il risultato sarà null
        imgProfilo= selezioneFile.showOpenDialog(null);

        if(imgProfilo!=null){
            //.toURI().toString() converte il file in un  Uri leggibile per java.Un Uri serve per identiicar eil file in modo univoco
            //per esempio creano un percorso assoluto per il file.Questo è necessario poichè il costruttore di Image richiede un Uri/Url
            Image immagine=new Image(imgProfilo.toURI().toString());
            contenitoreImmagine.setImage(immagine);
            DatiSessioneCliente.salvaImmagineProfiloUtente(DatiSessioneCliente.getIdUtente(),imgProfilo);
            DatiSessioneCliente.setImmagineProfilo(immagine);
            caricaInterfacciaDatiUtente();
            Model.getInstance().getClientMenuController().caricaMenu();
        }
    }
    private void adatta_tasti(Button btn, double percentuale) {
        btn.setStyle("-fx-font-size: " +1500*percentuale + "px;");
    }
    private void testoProfiloResponsive(){
        adatta_testo(infoNome,0.014);
        adatta_testo(infoDataNascita,0.014);
        adatta_testo(infoEmail,0.014);
        adatta_testo(infoTelefono,0.014);
        adatta_testo(infoPeso,0.014);
        adatta_testo(infoAltezza,0.014);
        adatta_testo(infoTipoAbbonamento,0.014);
        adatta_testo(infoDataInizio,0.014);
        adatta_testo(infoDataFine,0.014);
        adatta_testo(infoStatoPagamento,0.014);
        adatta_testo(infoStatoPagamento,0.014);
        adatta_testo(infoScheda,0.014);
        adatta_testo(infoCertificatoMedico,0.014);

        adatta_testo(labelNomeCognomeSuperiore,0.018);
        adatta_testo(labelNomeCognomeInferiore,0.014);
        adatta_testo(labelDataNascita,0.014);
        adatta_testo(labelEmail,0.014);
        adatta_testo(labelTellefono,0.014);
        adatta_testo(labelPesoAttuale,0.014);
        adatta_testo(labelAltezza,0.014);
        adatta_testo(labelTipoAbbonamento,0.014);
        adatta_testo(labelDataInizioAbbonamento,0.014);
        adatta_testo(labelDataFineAbbonamento,0.014);
        adatta_testo(labelStatoPagamento,0.014);
        adatta_testo(labelStatoCertificato,0.014);
        adatta_testo(labelSchedaAllenamento,0.014);


        adatta_testo(labelGenere,0.011);

        adatta_testo(titoloPrincipale,0.020);
        adatta_testo(titoloDatiFisici,0.017);
        adatta_testo(titoloGenerale,0.017);
        adatta_testo(titoloDatiAbbonamento,0.017);
        adatta_testo(titoloDatiPersonali,0.017);

        adatta_tasti(btnPulsanteEliminaUtente, 0.012);
        adatta_tasti(btnModificaDati,0.012);
        adatta_tasti(pulsanteCambiaImmagine,0.008);
    }


    public void caricaCertificato() throws SQLException, IOException {
        FileChooser selezioneFile = new FileChooser();
        selezioneFile.setTitle("Selezione certificato");
        selezioneFile.getExtensionFilters().add(new FileChooser.ExtensionFilter("Immagine", "*.png", "*.jpg", "*.jpeg"));
        imgCertificato= selezioneFile.showOpenDialog(null);
        if(DatiSessioneCliente.salvaCertificatoMeidico(DatiSessioneCliente.getIdUtente(),imgCertificato)){
            DatiSessioneCliente.setCertificato(1);
            caricaInterfacciaDatiUtente();
        }

    }
    public void onClickLabelScheda(){
        Model.getInstance().getViewFactoryClient().invalidateMyProfileView();
        Model.getInstance().getViewFactoryClient().getCurrentMenuView().set("Scheda");
    }

    public void onClickLabelAbbonamenti() throws IOException {
        FXMLLoader abbonamenti = new FXMLLoader(getClass().getResource("/Fxml/Client/ClientMenuView/SchermataSelezioneAbbonamento.fxml"));
        Parent root = abbonamenti.load();

        //Ottengo il controller della schermata
        SchermataSelezioneAbbonamentoController controller = abbonamenti.getController();

        //Creo lo stage della finestra
        Stage stage=new Stage();
        stage.setTitle("Schermata selezione abbonamento");
        Scene scena=new Scene(root);

        String cssTema=getClass().getResource("/Styles/bannerSelezioneStyle.css").toExternalForm();
        ControlloTemi.getInstance().aggiungiScena(scena,cssTema);
        stage.setScene(scena);
        stage.setResizable(false);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/Images/favicon.png")));
        stage.initModality(Modality.APPLICATION_MODAL);

        //Passo lo stage al controller
        controller.setStage(stage);
        stage.showAndWait();

    }

    public void onClickModificaDati() throws IOException {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/Fxml/Client/ModificaDati.fxml"));
        Parent root=loader.load();
        Stage stage=new Stage();
        Scene scena=new Scene(root);

        String cssTema=getClass().getResource("/Styles/modificaDati.css").toExternalForm();
        ControlloTemi.getInstance().aggiungiScena(scena,cssTema);
        stage.setTitle("Modifica dati");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/Images/favicon.png")));
        stage.setScene(scena);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }
}
