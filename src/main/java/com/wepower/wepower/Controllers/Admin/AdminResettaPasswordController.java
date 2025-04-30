package com.wepower.wepower.Controllers.Admin;

import com.wepower.wepower.Models.AdminModel.ResettaPasswordAdmin;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminResettaPasswordController implements Initializable {
    @FXML
    private Button inputGeneraPassword;
    @FXML
    private Button inputAnnulla;
    @FXML
    private TextField inputTextPassword;
    @FXML
    private Button inputSalva;

    private int idCliente;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        inputGeneraPassword.setOnAction(e -> {
            String password= ResettaPasswordAdmin.onGenera();
            inputTextPassword.setText(password);
        });

        inputSalva.setOnAction(e -> {ResettaPasswordAdmin.onSalva(idCliente, inputTextPassword.getText());
                Stage stage = (Stage) inputSalva.getScene().getWindow();
                stage.close();}
        );
        inputAnnulla.setOnAction(e -> {
            Stage stage = (Stage) inputAnnulla.getScene().getWindow();
            stage.close();
        });
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }
}
