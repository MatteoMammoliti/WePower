package com.wepower.wepower.Views;

import com.wepower.wepower.Controllers.Client.ClientController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ViewFactory {

    private AnchorPane dashboard;

    public ViewFactory() {}

    // Visualizziamo l'intera dashboard (senza menu)
    public AnchorPane getDashboard() {
        if (dashboard == null) {
            try {
                dashboard = new FXMLLoader(getClass().getResource("/Fxml/Client/Dashboard.fxml")).load();
            } catch (Exception e) {
                e.getStackTrace();
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

    // intera dashboard risercvata per il cliente (con menu)
    public void showDashboardClient() {
        FXMLLoader dashboardClient = new FXMLLoader(getClass().getResource("/Fxml/Client/Client.fxml"));
        ClientController controller = new ClientController();
        dashboardClient.setController(controller);

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
    }

    // chiudiamo la finestra da cui stiamo provenendo (nella transazione Login -> Dashboard, chiudiamo la finestra di Login)
    public void closeStage(Stage stage) {
        if (stage != null) {
            stage.close();
        }
    }
}
