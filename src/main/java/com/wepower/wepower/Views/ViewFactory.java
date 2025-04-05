package com.wepower.wepower.Views;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class ViewFactory {

    private final StringProperty currentMenuView;
    private BorderPane dashboard;

    public ViewFactory() {
        this.currentMenuView = new SimpleStringProperty("");
    }

    // otteniamo l'attuale view del menu (quella selezionata dal'utente)
    public StringProperty getCurrentMenuView() {
        return currentMenuView;
    }

    // Visualizziamo l'intera dashboard (senza menu)
    public BorderPane getDashboard() {
        if (dashboard == null) {
            try {
                dashboard = new FXMLLoader(getClass().getResource("/Fxml/Client/Dashboard.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return dashboard;
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
        stage.show();
        stage.setX(Screen.getPrimary().getVisualBounds().getMinX() + 100);
        stage.setY(Screen.getPrimary().getVisualBounds().getMinY() + 100);
    }

    // chiudiamo la finestra da cui stiamo provenendo (nella transazione Login -> Dashboard, chiudiamo la finestra di Login)
    public void closeStage(Stage stage) {
        if (stage != null) {
            stage.close();
        }
    }
}
