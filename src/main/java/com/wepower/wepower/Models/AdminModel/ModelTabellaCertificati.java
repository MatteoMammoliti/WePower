package com.wepower.wepower.Models.AdminModel;

import com.wepower.wepower.Models.ConnessioneDatabase;
import com.wepower.wepower.Views.AdminView.RigaTabellaCertificati;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ModelTabellaCertificati {

    //In questo metodo possiamo inizializzare la tabella, carichiamo i dati dal DB
    public static ObservableList<RigaTabellaCertificati> caricaDati(){
        Connection conn = ConnessioneDatabase.getConnection();

        //Creo una lista osservabile di RigaTabellaCertificati, che conterrà i dati della tabella
        //Inizializzo la lista con FXCollections.observableArrayList(),cosi sarà vuota.
        //Una lista osservabile è una lista che può essere osservata da altri oggetti, in questo caso dalla tabella,ogni volta
        //che aggiungo,modico o rimuovo un elemento dalla lista, la tabella si aggiornerà automaticamente

        ObservableList<RigaTabellaCertificati> clienti= FXCollections.observableArrayList();
        String dati="SELECT c.IdCliente,c.Nome,c.Cognome,ce.DataCaricamentoCertificato From Cliente c JOIN Certificato ce on c.IdCliente=ce.IdCliente AND ce.Stato='Attesa' ";
        try {
            PreparedStatement preparoQuery=conn.prepareStatement(dati);
            ResultSet risultati=preparoQuery.executeQuery();
            //In questo ciclo while, per ogni riga della tabella, creo un oggetto RigaTabellaCertificati e lo aggiungo alla lista
            while (risultati.next()){
                //Inizializzo le variabili con i dati della tabella
                String nome=risultati.getString("Nome");
                String cognome=risultati.getString("Cognome");
                int id=risultati.getInt("IdCliente");
                String dataCaricamento=risultati.getString("DataCaricamentoCertificato");
                //Creo un oggetto RigaTabellaCertificati e lo aggiungo alla lista
                RigaTabellaCertificati riga=new RigaTabellaCertificati(nome,cognome,id,dataCaricamento);
                clienti.add(riga);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return clienti;
    }

    public static Image prelevoImmagineCertificato(int id) throws SQLException {
        Connection conn = ConnessioneDatabase.getConnection();

        System.out.println("Query eseguita con ID: " + id);

        String immagine="SELECT ImgCertificato from Certificato WHERE IDCliente=?";
        try {
            PreparedStatement preparoQuery=conn.prepareStatement(immagine);
            preparoQuery.setInt(1,id);
            ResultSet risultati=preparoQuery.executeQuery();
            if(risultati.next()){
                byte[] byteImage=risultati.getBytes("ImgCertificato");
                InputStream is=new ByteArrayInputStream(byteImage);
                Image image=new Image(is);
                System.out.println("ttttt");
                return image;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    //Se clicco su conferma, aggiorno il certificato e il cliente
    public static boolean onClickConferma(int id) throws SQLException {
        Connection conn = ConnessioneDatabase.getConnection();

        String aggiornoCertificato="UPDATE Certificato SET Stato='Accettato' WHERE IdCliente=?";
        String aggiornoCliente="UPDATE Cliente SET CertificatoValido=2 WHERE IdCliente=?";
        try {
            conn.setAutoCommit(false);
            PreparedStatement preparoQuery=conn.prepareStatement(aggiornoCertificato);
            preparoQuery.setInt(1,id);
            int righeModificate=preparoQuery.executeUpdate();
            if(righeModificate<=0){
                conn.rollback();
                return false;
            }
            PreparedStatement preparoQuery2=conn.prepareStatement(aggiornoCliente);
            preparoQuery2.setInt(1,id);
            int righeModificate2=preparoQuery2.executeUpdate();
            if(righeModificate2<=0){
                conn.rollback();
                return false;
            }
            conn.commit();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //Se clicco su Rifiuta,rifiuto il certificato, lo elimino e aggiorno il cliente
    public static boolean onClickRifiuta(int id) throws SQLException {
        Connection conn = ConnessioneDatabase.getConnection();

        String eliminoCertificato="DELETE FROM Certificato WHERE IdCliente=?";
        String aggiornoCliente="UPDATE Cliente SET CertificatoValido=0 WHERE IdCliente=?";
        try {
            conn.setAutoCommit(false);
            PreparedStatement preparoQuery=conn.prepareStatement(eliminoCertificato);
            preparoQuery.setInt(1,id);
            int riga=preparoQuery.executeUpdate();
            if(riga<=0){
                conn.rollback();
                return false;

            }
            PreparedStatement preparoQuery2=conn.prepareStatement(aggiornoCliente);
            preparoQuery2.setInt(1,id);
            int righeModificate=preparoQuery2.executeUpdate();
            if(righeModificate<=0){
                conn.rollback();
                return false;
            }
            conn.commit();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}