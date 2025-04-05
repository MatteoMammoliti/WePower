package com.wepower.wepower.Controllers.Client;
import com.wepower.wepower.Models.Model;
import com.wepower.wepower.Models.DatiSessioneCliente;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientController implements Initializable {
    public BorderPane contenitore_client_view;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // il listener resta in ascolto sul menu e quando si passa da una view all'altra, esegue le istruzioni fissate
        Model.getInstance().getViewFactory().getCurrentMenuView().addListener((observable, attualeView, nuovaView) -> {

            switch (nuovaView) {
                case "Dashboard" -> contenitore_client_view.setCenter(Model.getInstance().getViewFactory().getDashboard());

                case "Logout" -> {
                    DatiSessioneCliente.logout();
                    Stage currentStage = (Stage) contenitore_client_view.getScene().getWindow();
                    Model.getInstance().getViewFactory().closeStage(currentStage);
                    Model.getInstance().getViewFactory().showLoginWindow();
                }

                default -> contenitore_client_view.setCenter(Model.getInstance().getViewFactory().getDashboard());
            }
        });
    }
}