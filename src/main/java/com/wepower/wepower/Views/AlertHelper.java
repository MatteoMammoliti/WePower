package com.wepower.wepower.Views;

import com.wepower.wepower.ControlloTemi;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class AlertHelper {
    public static void showAlert(String titolo, String messaggioHeader, String messaggioContent, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(titolo);
        alert.setHeaderText(messaggioHeader);
        alert.setContentText(messaggioContent);

        String percorsoIcona = switch (type) {
            case ERROR -> "/Images/IconeAlert/error.png";
            case WARNING -> "/Images/IconeAlert/error.png";
            case CONFIRMATION -> "/Images/IconeAlert/question.png";
            case INFORMATION -> "/Images/IconeAlert/info.png";
            default -> "/Images/IconeAlert/error.png";
        };
        ImageView icon = new ImageView(new Image(AlertHelper.class.getResourceAsStream(percorsoIcona)));
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().clear();
        dialogPane.getStylesheets().add(AlertHelper.class.getResource("/Styles/alertStyle.css").toExternalForm());
        dialogPane.getStylesheets().add(ControlloTemi.getInstance().getCssTemaCorrente());
        alert.setGraphic(icon);
        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        alertStage.getIcons().add(new Image(AlertHelper.class.getResourceAsStream("/Images/favicon.png")));
        alert.showAndWait();
    }
}