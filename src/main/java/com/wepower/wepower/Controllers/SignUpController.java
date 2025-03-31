package com.wepower.wepower.Controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class SignUpController {
    public Button loginButton;

    public void signinButtonClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        Stage stage = (Stage) loginButton.getScene().getWindow();

        stage.setScene(scene);
        stage.setTitle("WePower - Login Utente");
        stage.setResizable(false);
        stage.show();
    }
}