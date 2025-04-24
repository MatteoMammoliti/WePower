package com.wepower.wepower.Controllers.Admin;

import com.wepower.wepower.Models.AdminModel.ModelTabellaCertificati;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class VisualizzatoreCertificatoController implements Initializable {
    @FXML
    private ImageView immagine;
    @FXML
    private Button btnApprova;
    @FXML
    private Button btnRifiuta;
    private int idCliente;
    private TabellaCertificatiAdminController tabellaCertificati;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Il metodo Platform.runLater
        // serve per eseguire del codice sulla JavaFX Application Thread, ovvero il thread responsabile dell’aggiornamento dell’interfaccia grafica.
        //Tutto quello che metti dentro runLater() verrà eseguito appena possibile sul thread giusto per modificare l'interfaccia.
        Platform.runLater(() -> {
            try {
                immagine.setImage(ModelTabellaCertificati.prelevoImmagineCertificato(idCliente));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            btnApprova.setOnAction(e -> {
                try {
                    if (ModelTabellaCertificati.onClickConferma(idCliente)) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Certificato");
                        alert.setHeaderText("Certificato approvato");
                        alert.showAndWait();
                        Stage stage = (Stage) btnApprova.getScene().getWindow();
                        tabellaCertificati.aggiornaTabella();
                        AdminUtentiController.getIstanza().aggiornaUtenti();
                        stage.close();
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            });

            btnRifiuta.setOnAction(e -> {
                ButtonType si = new ButtonType("Sì");
                ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Vuoi procedere?", si, no);
                alert.setTitle("Conferma");
                alert.setHeaderText("Sei sicuro?Il certificato verrà eliminato definitivamente");
                Optional<ButtonType> result = alert.showAndWait();

                if (result.isPresent() && result.get() == si) {
                    if (ModelTabellaCertificati.onClickRifiuta(idCliente)) {
                        Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                        alert1.setTitle("Certificato");
                        alert1.setHeaderText("Certificato rifiutato");
                        alert1.setContentText("Il certificato è stato rifiutato");
                        alert1.showAndWait();
                        Stage stage = (Stage) btnRifiuta.getScene().getWindow();
                        tabellaCertificati.aggiornaTabella();
                        AdminUtentiController.getIstanza().aggiornaUtenti();
                        stage.close();
                    }
                }
            });
        });
    }

    public void setTabellaCertificati(TabellaCertificatiAdminController tabellaCertificati) {
        this.tabellaCertificati = tabellaCertificati;
    }


    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public static void apriSchermataVisualizzatoreCertificato(int id,TabellaCertificatiAdminController chiamante) throws IOException, SQLException {
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
        stage.setScene(scene);
        stage.showAndWait();
    }
}
