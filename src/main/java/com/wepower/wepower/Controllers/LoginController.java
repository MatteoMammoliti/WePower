package com.wepower.wepower.Controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    public Button signupButton;
    public PasswordField passwordField;
    public TextField showPassword;
    public Button eyeButton;

    public void signupButtonClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/SignUp.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        Stage stage = (Stage) signupButton.getScene().getWindow();

        stage.setScene(scene);
        stage.setTitle("WePower - Registrazione nuovo utente");
        stage.setResizable(false);
        stage.show();
    }

    public void nascondiPassword()
    {
        boolean isVisible = showPassword.isVisible();

        // alterniamo la visibilità del campo password e del campo di testo
        showPassword.setVisible(!isVisible);
        showPassword.setManaged(!isVisible);
        passwordField.setVisible(isVisible);
        passwordField.setManaged(isVisible);

        showPassword.setText(passwordField.getText());
        eyeButton.setText(isVisible ? "🔍" : "🔒");
    }
}