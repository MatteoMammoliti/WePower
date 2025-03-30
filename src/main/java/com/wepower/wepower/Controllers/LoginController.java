package com.wepower.wepower.Controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    public TextField username_text_field;
    public PasswordField password_field;
    public Button accedi_button;
    public Button signup_button;

    public void signup_button_click() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/SignUp.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        Stage stage = (Stage) signup_button.getScene().getWindow();

        stage.setScene(scene);
        stage.setTitle("WePower - Registrazione nuovo utente");
        stage.setResizable(false);
        stage.show();
    }
}

