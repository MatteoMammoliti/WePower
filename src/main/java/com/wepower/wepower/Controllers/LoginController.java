package com.wepower.wepower.Controllers;

import com.wepower.wepower.Models.Model;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    public Button loginButton;
    @FXML
    private Button signupButton;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField showPassword;

    @FXML
    private Button eyeButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Assegna un EventHandler al bottone
        eyeButton.setOnAction(event -> nascondiPassword());
        signupButton.setOnAction(event -> {
            try {
                signupButtonClick();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        loginButton.setOnAction(event -> Model.getInstance().getViewFactory().showDashboardClient());
    }

    public void signupButtonClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/SignUp.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        Stage stage = (Stage) signupButton.getScene().getWindow();

        stage.setScene(scene);
        stage.setTitle("WePower - Registrazione nuovo utente");
        stage.setResizable(false);
        stage.show();
    }

    private void nascondiPassword() {
        boolean isVisible = showPassword.isVisible();

        // Alterniamo la visibilità del campo password e del campo di testo
        showPassword.setVisible(!isVisible);
        showPassword.setManaged(!isVisible);
        passwordField.setVisible(isVisible);
        passwordField.setManaged(isVisible);

        // Sincronizza il testo
        showPassword.setText(passwordField.getText());

        // Cambia icona
        eyeButton.setText(isVisible ? "🔍" : "🔒");
    }
}