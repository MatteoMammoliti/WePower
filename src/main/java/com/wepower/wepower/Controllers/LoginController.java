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
    public Button signup_button;
    public PasswordField passwordF_field;
    public TextField show_password;
    public Button eyeButton;

    public void signup_button_click() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/SignUp.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        Stage stage = (Stage) signup_button.getScene().getWindow();

        stage.setScene(scene);
        stage.setTitle("WePower - Registrazione nuovo utente");
        stage.setResizable(false);
        stage.show();
    }

    public void nascondi_password()
    {
        boolean isVisible = show_password.isVisible();

        // alterniamo la visibilità del campo password e del campo di testo
        show_password.setVisible(!isVisible);
        show_password.setManaged(!isVisible);
        passwordF_field.setVisible(isVisible);
        passwordF_field.setManaged(isVisible);

        show_password.setText(passwordF_field.getText());
        eyeButton.setText(isVisible ? "🔍" : "🔒");
    }
}