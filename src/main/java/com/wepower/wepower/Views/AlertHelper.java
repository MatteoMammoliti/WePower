package com.wepower.wepower.Views;

import com.wepower.wepower.ControlloTemi;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class AlertHelper {
    public static void showAlert(String titolo, String messaggioHeader, String messaggioContent, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(titolo);
        alert.setHeaderText(messaggioHeader);
        alert.setContentText(messaggioContent);

        String percorsoIcona = "";
        switch (type) {
            case ERROR: percorsoIcona = "/Images/IconeAlert/error.png"; break;
            case WARNING: percorsoIcona = "/Images/IconeAlert/error.png"; break;
            case CONFIRMATION: percorsoIcona = "/Images/IconeAlert/question.png"; break;
            case INFORMATION: percorsoIcona = "/Images/IconeAlert/info.png"; break;
            default: percorsoIcona = "/Images/IconeAlert/error.png"; break;
        }
        ImageView icon = new ImageView(new Image(AlertHelper.class.getResourceAsStream(percorsoIcona)));
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().clear();
        dialogPane.getStylesheets().add(AlertHelper.class.getResource("/Styles/alertStyle.css").toExternalForm());
        dialogPane.getStylesheets().add(ControlloTemi.getInstance().getCssTemaCorrente());
        alert.setGraphic(icon);
        alert.showAndWait();
    }
}