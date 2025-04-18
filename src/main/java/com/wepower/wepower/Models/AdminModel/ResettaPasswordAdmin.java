package com.wepower.wepower.Models.AdminModel;

import com.wepower.wepower.Models.ConnessioneDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ResettaPasswordAdmin {
    public static boolean onSalva(int idCliente, String password) {
        Connection conn = null;
        try {
            conn = ConnessioneDatabase.getConnection();
            conn.setAutoCommit(false);
            String queryResettaPassword = "UPDATE CredenzialiCLiente SET password=? WHERE idCliente=?";
            int righe=0;
            try (PreparedStatement ps = conn.prepareStatement(queryResettaPassword)) {
                ps.setString(1, password);
                ps.setInt(2, idCliente);
                righe = ps.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            conn.commit();
            return righe == 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);

        }
    }
    public static String onGenera() {
        int code = (int) (Math.random() * 1_000_000);
        String password =String.format("%06d", code);
        return password;
    }

}
