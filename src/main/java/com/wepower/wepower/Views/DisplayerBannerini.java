package com.wepower.wepower.Views;

import javafx.geometry.Insets;
import javafx.scene.layout.HBox;

import java.util.ArrayList;

public class DisplayerBannerini extends HBox {


    public DisplayerBannerini(ArrayList<Banner> bannerini) {

        // dimensioni del displayer
        this.setPrefHeight(300);
        this.setPrefWidth(1530);
        this.setBackground(null);

        // impostiamo lo spazio di margine tra i banner
        this.setSpacing(20);

        this.setPadding(new Insets(10)); // Padding esterno

        this.getStyleClass().add("bannerino");
        this.getStylesheets().add(getClass().getResource("/Styles/bannerStyle.css").toExternalForm());
    }


    public void aggiungiBannerino(Banner b) {
        this.getChildren().add(b);
    }

    public void rimuoviBannerino(Banner b) {
        this.getChildren().remove(b);
    }
}
