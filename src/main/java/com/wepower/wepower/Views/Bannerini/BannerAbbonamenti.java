package com.wepower.wepower.Views.Bannerini;

import com.wepower.wepower.Controllers.Client.ClientViewsController.InserimentoDatiPagamentoController;
import com.wepower.wepower.Controllers.Client.ClientViewsController.ProfiloController;
import com.wepower.wepower.ControlloTemi;
import com.wepower.wepower.Models.ConnessioneDatabase;
import com.wepower.wepower.Models.DatiSessioneCliente;
import com.wepower.wepower.Views.AlertHelper;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


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
                new BackgroundSize(1.0, 1.0, true, true, true, true)
        );

        this.setPrefHeight(prefHieght); // altezza del banner
        this.setPrefWidth(prefWidth); // larghezza del banner

        this.setBackground(new Background(backgroundImage));

        titolo = new Label(nomeTitolo); // crea un'etichetta per il titolo
        prezzo = new Label(String.format("Costo: %.2f €", costo)); // crea un'etichetta per il prezzo

        this.getChildren().addAll(titolo, prezzo);
        this.setOnMouseClicked(event -> {
            try {
                ProfiloController.getInstance().onClickLabelAbbonamenti();
            } catch (IOException e) {
                AlertHelper.showAlert("Questo non doveva succedere", " Qualcosa è andato storto", null, Alert.AlertType.ERROR);
            }
        });

        this.getStyleClass().add("bannerino");
        this.getStylesheets().add(getClass().getResource("/Styles/Dashboard.css").toExternalForm());
    }

    static public ArrayList<BannerAbbonamenti> getBannerAbbonamentiDB() {
        ArrayList<BannerAbbonamenti> bannerAbbonamenti = new ArrayList<>();
        try (Connection conn = ConnessioneDatabase.getConnection()) {
            String query = "SELECT * FROM TipoAbbonamento";
            PreparedStatement dati = conn.prepareStatement(query);
            ResultSet risultato = dati.executeQuery();

            while (risultato.next()) {
                //String urlImmagine = risultato.getString("UrlImmagine");
                String nomeTitolo = risultato.getString("NomeAbbonamento");
                double costo = risultato.getDouble("Costo");
                String path = BannerAbbonamenti.class.getResource("/Images/LOGO.png").toExternalForm();
                BannerAbbonamenti banner = new BannerAbbonamenti(path, nomeTitolo, costo, 150, 300);
                banner.setOnMouseClicked(event -> {
                    if(DatiSessioneCliente.getStatoAbbonamento()){
                        AlertHelper.showAlert("Abbonamento già attivo", "Hai già un abbonamento attivo", null,  Alert.AlertType.INFORMATION);
                        return;
                    }
                    try {
                        banner.onClickBannerio(nomeTitolo,costo);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
                bannerAbbonamenti.add(banner);
            }
        } catch (SQLException e) {
            AlertHelper.showAlert("Questo non doveva succedere", " Qualcosa è andato storto", null, Alert.AlertType.ERROR);
        }
        return bannerAbbonamenti;
    }

    public void onClickBannerio(String nome,double prezzoB) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Client/ClientMenuView/InserimentoDatiPagamento.fxml"));
        Parent root = loader.load();
        InserimentoDatiPagamentoController controller = loader.getController();

        Scene scena = new Scene(root);

        String cssTema=getClass().getResource("/Styles/inserimentoDatiPagamento.css").toExternalForm();
        ControlloTemi.getInstance().aggiungiScena(scena, cssTema);

        controller.setNomeEPrezzoAbb(nome, prezzoB);
        Stage stage = new Stage();
        stage.setTitle("Pagamento abbonamento");
        stage.setScene(scena);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/Images/favicon.png")));
        stage.setResizable(false);
        stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }
}