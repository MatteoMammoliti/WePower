package com.wepower.wepower.Views.AdminView;

import com.wepower.wepower.Controllers.Admin.AdminDashboardController;
import com.wepower.wepower.Models.AdminModel.ModelDashboardAdmin;
import com.wepower.wepower.Views.AlertHelper;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.sql.SQLException;
import java.util.Optional;

public class RigaVisualizzatoreOfferteAttive extends HBox {
    private String nomeOfferta;
    private String costoOfferta;
    private Button eliminaOfferta;

    public RigaVisualizzatoreOfferteAttive(String nomeOfferta, String costoOfferta) {
        this.nomeOfferta = nomeOfferta;
        this.costoOfferta = costoOfferta;
        this.setPrefHeight(50);
        this.setSpacing(20);
        this.setAlignment(Pos.CENTER);
        Label nome = new Label(nomeOfferta);
        Label costo=new Label(costoOfferta);
        eliminaOfferta = new Button("Elimina Offerta");
        eliminaOfferta.setPrefHeight(20);
        eliminaOfferta.setOnAction(e -> {
            try {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Elimina promozione");
                alert.setHeaderText("Sei sicuro di voler eliminare la promozione: " + nomeOfferta + "?");
                alert.setContentText("Questa azione è irreversibile.");
                ImageView icon = new ImageView(new Image(getClass().getResourceAsStream("/Images/IconeAlert/question.png")));
                DialogPane dialogPane = alert.getDialogPane();
                dialogPane.getStylesheets().add(getClass().getResource("/Styles/alertStyle.css").toExternalForm());
                alert.setGraphic(icon);

                // Mostro la finestra e aspetto la risposta
                Optional<ButtonType> result = alert.showAndWait();

                // Controllo se l'utente ha cliccato su OK
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    // L'utente ha confermato, elimina la promozione
                    if(ModelDashboardAdmin.eliminaPromozione(nomeOfferta)){
                        AlertHelper.showAlert("Elimina promozione","Promozione eliminata con successo", null, Alert.AlertType.INFORMATION );
                        AdminDashboardController.getInstance().setPromozioni();
                    }
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        this.getChildren().addAll(nome,costo,eliminaOfferta);
    }
}