package com.wepower.wepower.Views;

import com.wepower.wepower.ControlloTemi;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class ViewFactoryAdmin {

    private final StringProperty currentMenuView;
    private BorderPane dashboard;
    private BorderPane utenti;
    private BorderPane schede;
    private BorderPane certificati;

    public ViewFactoryAdmin() {
        currentMenuView = new SimpleStringProperty("");
    }

    public StringProperty getCurrentMenuView() {
        return currentMenuView;
    }

    public void showDashboardAdmin() {
        FXMLLoader dashboardAdmin = new FXMLLoader(getClass().getResource("/Fxml/Admin/ContainerAdminView.fxml"));
        Scene scene = null;

        try {

            scene = new Scene(dashboardAdmin.load());
        } catch (Exception e) {
            AlertHelper.showAlert("Questo non doveva succedere", " Qualcosa è andato storto nel caricamento della pagina", null, Alert.AlertType.ERROR);
        }

        String cssTema=getClass().getResource("/Styles/dashboardAdmin.css").toExternalForm();
        ControlloTemi.getInstance().aggiungiScena(scene, cssTema);

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("WePower - Dashboard");
        stage.setMaximized(true);
        stage.setMinHeight(600);
        stage.setMinWidth(1300);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/Images/favicon.png")));

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

                        // -- FUNZIONI DI CAMBIO DI VIEW DEL MENU --

    public BorderPane getDashboard() {
        if (dashboard == null) {
            try {
                dashboard = new FXMLLoader(getClass().getResource("/Fxml/Admin/DashboardAdmin.fxml")).load();
            } catch (Exception e) {
                AlertHelper.showAlert("Questo non doveva succedere", " Qualcosa è andato storto nel caricamento della pagina", null, Alert.AlertType.ERROR);
            }
        } else if (dashboard.getParent() != null) {
            ((javafx.scene.layout.Pane) dashboard.getParent()).getChildren().remove(dashboard);
        }
        return dashboard;
    }

    public BorderPane getUtentiView() {
        if (utenti == null) {
            try {
                utenti = new FXMLLoader(getClass().getResource("/Fxml/Admin/AdminMenuView/UtentiAdmin.fxml")).load();
            } catch (Exception e) {
                AlertHelper.showAlert("Questo non doveva succedere", " Qualcosa è andato storto nel caricamento della pagina", null, Alert.AlertType.ERROR);
            }
        } else if (utenti.getParent() != null) {
            ((javafx.scene.layout.Pane) utenti.getParent()).getChildren().remove(utenti);
        }
        return utenti;
    }

    public BorderPane getSchedeView() {
        if (schede == null) {
            try {
                schede = new FXMLLoader(getClass().getResource("/Fxml/Admin/AdminMenuView/SchedeAdmin.fxml")).load();
            } catch (Exception e) {
                AlertHelper.showAlert("Questo non doveva succedere", " Qualcosa è andato storto nel caricamento della pagina", null, Alert.AlertType.ERROR);
            }
        } else if (schede.getParent() != null) {
            ((javafx.scene.layout.Pane) schede.getParent()).getChildren().remove(schede);
        }
        return schede;
    }

    public BorderPane getCertificatiView() {
        if (certificati == null) {
            try {
                certificati = new FXMLLoader(getClass().getResource("/Fxml/Admin/AdminMenuView/CertificatiAdmin.fxml")).load();
            } catch (Exception e) {
                AlertHelper.showAlert("Questo non doveva succedere", " Qualcosa è andato storto nel caricamento della pagina", null, Alert.AlertType.ERROR);
            }
        } else if (certificati.getParent() != null) {
            ((javafx.scene.layout.Pane) certificati.getParent()).getChildren().remove(certificati);
        }
        return certificati;
    }
}