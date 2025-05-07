package com.wepower.wepower.Controllers.Admin;
import com.wepower.wepower.Models.AdminModel.DatiSessioneAdmin;
import com.wepower.wepower.Models.ConnessioneDatabase;
import com.wepower.wepower.Models.DatiPalestra.DatiSessionePalestra;
import com.wepower.wepower.Models.Model;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminController implements Initializable {

    @FXML private BorderPane contenitore_admin_view;

    public AdminController() {}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // listener che rimane in ascolto di modifiche alla String Property della ViewFactory Admin
        // in base al valore assunto dalla string, visualizza nel contenitore dell'admin view (insieme al menu)
        Model.getInstance().getViewFactoryAdmin().getCurrentMenuView().addListener((observableValue, attualeView, nuovaView) -> {

            switch (nuovaView) {
                case "Dashboard" -> contenitore_admin_view.setCenter(Model.getInstance().getViewFactoryAdmin().getDashboard());

                case "Utenti" -> contenitore_admin_view.setCenter(Model.getInstance().getViewFactoryAdmin().getUtentiView());

                case "Schede" -> contenitore_admin_view.setCenter(Model.getInstance().getViewFactoryAdmin().getSchedeView());

                case "Certificati" -> contenitore_admin_view.setCenter(Model.getInstance().getViewFactoryAdmin().getCertificatiView());

                case "Logout" -> onLogout();

                default -> contenitore_admin_view.setCenter(Model.getInstance().getViewFactoryAdmin().getDashboard());
            }
        });
    }

    private void onLogout() {
        DatiSessioneAdmin.logout();
        DatiSessionePalestra.svuotaPrenotazioniSalaPesi();
        ConnessioneDatabase.closeConnection();
        Model.invalidate();
        Stage currentStage = (Stage) contenitore_admin_view.getScene().getWindow();
        Model.getInstance().getViewFactoryClient().closeStage(currentStage);
        Model.getInstance().getViewFactoryClient().showLoginWindow();
    }
}