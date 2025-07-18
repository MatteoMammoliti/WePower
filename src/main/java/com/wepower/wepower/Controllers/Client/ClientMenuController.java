package com.wepower.wepower.Controllers.Client;
import com.jfoenix.controls.JFXToggleButton;
import com.wepower.wepower.ControlloTemi;
import com.wepower.wepower.Models.Model;
import com.wepower.wepower.Models.DatiSessioneCliente;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.util.Objects;
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
    @FXML private JFXToggleButton pulsanteCambioTema;

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

        String temaDefault=getClass().getResource("/Styles/TemaBlu.css").toExternalForm();
        if(!Objects.equals(ControlloTemi.getInstance().getCssTemaCorrente(), temaDefault)) {
            this.pulsanteCambioTema.setSelected(true);
        }

        pulsanteCambioTema.setOnAction(e -> {
            String blutema= getClass().getResource("/Styles/TemaBlu.css").toExternalForm();
            String temaProva=getClass().getResource("/Styles/TemaVerde.css").toExternalForm();
            if(ControlloTemi.getInstance().getCssTemaCorrente().equals(blutema)){
                ControlloTemi.getInstance().cambiaTema(temaProva);
            }
            else{
                ControlloTemi.getInstance().cambiaTema(blutema);
            }
        });
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

}
