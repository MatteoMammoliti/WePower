package com.wepower.wepower.Controllers.Client.ClientViewsController;

import com.wepower.wepower.Models.DatiSessioneCliente;
import com.wepower.wepower.Models.Model;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;
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
    @FXML
    private Label labelGenere;
    @FXML
    private ImageView contenitoreImmagine;
    @FXML
    private Button pulsanteCambiaImmagine;
    @FXML
    private Label labelNomeCognomeSuperiore;
    @FXML
    private Label statoAbbonamentoSuperiore;
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
    private Label labelPercentualeGrasso;
    @FXML
    private Button btnModificaDati;
    @FXML
    private Button btnPulsanteEliminaUtente;
    @FXML
    private BorderPane contenitoreMioProfilo;
    private File imgProfilo;
    private File imgCertificato;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instance=this;
        Model.getInstance().setProfiloController(this);
        contenitoreMioProfilo.setFocusTraversable(false);
        try {
            caricaInterfacciaDatiUtente();
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
        String pesoAttuale = DatiSessioneCliente.getPesoAttuale();
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
            System.out.println("sono qui si");
            statoAbbonamentoSuperiore.setText("Abbonamento: Attivo");
            statoAbbonamentoSuperiore.setStyle("-fx-text-fill: green");
            labelTipoAbbonamento.setText(DatiSessioneCliente.getTipoAbbonamentoAttivo());
            labelDataInizioAbbonamento.setText(DatiSessioneCliente.getDataInizioAbbonamentoAttivo());
            labelDataFineAbbonamento.setText(DatiSessioneCliente.getDataFineAbbonamentoAttivo());
            labelStatoPagamento.setText("Attivo");
            labelStatoPagamento.getStyleClass().add("abbonamentoSi");
        } else {
            System.out.println("sono qui no");
            statoAbbonamentoSuperiore.setText("Abbonamento: Non Attivo");
            statoAbbonamentoSuperiore.setStyle("-fx-text-fill: red");
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
            labelSchedaAllenamento.setText("Componi o richiedi la tua scheda allenamento");
            labelSchedaAllenamento.getStyleClass().add("schedaNo");
        } else {
            labelSchedaAllenamento.setText("Visualizza la tua scheda attiva");
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
            labelAltezza.setText(altezza);
        }
        else{
            labelAltezza.setText("Nessuna altezza registrata");
        }

        if(pesoAttuale!=null){
            labelPesoAttuale.setText(pesoAttuale);
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

    public void caricaCertificato() throws SQLException, IOException {
        FileChooser selezioneFile = new FileChooser();
        selezioneFile.setTitle("Selezione certificato");
        selezioneFile.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
        imgCertificato= selezioneFile.showOpenDialog(null);
        DatiSessioneCliente.salvaCertificatoMeidico(DatiSessioneCliente.getIdUtente(),imgCertificato);
        DatiSessioneCliente.setCertificato(1);
        caricaInterfacciaDatiUtente();
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
        stage.setTitle("Modifica dati");
        stage.setScene(scena);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }
}
