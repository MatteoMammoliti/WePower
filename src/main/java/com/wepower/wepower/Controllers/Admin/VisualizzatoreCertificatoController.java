package com.wepower.wepower.Controllers.Admin;

import com.wepower.wepower.ControlloTemi;
import com.wepower.wepower.Models.AdminModel.ModelTabellaCertificati;
import com.wepower.wepower.Views.AlertHelper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class VisualizzatoreCertificatoController implements Initializable {

    @FXML private ImageView immagine;
    @FXML private Button btnApprova;
    @FXML private Button btnRifiuta;
    private int idCliente;
    private TabellaCertificatiAdminController tabellaCertificati;

    public VisualizzatoreCertificatoController() {}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> { // questo thread aspetta che l'intera interfaccia sia caricata (e quindi anche il controller) prima di settare l'id utente
            try {
                immagine.setImage(ModelTabellaCertificati.prelevoImmagineCertificato(idCliente));
            } catch (SQLException e) {
                AlertHelper.showAlert("Questo non doveva succedere", "Errore durante il caricamento dell'immagine profilo", null, Alert.AlertType.ERROR);
            }

            btnApprova.setOnAction(e -> {
                try {
                    if (ModelTabellaCertificati.onClickConferma(idCliente)) {
                        AlertHelper.showAlert("Certificato", "Certificato approvato", "Il certificato è stato approvato",  Alert.AlertType.INFORMATION);
                        Stage stage = (Stage) btnApprova.getScene().getWindow();
                        AdminDashboardController.getInstance().setDatiPalestra();
                        tabellaCertificati.aggiornaTabella();
                        stage.close();
                    }
                } catch (SQLException ex) {
                    AlertHelper.showAlert("Errore", "Errore durante l'approvazione del certificato", null,  Alert.AlertType.ERROR);
                }
            });

            btnRifiuta.setOnAction(e -> {
                ButtonType si = new ButtonType("Sì");
                ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Vuoi procedere?", si, no);
                alert.setTitle("Conferma");
                alert.setHeaderText("Sei sicuro? Il certificato verrà eliminato definitivamente");
                ImageView icon = new ImageView(new Image(getClass().getResourceAsStream("/Images/IconeAlert/question.png")));
                DialogPane dialogPane = alert.getDialogPane();
                dialogPane.getStylesheets().add(getClass().getResource("/Styles/alertStyle.css").toExternalForm());
                alert.setGraphic(icon);
                Optional<ButtonType> result = alert.showAndWait();

                if (result.isPresent() && result.get() == si) {
                    try {
                        if (ModelTabellaCertificati.onClickRifiuta(idCliente)) {
                            AlertHelper.showAlert("Certificato", "Certificato rifiutato", "Il certificato è stato rifiutato",  Alert.AlertType.INFORMATION);
                            Stage stage = (Stage) btnRifiuta.getScene().getWindow();
                            AdminDashboardController.getInstance().setDatiPalestra();
                            tabellaCertificati.aggiornaTabella();
                            stage.close();
                        }
                    } catch (SQLException ex) {
                        AlertHelper.showAlert("Errore", "Errore durante il rifiuto del certificato", null,  Alert.AlertType.ERROR);
                    }
                }
            });
        });
    }

    public void setTabellaCertificati(TabellaCertificatiAdminController tabellaCertificati) { this.tabellaCertificati = tabellaCertificati; }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public static void apriSchermataVisualizzatoreCertificato(int id,TabellaCertificatiAdminController chiamante) throws IOException {
        FXMLLoader loader=new FXMLLoader(VisualizzatoreCertificatoController.class.getResource("/Fxml/Admin/AdminVisualizzaCertificato.fxml"));
        Parent root=loader.load();

        VisualizzatoreCertificatoController controller=loader.getController();
        controller.setIdCliente(id);
        controller.setTabellaCertificati(chiamante);

        Stage stage=new Stage();
        stage.setTitle("Certificato");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        Scene scene=new Scene(root);
        scene.getStylesheets().add(ControlloTemi.getInstance().getCssTemaCorrente());
        stage.setScene(scene);
        stage.showAndWait();
    }
}
