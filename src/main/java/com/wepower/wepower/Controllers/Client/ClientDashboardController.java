package com.wepower.wepower.Controllers.Client;

import com.wepower.wepower.Views.Bannerino;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientDashboardController implements Initializable {


    @FXML
    private HBox displaybannerini;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Crea i banner
        Bannerino b1 = new Bannerino(getClass().getResource("/Images/LOGO.png").toExternalForm(), "Smartphone", 599.99);
        Bannerino b2 = new Bannerino(getClass().getResource("/Images/LOGO.png").toExternalForm(), "Laptop", 1299.99);
        Bannerino b3 = new Bannerino(getClass().getResource("/Images/LOGO.png").toExternalForm(), "Smartwatch", 299.99);

        // Aggiungi i banner all'HBox
        displaybannerini.getChildren().addAll(b1, b2, b3);


    }
}
