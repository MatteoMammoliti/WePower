package com.wepower.wepower.Controllers.Client.ClientViewsController;

import com.wepower.wepower.Models.SchedaAllenamento.ModelSchedaAllenamentoCliente;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class SchermataCreazioneSchedaController {

    @FXML private Button createScheda;
    @FXML private Button askScheda;

    public SchermataCreazioneSchedaController() {}

    public void initialize() {
        createScheda.setOnAction(event -> ModelSchedaAllenamentoCliente.creaScheda());
        askScheda.setOnAction(event -> ModelSchedaAllenamentoCliente.richiediScheda());
    }
}