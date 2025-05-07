package com.wepower.wepower.Controllers.Client;
import com.wepower.wepower.ControlloTemi;
import com.wepower.wepower.Models.Model;
import com.wepower.wepower.Models.DatiSessioneCliente;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

public class ClientMenuController  implements Initializable {

    @FXML private Button dashboardButton;
    @FXML private Button prenotazioneButton;
    @FXML private Button myProfileButton;
    @FXML private Button contactUsButton;
    @FXML private Button logoutButton;
    @FXML private Button schedaButton;
    @FXML private ImageView imageUtente;
    @FXML private Label nomeCognomeUtente;
    @FXML private Label emailUtente;
    @FXML private Label labelStatoAbbonamento;
    @FXML private VBox profileSection;
    @FXML private VBox contenitorePulsantiView;
    @FXML private Button pulsanteCambioTema;

    public ClientMenuController() {}

    // funzione che carica i componenti del menu. verrà richiamata anche in fase di aggiornamento interfaccia post operazione di modifica
    public void caricaMenu() {
        nomeCognomeUtente.setText(DatiSessioneCliente.getNomeUtente() + " " + DatiSessioneCliente.getCognome());
        emailUtente.setText(DatiSessioneCliente.getEmail());

        if (DatiSessioneCliente.getStatoAbbonamento()){
            labelStatoAbbonamento.setText("Abbonamento attivo");
            labelStatoAbbonamento.setStyle("-fx-text-fill: #255a21; -fx-font-weight: bold");
        } else {
            labelStatoAbbonamento.setText("Abbonamento non attivo");
            labelStatoAbbonamento.setStyle("-fx-text-fill: red; -fx-font-weight: bold");
        }
        this.imageUtente.setImage(DatiSessioneCliente.getImmagineProfilo());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        imageUtente.fitWidthProperty().bind(
                profileSection.widthProperty().multiply(0.8)
        );
        Model.getInstance().setClientMenuController(this);
        addListeners();
        caricaMenu();
        Platform.runLater(() -> {
            Stage stage = (Stage) contenitorePulsantiView.getScene().getWindow();
            stage.widthProperty().addListener((obs, oldVal, newVal) -> aggiustaSpazioPulsanti());
            stage.heightProperty().addListener((obs, oldVal, newVal) -> aggiustaSpazioPulsanti());

            aggiustaSpazioPulsanti();
        });

        pulsanteCambioTema.setOnAction(e -> {
            String blutema= getClass().getResource("/Styles/TemaBlu.css").toExternalForm();
            String temaProva=getClass().getResource("/Styles/TemaProva.css").toExternalForm();
            if(ControlloTemi.getInstance().getCssTemaCorrente().equals(blutema)){
                ControlloTemi.getInstance().cambiaTema(temaProva);
            }
            else{
                ControlloTemi.getInstance().cambiaTema(blutema);
            }
        });
    }

    private void aggiustaSpazioPulsanti() {
        double altezza = contenitorePulsantiView.getScene().getHeight();
        double spacing=altezza*0.02;
        setSpacing(spacing);
    }

    // LISTENERS DEI BOTTONI DEL MENU
    private void addListeners() {
        dashboardButton.setOnAction(event -> onDashboard());
        schedaButton.setOnAction(event -> onScheda());
        prenotazioneButton.setOnAction(event -> onPrenotazione());
        myProfileButton.setOnAction(event -> onMyProfile());
        contactUsButton.setOnAction(event -> onContactUs());
        logoutButton.setOnAction(event -> onLogout());
    }

    // FUNZIONI DI NAVIGAZIONE
    private void onDashboard() {
        Model.getInstance().getViewFactoryClient().getCurrentMenuView().set("Dashboard");
    }

    private void onScheda() {
        Model.getInstance().getViewFactoryClient().getCurrentMenuView().set("Scheda");
    }

    private void onPrenotazione() { Model.getInstance().getViewFactoryClient().getCurrentMenuView().set("Prenotazione"); }

    private void onMyProfile() {
        Model.getInstance().getViewFactoryClient().getCurrentMenuView().set("MyProfile");
    }

    private void onContactUs() {
        Model.getInstance().getViewFactoryClient().getCurrentMenuView().set("ContactUs");
    }

    private void onLogout() {
        Model.getInstance().getViewFactoryClient().getCurrentMenuView().set("Logout");
    }

    private void setSpacing(double spacing) { contenitorePulsantiView.setSpacing(spacing); }
}
