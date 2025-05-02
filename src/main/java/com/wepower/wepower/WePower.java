package com.wepower.wepower;

import com.wepower.wepower.Models.Model;
import com.wepower.wepower.Views.ViewFactoryClient;
import javafx.application.Application;
import javafx.stage.Stage;

public class WePower extends Application
{

    @Override
    public void start(Stage stage) throws Exception {

        ViewFactoryClient viewFactory = Model.getInstance().getViewFactoryClient();
        Model.getInstance().TestConnessione();
        String temaDefault=getClass().getResource("/Styles/TemaBlu.css").toExternalForm();
        ControlloTemi.getInstance().cambiaTema(temaDefault);

        viewFactory.showLoginWindow();
    }
}