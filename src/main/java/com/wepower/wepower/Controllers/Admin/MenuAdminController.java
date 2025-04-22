package com.wepower.wepower.Controllers.Admin;

import com.wepower.wepower.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class MenuAdminController implements Initializable {


    public Button certificatiButton;
    public Button schedeButton;
    public Button usersButton;
    public Button dashboardButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListeners();
    }

    // LISTENERS DEI BOTTONI DEL MENU
    private void addListeners() {
        dashboardButton.setOnAction(event -> onDashboard());
        usersButton.setOnAction(event -> onUtenti());
        schedeButton.setOnAction(event -> onSchede());
        certificatiButton.setOnAction(event -> onCertificati());
    }

    // FUNZIONI DI NAVIGAZIONE
    private void onDashboard() { Model.getInstance().getViewFactoryAdmin().getCurrentMenuView().set("Dashboard"); }
    private void onUtenti() { Model.getInstance().getViewFactoryAdmin().getCurrentMenuView().set("Utenti"); }
    private void onSchede() { Model.getInstance().getViewFactoryAdmin().getCurrentMenuView().set("Schede"); }
    private void onCertificati() { Model.getInstance().getViewFactoryAdmin().getCurrentMenuView().set("Certificati"); }
}
