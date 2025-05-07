package com.wepower.wepower.Views;
import com.wepower.wepower.ControlloTemi;
import com.wepower.wepower.Models.DatiSessioneCliente;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class ViewFactoryClient {

    private final StringProperty currentMenuView;
    private BorderPane dashboard;
    private BorderPane scheda;
    private BorderPane prenotazioni;
    private BorderPane myProfile;
    private SplitPane contactUs;

    public ViewFactoryClient() {
        this.currentMenuView = new SimpleStringProperty("");
    }

    // otteniamo l'attuale view del menu (quella selezionata dal'utente)
    public StringProperty getCurrentMenuView() {
        return currentMenuView;
    }

    public void showLoginWindow() {

        FXMLLoader loginWindow = new FXMLLoader(getClass().getResource("/Fxml/Login.fxml"));
        Scene scene = null;

        try {
            scene = new Scene(loginWindow.load());
        } catch (Exception e) {
            AlertHelper.showAlert("Questo non doveva succedere", " Qualcosa è andato storto nel caricamento della pagina", null, Alert.AlertType.ERROR);
        }
        String cssTema=getClass().getResource("/Styles/login.css").toExternalForm();
        ControlloTemi.getInstance().aggiungiScena(scene,cssTema);

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("WePower - Login");
        stage.setResizable(false);
        stage.show();
    }

    public void showSignUpWindow() {

        FXMLLoader loginWindow = new FXMLLoader(getClass().getResource("/Fxml/SignUp.fxml"));
        Scene scene = null;

        try {
            scene = new Scene(loginWindow.load());
        } catch (Exception e) {
            AlertHelper.showAlert("Questo non doveva succedere", " Qualcosa è andato storto nel caricamento della pagina", null, Alert.AlertType.ERROR);
        }

        String cssTema= getClass().getResource("/Styles/login.css").toExternalForm();
        ControlloTemi.getInstance().aggiungiScena(scene,cssTema);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("WePower - Login");
        stage.setResizable(false);
        stage.show();
    }

    // intera dashboard riservata per il cliente (con menu)
    public void showDashboardClient() {

        FXMLLoader dashboardClient = new FXMLLoader(getClass().getResource("/Fxml/Client/ContainerClientView.fxml"));
        Scene scene = null;

        try {
            scene = new Scene(dashboardClient.load());
        } catch (Exception e) {
            AlertHelper.showAlert("Questo non doveva succedere", " Qualcosa è andato storto nel caricamento della pagina", null, Alert.AlertType.ERROR);
        }

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("WePower - Dashboard");
        stage.setMinHeight(600);
        stage.setMinWidth(1300);
        stage.setMaximized(true);

        String cssTema=getClass().getResource("/Styles/Dashboard.css").toExternalForm();
        ControlloTemi.getInstance().aggiungiScena(scene,cssTema);

        stage.centerOnScreen();
        stage.show();
        stage.maximizedProperty().addListener((obs, wasMaximized, isNowMaximized) -> {
            if (!isNowMaximized) {
                stage.setWidth(stage.getMinWidth());
                stage.setHeight(stage.getMinHeight());
                stage.centerOnScreen();
            }
        });

    }

                                                    // -- FUNZIONI DI CAMBIO VIEW DEL MENU --

    // Visualizziamo l'intera dashboard (senza menu)
    public BorderPane getDashboard() {
        if (dashboard == null) {
            try {
                dashboard = new FXMLLoader(getClass().getResource("/Fxml/Client/DashboardClient.fxml")).load();
            } catch (Exception e) {
                AlertHelper.showAlert("Questo non doveva succedere", " Qualcosa è andato storto nel caricamento della pagina", null, Alert.AlertType.ERROR);
            }
        }
        else if (dashboard.getParent() != null) {
            ((javafx.scene.layout.Pane) dashboard.getParent()).getChildren().remove(dashboard);
        }
        return dashboard;
    }

    public BorderPane getSchedaView() {
        if (scheda == null) {
            try {
                if (DatiSessioneCliente.getIdSchedaAllenamento() != 0) {
                    scheda = new FXMLLoader(getClass().getResource("/Fxml/Client/ClientMenuView/Scheda.fxml")).load();
                }
                else
                {
                    scheda = new FXMLLoader(getClass().getResource("/Fxml/Client/ClientMenuView/SchermataCreazioneScheda.fxml")).load();
                }
            } catch (Exception e) {
                AlertHelper.showAlert("Questo non doveva succedere", " Qualcosa è andato storto nel caricamento della pagina", null, Alert.AlertType.ERROR);
            }
        } else if (scheda.getParent() != null) {
            ((javafx.scene.layout.Pane) scheda.getParent()).getChildren().remove(scheda);
        }
        return scheda;
    }

    public BorderPane getPrenotazioniView() {
        if (prenotazioni == null) {
            try {
                prenotazioni = new FXMLLoader(getClass().getResource("/Fxml/Client/ClientMenuView/Prenotazioni.fxml")).load();
            } catch (Exception e) {
                AlertHelper.showAlert("Questo non doveva succedere", " Qualcosa è andato storto nel caricamento della pagina", null, Alert.AlertType.ERROR);
            }
        } else if (prenotazioni.getParent() != null) {
            ((javafx.scene.layout.Pane) prenotazioni.getParent()).getChildren().remove(prenotazioni);
        }

        return prenotazioni;
    }

    public BorderPane getMyProfileView() {
        if (myProfile == null) {
            try {
                myProfile = new FXMLLoader(getClass().getResource("/Fxml/Client/ClientMenuView/MyProfile.fxml")).load();
            } catch (Exception e) {
                AlertHelper.showAlert("Questo non doveva succedere", " Qualcosa è andato storto nel caricamento della pagina", null, Alert.AlertType.ERROR);
            }
        } else if (myProfile.getParent() != null) {
            ((javafx.scene.layout.Pane) myProfile.getParent()).getChildren().remove(myProfile);
        }
        return myProfile;
    }

    public SplitPane getContactUsView() {
        if (contactUs == null) {
            try {
                contactUs = new FXMLLoader(getClass().getResource("/Fxml/Client/ClientMenuView/ContactUs.fxml")).load();
            } catch (Exception e) {
                AlertHelper.showAlert("Questo non doveva succedere", " Qualcosa è andato storto nel caricamento della pagina", null, Alert.AlertType.ERROR);
            }
        } else if (contactUs.getParent() != null) {
            ((javafx.scene.layout.Pane) contactUs.getParent()).getChildren().remove(contactUs);
        }
        return contactUs;
    }

    // chiudiamo la finestra da cui stiamo provenendo (nella transazione Login -> Dashboard, chiudiamo la finestra di Login)
    public void closeStage(Stage stage) {
        if (stage != null) {
            stage.close();
        }
    }

    //               -- FUNZIONI DI INVALIDAZIONE E AGGIORNAMENTO DELLE VIEW --
    public void invalidateSchedaView() { // dopo la creazione della scheda
        this.scheda = null;
    }

    public void invalidateMyProfileView(){
        this.myProfile = null;
    }

    public void invalidateDashboard() {
        this.dashboard = null;
    }
}