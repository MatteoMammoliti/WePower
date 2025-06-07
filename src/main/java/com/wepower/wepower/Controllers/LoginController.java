package com.wepower.wepower.Controllers;

import com.wepower.wepower.Models.ConnessioneDatabase;
import com.wepower.wepower.Models.Model;
import com.wepower.wepower.Models.ModelAutenticazione;
import com.wepower.wepower.Models.DatiSessioneCliente;
import com.wepower.wepower.Views.AlertHelper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.sql.SQLException;

public class LoginController implements Initializable {

    @FXML private Button loginButton;
    @FXML private Label labelLoginError;
    @FXML private TextField textEmail;
    @FXML private Button signupButton;
    @FXML private PasswordField passwordField;
    @FXML private TextField showPassword;
    @FXML private Button eyeButton;

    public LoginController() {}

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sincronizzaPassword();
        eyeButton.setOnAction(event -> nascondiPassword());

        signupButton.setOnAction(event -> onSignUp());

        loginButton.setOnAction(event -> {
            try {
                clickLogin();
            } catch (SQLException e) {
                AlertHelper.showAlert("Questo non doveva succedere", "Errore durante il login", null, Alert.AlertType.ERROR);
            }
        });
    }

    public void sincronizzaPassword(){
        passwordField.textProperty().addListener((obs, oldText, newText) -> {
            if (!showPassword.isVisible()) {
                showPassword.setText(newText);
            }
        });
        showPassword.textProperty().addListener((obs, oldText, newText) -> {
            if (showPassword.isVisible()) {
                passwordField.setText(newText);
            }
        });
    }
    public void onSignUp() {
        Stage stage = (Stage) signupButton.getScene().getWindow();
        Model.getInstance().getViewFactoryClient().closeStage(stage);
        Model.getInstance().getViewFactoryClient().showSignUpWindow();
    }

     //FUNZIONI LOGIN CON CONNESSIONR AL DB
    public void clickLogin() throws SQLException {
        String email=textEmail.getText();
        String password=passwordField.getText();

        if(ModelAutenticazione.verificaCredenziali(email,password)) {

            labelLoginError.setVisible(false);

            ModelAutenticazione.prelevaDatiPalestra();
            Stage stage = (Stage) loginButton.getScene().getWindow();
            Model.getInstance().getViewFactoryClient().closeStage(stage);
            String ruolo = DatiSessioneCliente.getNomeUtente();

            if (Objects.equals(ruolo, "Admin")) {
                Model.getInstance().getViewFactoryAdmin().showDashboardAdmin();
            }
            else {
                Model.getInstance().getViewFactoryClient().showDashboardClient();
            }
        }
        else
        {
            labelLoginError.setVisible(true);
        }
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