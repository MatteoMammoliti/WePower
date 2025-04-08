package com.wepower.wepower.Controllers.Admin;

import com.wepower.wepower.Models.TabellaUtentiDashboardAdmin;
import com.wepower.wepower.Views.RigaDashboardAdmin;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AdminDashboardController implements Initializable {

    @FXML
    private VBox containerUsers;

    private void loadUtenti() throws SQLException {
        ArrayList<RigaDashboardAdmin> A = TabellaUtentiDashboardAdmin.riempiRiga();
        containerUsers.getChildren().clear();
        containerUsers.getChildren().addAll(A);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadUtenti();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
