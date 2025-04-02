package com.wepower.wepower.Views;

import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;

import java.util.ArrayList;

public class DisplayBannerini  extends HBox {


    public DisplayBannerini(ArrayList<Bannerino> bannerini) {
        this.setPrefHeight(300);
        this.setPrefWidth(1530);
        this.setBackground(null);
        this.getChildren().addAll(bannerini);
        this.getStyleClass().add("bannerino");
        this.getStylesheets().add(getClass().getResource("/styles/bannerStyle.css").toExternalForm());


    }

    public void aggiungiBannerino(Bannerino b) {
        this.getChildren().add(b);
    }

    public void rimuoviBannerino(Bannerino b) {
        this.getChildren().remove(b);
    }
}
