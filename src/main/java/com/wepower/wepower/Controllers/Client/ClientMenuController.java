package com.wepower.wepower.Controllers.Client;

import com.wepower.wepower.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListeners();
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
        Model.getInstance().getViewFactory().getCurrentMenuView().set("Dashboard");
    }

    private void onScheda() {
        Model.getInstance().getViewFactory().getCurrentMenuView().set("Scheda");
    }

    private void onPrenotazione() {
        Model.getInstance().getViewFactory().getCurrentMenuView().set("Prenotazione");
    }

    private void onParametri() {
        Model.getInstance().getViewFactory().getCurrentMenuView().set("Parametri");
    }

    private void onMyProfile() {
        Model.getInstance().getViewFactory().getCurrentMenuView().set("MyProfile");
    }

    private void onContactUs() {
        Model.getInstance().getViewFactory().getCurrentMenuView().set("ContactUs");
    }

    private void onLogout() {
        Model.getInstance().getViewFactory().getCurrentMenuView().set("Logout");
    }
}
