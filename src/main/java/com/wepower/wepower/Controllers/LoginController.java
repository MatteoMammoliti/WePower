package com.wepower.wepower.Controllers;

import com.wepower.wepower.Models.Model;
import com.wepower.wepower.Models.ModelAutenticazione;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.sql.SQLException;

public class LoginController implements Initializable {

    public Button loginButton;
    public Label labelLoginError;

    @FXML
    private TextField textEmail;

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
        eyeButton.setOnAction(event -> nascondiPassword());

        signupButton.setOnAction(event -> onSignUp());

        loginButton.setOnAction(event -> {
            try {
                clickLogin();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void onSignUp() {
        Stage stage = (Stage) signupButton.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
        Model.getInstance().getViewFactory().showSignUpWindow();
    }

    public void onLogin() {
        Stage stage = (Stage) loginButton.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
        Model.getInstance().getViewFactory().showDashboardClient();
    }

     //FUNZIONI LOGIN CON CONNESSIONR AL DB
    public void clickLogin() throws SQLException {
        String email=textEmail.getText();
        String password=passwordField.getText();
        if(ModelAutenticazione.verificaCredenziali(email,password)){
            System.out.println("Login effettuato con successo");
            labelLoginError.setVisible(false);
            Model.getInstance().getViewFactory().showDashboardClient();
        }
        else{
            labelLoginError.setVisible(true);
            System.out.println("Login fallito");
        }
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