package com.wepower.wepower.Models.DatiPalestra;

import com.wepower.wepower.Models.ConnessioneDatabase;

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

    public static ArrayList<Abbonamento> getAbbonamentiDb() throws SQLException {
        ArrayList<Abbonamento> abbonamenti=new ArrayList<>();
        String prendiDati="SELECT * FROM TipoAbbonamento";
        try(Connection conn= ConnessioneDatabase.getConnection()){
            try(PreparedStatement preparo=conn.prepareStatement(prendiDati)){
                ResultSet risultato=preparo.executeQuery();

                while(risultato.next()){
                    Abbonamento nuovo=new Abbonamento(risultato.getString("NomeAbbonamento"),risultato.getString("Descrizione"),risultato.getInt("Costo"),risultato.getString("Durata"));
                    abbonamenti.add(nuovo);
                }
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
        return abbonamenti;
    }
}
