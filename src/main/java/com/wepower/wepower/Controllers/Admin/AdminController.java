package com.wepower.wepower.Controllers.Admin;

import com.wepower.wepower.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminController implements Initializable {

    public BorderPane contenitore_admin_view;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Model.getInstance().getViewFactoryAdmin().getCurrentMenuView().addListener((observableValue, attualeView, nuovaView) -> {

            switch (nuovaView) {
                case "Dashboard" -> contenitore_admin_view.setCenter(Model.getInstance().getViewFactoryAdmin().getDashboard());

                case "Utenti" -> contenitore_admin_view.setCenter(Model.getInstance().getViewFactoryAdmin().getUtentiView());

                case "Schede" -> contenitore_admin_view.setCenter(Model.getInstance().getViewFactoryAdmin().getSchedeView());

                case "Certificati" -> contenitore_admin_view.setCenter(Model.getInstance().getViewFactoryAdmin().getCertificatiView());

                default -> contenitore_admin_view.setCenter(Model.getInstance().getViewFactoryAdmin().getDashboard());
            }
        });
    }
}
