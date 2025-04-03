package com.wepower.wepower;

import com.wepower.wepower.Models.Model;
import com.wepower.wepower.Views.ViewFactory;
import javafx.application.Application;
import javafx.stage.Stage;

public class WePower extends Application
{

    @Override
    public void start(Stage stage) throws Exception {
        ViewFactory viewFactory = Model.getInstance().getViewFactory();
        viewFactory.showLoginWindow();
    }
}