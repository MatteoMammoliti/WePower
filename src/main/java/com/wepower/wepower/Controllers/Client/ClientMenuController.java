package com.wepower.wepower.Controllers.Client;
import com.wepower.wepower.Models.Model;
import com.wepower.wepower.Models.DatiSessioneCliente;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import java.net.URL;
import java.util.ResourceBundle;

public class ClientMenuController  implements Initializable {

    public Button dashboardButton;
    public Button prenotazioneButton;
    public Button parametriButton;
    public Button myProfileButton;
    public Button contactUsButton;
    public Button logoutButton;
    public Button schedaButton;
    public ImageView imageUtente;
    public Label nomeCognomeUtente;
    public Label emailUtente;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListeners();
        nomeCognomeUtente.setText(DatiSessioneCliente.getNomeUtente() + " " + DatiSessioneCliente.getCognome());
        emailUtente.setText(DatiSessioneCliente.getEmail());
    }

    // LISTENERS DEI BOTTONI DEL MENU
    private void addListeners() {
        dashboardButton.setOnAction(event -> onDashboard());
        schedaButton.setOnAction(event -> onScheda());
        prenotazioneButton.setOnAction(event -> onPrenotazione());
        parametriButton.setOnAction(event -> onParametri());
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

    private void onPrenotazione() {
        Model.getInstance().getViewFactoryClient().getCurrentMenuView().set("Prenotazione");
    }

    private void onParametri() {
        Model.getInstance().getViewFactoryClient().getCurrentMenuView().set("Parametri");
    }

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
