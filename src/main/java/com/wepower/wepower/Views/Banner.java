    package com.wepower.wepower.Views;

    import javafx.scene.control.Label;
    import javafx.scene.image.Image;
    import javafx.scene.layout.*;


    public class Banner extends VBox {
        private Label titolo;
        private Label prezzo;
        private double prefHieght = 200;
        private double prefWidth = 350;

        public Banner() {}

        public Banner(String UrlImmagine, String nomeTitolo, double costo) {
            Image background = new Image(UrlImmagine);
            BackgroundImage backgroundImage = new BackgroundImage(
                    background,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(1.0,1.0,true,true,true,true)
            );

            this.setPrefHeight(prefHieght); // altezza del banner
            this.setPrefWidth(prefWidth);

            this.setBackground(new Background(backgroundImage));

            titolo = new Label(nomeTitolo); // crea un'etichetta per il titolo


            prezzo = new Label(String.format("Costo: %.2f €", costo)); // crea un'etichetta per il prezzo



            this.getChildren().addAll(titolo, prezzo);

            this.getStyleClass().add("bannerino");
            this.getStylesheets().add(getClass().getResource("/Styles/bannerStyle.css").toExternalForm());
        }

        public double getAltezza() {
            return this.prefHieght;
        }

        public double getLarghezza() {
            return this.prefWidth;
        }
    }
