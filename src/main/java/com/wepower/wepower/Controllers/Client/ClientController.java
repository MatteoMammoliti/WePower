package com.wepower.wepower.Controllers.Client;
import com.wepower.wepower.Models.ConnessioneDatabase;
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
        Model.getInstance().getViewFactoryClient().getCurrentMenuView().addListener((observable, attualeView, nuovaView) -> {

            switch (nuovaView) { // nello switch settiamo la StringProperty su cui c'è un listener in ascolto di sue modifiche
                case "Dashboard" -> contenitore_client_view.setCenter(Model.getInstance().getViewFactoryClient().getDashboard());

                case "Logout" -> {
                    ConnessioneDatabase.closeConnection();
                    DatiSessioneCliente.logout();
                    Stage currentStage = (Stage) contenitore_client_view.getScene().getWindow();
                    Model.getInstance().getViewFactoryClient().closeStage(currentStage);
                    Model.getInstance().getViewFactoryClient().showLoginWindow();
                }

                case "Scheda" -> contenitore_client_view.setCenter(Model.getInstance().getViewFactoryClient().getSchedaView());

                case "Prenotazione" -> contenitore_client_view.setCenter(Model.getInstance().getViewFactoryClient().getPrenotazioniView());

                case "MyProfile" -> contenitore_client_view.setCenter(Model.getInstance().getViewFactoryClient().getMyProfileView());

                case "ContactUs" -> contenitore_client_view.setCenter(Model.getInstance().getViewFactoryClient().getContactUsView());

                default -> contenitore_client_view.setCenter(Model.getInstance().getViewFactoryClient().getDashboard());
            }
        });
    }
}