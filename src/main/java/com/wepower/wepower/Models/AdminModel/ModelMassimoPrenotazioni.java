package com.wepower.wepower.Models.AdminModel;

import com.wepower.wepower.Models.ConnessioneDatabase;
import com.wepower.wepower.Views.AlertHelper;
import javafx.scene.control.Alert;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ModelMassimoPrenotazioni {
    //Aggiorno la capienza massima della palestra
    public static void salvaNuovaCapienza(int nuovaCapienza) {
        Connection conn = ConnessioneDatabase.getConnection();

        String query="UPDATE SalaPesi SET NumeriPostiMassimo = ? WHERE IdSalaPesi = 1";

        PreparedStatement caricoDati = null;
        try {
            caricoDati = conn.prepareStatement(query);
            caricoDati.setInt(1, nuovaCapienza);
            caricoDati.executeUpdate();

        } catch (SQLException e) {
            AlertHelper.showAlert("Questo non doveva succedere", "Errore durante l'aggiornamento della capienza", null, Alert.AlertType.ERROR);
        } finally {
            if(caricoDati != null) {
                try { caricoDati.close(); } catch (SQLException ignored) {}
            }
        }
    }
}
