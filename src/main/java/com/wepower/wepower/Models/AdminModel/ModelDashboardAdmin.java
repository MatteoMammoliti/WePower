package com.wepower.wepower.Models.AdminModel;

import com.wepower.wepower.Models.ConnessioneDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ModelDashboardAdmin {

    public static int numeroAbbonamentiAttivi(){
        String numeroAbbonamenti="SELECT COUNT(*) FROM AbbonamentoCliente WHERE StatoAbbonamento=1";
        try(Connection conn= ConnessioneDatabase.getConnection()){
            PreparedStatement pst=conn.prepareStatement(numeroAbbonamenti);
            ResultSet rs=pst.executeQuery();
            if(rs.next()){
                return rs.getInt(1);
            }


        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return 0;
    }
}