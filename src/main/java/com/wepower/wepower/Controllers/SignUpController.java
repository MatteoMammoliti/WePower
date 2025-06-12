package com.wepower.wepower.Controllers;

import com.wepower.wepower.Models.Model;
import com.wepower.wepower.Models.ModelRegistrazione;
import com.wepower.wepower.Models.ModelValidazione;
import com.wepower.wepower.Views.AlertHelper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import javax.swing.*;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {

    @FXML private Label labelErroreNome;
    @FXML private Label labelErroreCognome;
    @FXML private Label labelEmailNonValida;
    @FXML private Label labelDataErrata;
    @FXML private Label labelPasswordDiverse;
    @FXML private Button loginButton;
    @FXML private PasswordField passwordField;
    @FXML private TextField passwordVisibile;
    @FXML private TextField repeatPasswordVisibile;
    @FXML private PasswordField repeatPasswordField;
    @FXML private Button eyeButton;
    @FXML private Button signUpButton;
    @FXML private TextField emailText;
    @FXML private TextField textCognome;
    @FXML private DatePicker dataNascitaText;
    @FXML private TextField textNome;

    public SignUpController() {}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sincronizzaPassword();
        loginButton.setOnAction(event -> onLogin());

        signUpButton.setOnAction(event -> {
            try {
                registrazione();
            } catch (SQLException e) {
                AlertHelper.showAlert("Questo non doveva succedere", "Errore durante la registrazione", null, Alert.AlertType.ERROR);
            }
        });

        eyeButton.setOnAction(event -> nascondiPassword());
    }

    public  void sincronizzaPassword(){
        //Utilizzo un "Osservatore" per sincronizzare i campi password e ripeti password
        passwordVisibile.textProperty().addListener((obs, oldText, newText) -> {
            if (!passwordField.isVisible()) {
                passwordField.setText(newText);
            }
        });

        passwordField.textProperty().addListener((obs, oldText, newText) -> {
            if (!passwordVisibile.isVisible()) {
                passwordVisibile.setText(newText);
            }
        });
        repeatPasswordVisibile.textProperty().addListener((obs, oldText, newText) -> {
            if (!repeatPasswordField.isVisible()) {
                repeatPasswordField.setText(newText);
            }
        });

        repeatPasswordField.textProperty().addListener((obs, oldText, newText) -> {
            if (!repeatPasswordVisibile.isVisible()) {
                repeatPasswordVisibile.setText(newText);
            }
        });
    }
    public void onLogin() {
        Stage stage = (Stage) loginButton.getScene().getWindow();
        Model.getInstance().getViewFactoryClient().closeStage(stage);
        Model.getInstance().getViewFactoryClient().showLoginWindow();
    }

    public void registrazione() throws SQLException {
        if(ModelRegistrazione.verificaEmailEsistente(emailText.getText())){
            AlertHelper.showAlert("Errore", "Email già esistente", null,  Alert.AlertType.ERROR);
            return;
        }
        //Prendo i dati in input
        String password;
        String passwordRipetuta;
        if(passwordField.isVisible()){
            password= passwordVisibile.getText();
            passwordRipetuta= repeatPasswordVisibile.getText();
        }else {
            password= passwordField.getText();
            passwordRipetuta= repeatPasswordField.getText();
        }


        String nome = textNome.getText();
        String cognome = textCognome.getText();
        String email = emailText.getText();
        LocalDate dataNascita = dataNascitaText.getValue();

        if(password.isEmpty() || passwordRipetuta.isEmpty() || nome.isEmpty() || cognome.isEmpty() || email.isEmpty() || dataNascitaText.getValue() == null){
            AlertHelper.showAlert("Errore", "Compila tutti i campi", null,  Alert.AlertType.ERROR);
            return;
        }

        if(!ModelValidazione.controlloEmailvalida(email)){
            labelEmailNonValida.setVisible(true);
            return;
        }else{
            labelEmailNonValida.setVisible(false);
        }
        if(!password.equals(passwordRipetuta)){
            labelPasswordDiverse.setVisible(true);
            return;
        }else {
            labelPasswordDiverse.setVisible(false);}

        if (!ModelValidazione.controlloNome(nome)) {
            labelErroreNome.setVisible(true);
            return;
            } else {
                labelErroreNome.setVisible(false);}

        if(!ModelValidazione.controlloCognome(cognome)){
            labelErroreCognome.setVisible(true);
            return;
            }else{
            labelErroreCognome.setVisible(false);}
        if(!ModelValidazione.controlloData(dataNascita)) {
            labelDataErrata.setVisible(true);
            return;
        }else{
            labelDataErrata.setVisible(false);
        }

        if(password.length()<8){
            AlertHelper.showAlert("Campo password non valido", "La password deve contere almeno 8 caratteri", null, Alert.AlertType.ERROR);
            this.passwordField.clear();
            this.repeatPasswordField.clear();
            return;
        }

        if (ModelRegistrazione.registraUtente(nome,cognome,dataNascita,email,password)) {
            AlertHelper.showAlert("Registrazione effettuata", "Registrazione effettuata con successo", null,  Alert.AlertType.INFORMATION);
            onLogin();
        }
        else {
            AlertHelper.showAlert("Registrazione fallita", "Registrazione fallita", null,  Alert.AlertType.ERROR);
        }
    }

    public void nascondiPassword(){
        boolean isVisible = passwordVisibile.isVisible() && repeatPasswordVisibile.isVisible();

        passwordVisibile.setVisible(!isVisible);
        repeatPasswordVisibile.setVisible(!isVisible);

        passwordField.setVisible(isVisible);
        repeatPasswordField.setVisible(isVisible);

        if (isVisible) {
            passwordField.setText(passwordVisibile.getText());
            repeatPasswordField.setText(repeatPasswordVisibile.getText());
        } else {
            passwordVisibile.setText(passwordField.getText());
            repeatPasswordVisibile.setText(repeatPasswordField.getText());
        }

        passwordVisibile.setManaged(!isVisible);
        repeatPasswordVisibile.setManaged(!isVisible);

        passwordField.setManaged(isVisible);
        repeatPasswordField.setManaged(isVisible);

        eyeButton.setText(isVisible ? "🔍" : "🔒");
    }
}
