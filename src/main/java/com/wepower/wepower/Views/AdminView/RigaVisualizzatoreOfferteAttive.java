package com.wepower.wepower.Views.AdminView;

import com.wepower.wepower.Controllers.Admin.AdminDashboardController;
import com.wepower.wepower.Models.AdminModel.ModelDashboardAdmin;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
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

                // Mostro la finestra e aspetto la risposta
                Optional<ButtonType> result = alert.showAndWait();

                // Controllo se l'utente ha cliccato su OK
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    // L'utente ha confermato, elimina la promozione
                    if(ModelDashboardAdmin.eliminaPromozione(nomeOfferta)){
                        Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                        alert1.setTitle("Elimina promozione");
                        alert1.setHeaderText("Promozione eliminata con successo");
                        AdminDashboardController.getInstance().setPromozioni();
                } else {
                    // L'utente ha annullato
                    System.out.println("Eliminazione annullata.");
                }

                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        this.getChildren().addAll(nome,costo,eliminaOfferta);

    }
}
