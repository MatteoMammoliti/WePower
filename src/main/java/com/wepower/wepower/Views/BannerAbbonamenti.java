package com.wepower.wepower.Views;

import com.wepower.wepower.Models.ConnessioneDatabase;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class BannerAbbonamenti extends VBox {
    private Label titolo;
    private Label prezzo;

    public BannerAbbonamenti(String UrlImmagine, String nomeTitolo, double costo, double prefHieght, double prefWidth) {
        Image background = new Image(UrlImmagine);
        BackgroundImage backgroundImage = new BackgroundImage(
                background,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(1.0,1.0,true,true,true,true)
        );

        this.setPrefHeight(prefHieght); // altezza del banner
        this.setPrefWidth(prefWidth); // larghezza del banner

        this.setBackground(new Background(backgroundImage));

        titolo = new Label(nomeTitolo); // crea un'etichetta per il titolo
        prezzo = new Label(String.format("Costo: %.2f €", costo)); // crea un'etichetta per il prezzo

        this.getChildren().addAll(titolo, prezzo);

        this.getStyleClass().add("bannerino");
        this.getStylesheets().add(getClass().getResource("/Styles/bannerStyle.css").toExternalForm());
    }

    static public ArrayList<BannerAbbonamenti> getBannerAbbonamentiDB(){
        ArrayList<BannerAbbonamenti> bannerAbbonamenti=new ArrayList<>();
        try (Connection conn= ConnessioneDatabase.getConnection()) {
            String query = "SELECT * FROM TipoAbbonamento";
            PreparedStatement dati = conn.prepareStatement(query);
            ResultSet risultato = dati.executeQuery();

            while(risultato.next()){
                //String urlImmagine = risultato.getString("UrlImmagine");
                String nomeTitolo = risultato.getString("NomeAbbonamento");
                double costo = risultato.getDouble("Costo");
                String path = BannerAbbonamenti.class.getResource("/Images/LOGO.png").toExternalForm();
                BannerAbbonamenti banner = new BannerAbbonamenti(path, nomeTitolo, costo, 200, 300);
                bannerAbbonamenti.add(banner);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return bannerAbbonamenti;

    }

}