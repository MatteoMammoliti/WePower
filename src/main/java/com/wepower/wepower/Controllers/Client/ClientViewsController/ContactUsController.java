package com.wepower.wepower.Controllers.Client.ClientViewsController;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.ResourceBundle;

public class ContactUsController implements Initializable {

    @FXML
    private WebView googleMapsView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        WebEngine engine = googleMapsView.getEngine();

        URL htmlUrl = getClass().getResource("/Html/googleMapsAPI.html");
        if (htmlUrl == null) {
            return;
        }
        String toLoad = htmlUrl.toExternalForm();
        engine.load(toLoad);
    }
}