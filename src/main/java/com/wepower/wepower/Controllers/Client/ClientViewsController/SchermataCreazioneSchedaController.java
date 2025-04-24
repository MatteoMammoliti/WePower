package com.wepower.wepower.Controllers.Client.ClientViewsController;

import com.wepower.wepower.Models.ConnessioneDatabase;
import com.wepower.wepower.Models.DatiSessioneCliente;
import com.wepower.wepower.Models.Model;
import com.wepower.wepower.Models.SchedaAllenamento.ModelSchedaAllenamento;
import javafx.scene.control.Button;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SchermataCreazioneSchedaController {
    public Button createScheda;
    public Button askScheda;

    public void initialize() {
        createScheda.setOnAction(event -> ModelSchedaAllenamento.creaScheda());
        askScheda.setOnAction(event -> ModelSchedaAllenamento.richiediScheda());
    }
}
