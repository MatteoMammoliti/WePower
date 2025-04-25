package com.wepower.wepower.Controllers.Client.ClientViewsController;

import com.wepower.wepower.Models.SchedaAllenamento.ModelSchedaAllenamentoCliente;
import javafx.scene.control.Button;

public class SchermataCreazioneSchedaController {
    public Button createScheda;
    public Button askScheda;

    public void initialize() {
        createScheda.setOnAction(event -> ModelSchedaAllenamentoCliente.creaScheda());
        askScheda.setOnAction(event -> ModelSchedaAllenamentoCliente.richiediScheda());
    }
}
