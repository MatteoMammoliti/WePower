package com.wepower.wepower.Models.AdminModel;

import com.wepower.wepower.Models.ConnessioneDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ModelMassimoPrenotazioni {
    public static void salvaNuovaCapienza(int nuovaCapienza) {
        String query="UPDATE Salapesi SET NumeriPostiMassimo = ?";
        try(Connection conn= ConnessioneDatabase.getConnection()) {
            PreparedStatement caricoDati = conn.prepareStatement(query);
            caricoDati.setInt(1, nuovaCapienza);
            caricoDati.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
