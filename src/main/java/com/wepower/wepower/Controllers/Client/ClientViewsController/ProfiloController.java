package com.wepower.wepower.Controllers.Client.ClientViewsController;

import com.wepower.wepower.ControlloTemi;
import com.wepower.wepower.Models.DatiSessioneCliente;
import com.wepower.wepower.Models.Model;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;
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
    public VBox contenitoreDati1;
    public VBox contenitoreDati2;
    public Label infoNome;
    public Label infoDataNascita;
    public Label infoEmail;
    public Label infoTelefono;
    public Label infoTipoAbbonamento;
    public Label infoDataInizio;
    public Label infoDataFine;
    public Label infoStatoPagamento;
    public Label infoPeso;
    public Label infoAltezza;
    public Label infoCertificatoMedico;
    public Label infoScheda;
    public Label titoloPrincipale;
    public Label titoloDatiAbbonamento;
    public Label titoloDatiFisici;
    public Label titoloGenerale;
    public Label titoloDatiPersonali;

    @FXML
    private Label labelGenere;


    @FXML
    private ImageView contenitoreImmagine;
    @FXML
    private Button pulsanteCambiaImmagine;
    @FXML
    private Label labelNomeCognomeSuperiore;
    @FXML

    private Label labelNomeCognomeInferiore;
    @FXML
    private Label labelDataNascita;
    @FXML
    private Label labelEmail;
    @FXML
    private Label labelTellefono;
    @FXML
    private Label labelStatoCertificato;
    @FXML
    private Label labelSchedaAllenamento;
    @FXML
    private Label labelTipoAbbonamento;
    @FXML
    private Label labelDataInizioAbbonamento;
    @FXML
    private Label labelDataFineAbbonamento;
    @FXML
    private Label labelStatoPagamento;
    @FXML
    private Label labelPesoAttuale;
    @FXML
    private Label labelAltezza;
    @FXML
    private Button btnModificaDati;
    @FXML
    private Button btnPulsanteEliminaUtente;
    @FXML
    private BorderPane contenitoreMioProfilo;
    @FXML
    private File imgProfilo;
    private File imgCertificato;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instance=this;
        Model.getInstance().setProfiloController(this);
        contenitoreMioProfilo.setFocusTraversable(false);
        contenitoreDati1.prefWidthProperty().bind(contenitoreMioProfilo.widthProperty().multiply(0.35));
        contenitoreDati2.prefWidthProperty().bind(contenitoreMioProfilo.widthProperty().multiply(0.35));

        try {
            caricaInterfacciaDatiUtente();
            testoProfiloResponsive();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    public ProfiloController(){}


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
        labelStatoCertificato.getStyleClass().removeAll("certificatoSi", "certificatoNo","certificatoAttesa");
        labelStatoPagamento.setOnMouseClicked(null);
        labelStatoPagamento.getStyleClass().removeAll("abbonamentoSi", "abbonamentoNo");

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
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        if (statoAbbonamento) {
            labelTipoAbbonamento.setText(DatiSessioneCliente.getTipoAbbonamentoAttivo());
            labelDataInizioAbbonamento.setText(DatiSessioneCliente.getDataInizioAbbonamentoAttivo());
            labelDataFineAbbonamento.setText(DatiSessioneCliente.getDataFineAbbonamentoAttivo());
            labelStatoPagamento.setText("Attivo");
            labelStatoPagamento.getStyleClass().add("abbonamentoSi");
        } else {
            labelStatoPagamento.setText("Riattiva il tuo abbonamento");
            labelStatoPagamento.getStyleClass().add("abbonamentoNo");
            labelStatoPagamento.setOnMouseClicked(event -> {
                try {
                    onClickLabelAbbonamenti();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            labelTipoAbbonamento.setText("Non attivo");
            labelDataInizioAbbonamento.setText("Non attivo");
            labelDataFineAbbonamento.setText("Non attivo");
        }
        if (DatiSessioneCliente.getIdSchedaAllenamento() == 0) {
            labelSchedaAllenamento.setText("Gestisci la tua scheda");
            labelSchedaAllenamento.getStyleClass().add("schedaNo");
        } else {
            labelSchedaAllenamento.setText("Visualizza la tua scheda");
            labelSchedaAllenamento.getStyleClass().add("schedaSi");
        }
        labelSchedaAllenamento.setOnMouseClicked(event -> onClickLabelScheda());
        if(telefono!=null){
            labelTellefono.setText(telefono);
        }
        else{
            labelTellefono.setText("Nessun numero di telefono");
        }
        if(DatiSessioneCliente.getCertificato()==2) {
            labelStatoCertificato.setText("Certificato valido");
            labelStatoCertificato.getStyleClass().add("certificatoSi");
        }else if(DatiSessioneCliente.getCertificato()==1){
            labelStatoCertificato.setText("Certificato in attesa di approvazione");
            labelStatoCertificato.getStyleClass().add("certificatoAttesa");
        }else{
            labelStatoCertificato.setText("Carica il tuo certificato");
            labelStatoCertificato.getStyleClass().add("certificatoNo");
            labelStatoCertificato.setOnMouseClicked(event -> {
                try {
                    caricaCertificato();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
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
            labelPesoAttuale.setText(pesoAttuale.toString()+" kg");
        }
        else{
            labelPesoAttuale.setText("Nessun peso registrato");
        }



        btnModificaDati.setOnAction(event -> {
            try {
                onClickModificaDati();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        btnPulsanteEliminaUtente.setOnAction(event -> {
            try {
                if (DatiSessioneCliente.onClickEliminaUtente(DatiSessioneCliente.getIdUtente())) {
                    Stage temp = (Stage) btnPulsanteEliminaUtente.getScene().getWindow();
                    Model.getInstance().getViewFactoryClient().closeStage(temp);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
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
        stage.setScene(scena);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }
}
