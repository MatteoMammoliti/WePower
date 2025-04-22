package com.wepower.wepower.Views.AdminView;

import com.wepower.wepower.Controllers.Admin.AdminDashboardController;
import com.wepower.wepower.Models.AdminModel.ModelDashboardAdmin;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import javax.swing.*;
import java.sql.SQLException;

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
                int scelta=JOptionPane.showConfirmDialog(null, "Sei sicuro di voler eliminare la promozione:"+nomeOfferta+"?", "Elimina promozione", JOptionPane.YES_NO_OPTION);
                if (scelta == JOptionPane.YES_OPTION) {
                    // Elimina la promozione
                    if(ModelDashboardAdmin.eliminaPromozione(nomeOfferta)){
                        JOptionPane.showMessageDialog(null, "Promozione eliminata con successo");
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
