package com.wepower.wepower.Models.DatiPalestra;

import com.wepower.wepower.Models.ConnessioneDatabase;
import com.wepower.wepower.Views.AlertHelper;
import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Abbonamento {
    private String nomeAbbonamento;
    private String descrizioneAbbonamento;
    private int costo;
    private String durataAbbonamento;
    public Abbonamento(String nome,String descrizione,int costo,String durata){
        this.nomeAbbonamento=nome;
        this.descrizioneAbbonamento=descrizione;
        this.costo=costo;
        this.durataAbbonamento=durata;
    }

    //Getter
    public String getNomeAbbonamento() {return nomeAbbonamento;}
    public String getDescrizioneAbbonamento() {return descrizioneAbbonamento;}
    public int getCosto() {return costo;}
    public String getDurataAbbonamento() {return durataAbbonamento;}

    public static ArrayList<Abbonamento> getAbbonamentiDb() {
        ArrayList<Abbonamento> abbonamenti=new ArrayList<>();
        String prendiDati="SELECT * FROM TipoAbbonamento";

        PreparedStatement preparo = null;
        ResultSet risultato = null;
        try {
            preparo=ConnessioneDatabase.getConnection().prepareStatement(prendiDati);
            risultato=preparo.executeQuery();

            while(risultato.next()){
                Abbonamento nuovo=new Abbonamento(risultato.getString("NomeAbbonamento"),risultato.getString("Descrizione"),risultato.getInt("Costo"),risultato.getString("Durata"));
                abbonamenti.add(nuovo);
            }
        } catch(SQLException e){
            AlertHelper.showAlert("Questo non doveva succedere", "Qualcosa è andato storto nel prelevamento dei dati", null, Alert.AlertType.ERROR);
        } finally {
            if (preparo != null) {
                try { preparo.close(); } catch (SQLException ignored) {}
            }
            if (risultato != null) {
                try { risultato.close(); } catch (SQLException ignored) {}
            }
        }
        return abbonamenti;
    }
}
