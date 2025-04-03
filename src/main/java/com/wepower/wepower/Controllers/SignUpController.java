package com.wepower.wepower.Controllers;

import com.wepower.wepower.Models.Model;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {
    public Button loginButton;
    public PasswordField PasswordField;
    public TextField passwordVisibile;
    public TextField RepeatPasswordVisibile;
    public PasswordField RepeatPasswordField;
    public Button eyeButton;
    public Button signUpButton;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loginButton.setOnAction(event -> onLogin());

        signUpButton.setOnAction(event -> onSignUp());

        eyeButton.setOnAction(event -> nascondiPassword());
    }

    public void onLogin() {
        Stage stage = (Stage) loginButton.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
        Model.getInstance().getViewFactory().showLoginWindow();
    }

    public void onSignUp() {
        Stage stage = (Stage) signUpButton.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
        Model.getInstance().getViewFactory().showDashboardClient();
    }

    public void nascondiPassword(){
        boolean isVisible = passwordVisibile.isVisible() && RepeatPasswordVisibile.isVisible();

        passwordVisibile.setVisible(!isVisible);
        RepeatPasswordVisibile.setVisible(!isVisible);


        PasswordField.setVisible(isVisible);
        RepeatPasswordField.setVisible(isVisible);

        if (isVisible) {
            PasswordField.setText(passwordVisibile.getText());
            RepeatPasswordField.setText(RepeatPasswordVisibile.getText());
        } else {
            passwordVisibile.setText(PasswordField.getText());
            RepeatPasswordVisibile.setText(RepeatPasswordField.getText());
        }

        passwordVisibile.setManaged(!isVisible);
        RepeatPasswordVisibile.setManaged(!isVisible);

        PasswordField.setManaged(isVisible);
        RepeatPasswordField.setManaged(isVisible);

        eyeButton.setText(isVisible ? "🔍" : "🔒");
    }
}