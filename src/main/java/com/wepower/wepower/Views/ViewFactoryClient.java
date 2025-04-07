package com.wepower.wepower.Views;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class ViewFactoryClient {

    private final StringProperty currentMenuView;
    private BorderPane dashboard;
    private BorderPane scheda;
    private BorderPane prenotazioni;
    private BorderPane parametri;
    private BorderPane myProfile;
    private BorderPane contactUs;

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
            e.printStackTrace();
        }

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
            e.printStackTrace();
        }

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
            e.printStackTrace();
        }

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("WePower - Dashboard");
        Platform.runLater(() -> {
            stage.sizeToScene(); // forza il layout
        });
        stage.show();
        stage.setX(Screen.getPrimary().getVisualBounds().getMinX() + 100);
        stage.setY(Screen.getPrimary().getVisualBounds().getMinY() + 100);
    }


                                                    // -- FUNZIONI DI CAMBIO VIEW DEL MENU --

    // Visualizziamo l'intera dashboard (senza menu)
    public BorderPane getDashboard() {
        if (dashboard == null) {
            try {
                dashboard = new FXMLLoader(getClass().getResource("/Fxml/Client/Dashboard.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
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
                scheda = new FXMLLoader(getClass().getResource("/Fxml/Client/ClientMenuView/Scheda.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
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
                e.printStackTrace();
            }
        } else if (prenotazioni.getParent() != null) {
            ((javafx.scene.layout.Pane) prenotazioni.getParent()).getChildren().remove(prenotazioni);
        }
        return prenotazioni;
    }

    public BorderPane getParametriView() {
        if (parametri == null) {
            try {
                parametri = new FXMLLoader(getClass().getResource("/Fxml/Client/ClientMenuView/Parametri.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (parametri.getParent() != null) {
            ((javafx.scene.layout.Pane) parametri.getParent()).getChildren().remove(parametri);
        }
        return parametri;
    }

    public BorderPane getMyProfileView() {
        if (myProfile == null) {
            try {
                myProfile = new FXMLLoader(getClass().getResource("/Fxml/Client/ClientMenuView/MyProfile.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (myProfile.getParent() != null) {
            ((javafx.scene.layout.Pane) myProfile.getParent()).getChildren().remove(myProfile);
        }
        return myProfile;
    }

    public BorderPane getContactUsView() {
        if (contactUs == null) {
            try {
                contactUs = new FXMLLoader(getClass().getResource("/Fxml/Client/ClientMenuView/ContactUs.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
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
}