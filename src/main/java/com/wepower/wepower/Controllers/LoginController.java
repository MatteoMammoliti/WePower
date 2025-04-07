package com.wepower.wepower.Controllers;

import com.wepower.wepower.Models.Model;
import com.wepower.wepower.Models.ModelAutenticazione;
import com.wepower.wepower.Models.DatiSessioneCliente;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;
import java.sql.SQLException;

public class LoginController implements Initializable {

    @FXML
    private Button loginButton;

    @FXML
    private Label labelLoginError;

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
        sincronizzaPassword();
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
        Model.getInstance().getViewFactory().closeStage(stage);
        Model.getInstance().getViewFactory().showSignUpWindow();
    }



     //FUNZIONI LOGIN CON CONNESSIONR AL DB
    public void clickLogin() throws SQLException {
        String email=textEmail.getText();
        String password=passwordField.getText();
        String ruolo = DatiSessioneCliente.getNomeUtente();

        if(ModelAutenticazione.verificaCredenziali(email,password)) {
            System.out.println("Login effettuato con successo");
            labelLoginError.setVisible(false);

            Stage stage = (Stage) loginButton.getScene().getWindow();
            Model.getInstance().getViewFactory().closeStage(stage);

            if (ruolo == "Admin") {
                //Model.getInstance().getViewFactory().showDashboardAdmin();
            }
            else {
                Model.getInstance().getViewFactory().showDashboardClient();
            }
        }
        else
        {
            labelLoginError.setVisible(true);
            System.out.println("Login fallito");
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