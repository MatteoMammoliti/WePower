package com.wepower.wepower.Models;

import com.wepower.wepower.Controllers.Client.ClientMenuController;
import com.wepower.wepower.Controllers.Client.ClientViewsController.ProfiloController;
import com.wepower.wepower.Controllers.Client.ClientViewsController.SchedaController;
import com.wepower.wepower.Controllers.Client.ClientViewsController.SchermataSelezioneAbbonamentoController;
import com.wepower.wepower.Views.ViewFactoryAdmin;
import com.wepower.wepower.Views.ViewFactoryClient;

public class Model {

    // unica istanza di Model (singleton)
    private ProfiloController profiloController;
    private SchermataSelezioneAbbonamentoController schermataSelezioneAbbonamento;
    private static Model model;
    private ConnessioneDatabase connessioneDatabase;
    private ClientMenuController clientMenuController;
    private SchedaController schedaController;

    // la classe Model (di business) è collegata alla gestione delle viste (viewFactory)
    private final ViewFactoryClient viewFactoryClient;
    private final ViewFactoryAdmin viewFactoryAdmin;

    // il costruttore di Model DEVE essere privato (singleton)
    private Model() {

        this.viewFactoryClient = new ViewFactoryClient();
        this.viewFactoryAdmin = new ViewFactoryAdmin();
    }

    public ViewFactoryClient getViewFactoryClient() {
        return viewFactoryClient;
    }
    public ProfiloController getProfiloController() {return profiloController;}
    public SchermataSelezioneAbbonamentoController getSchermataSelezioneAbbonamento() {return schermataSelezioneAbbonamento;}
    public ViewFactoryAdmin getViewFactoryAdmin() { return viewFactoryAdmin; }
    public ClientMenuController getClientMenuController() { return clientMenuController; }
    public SchedaController getSchedaController() { return schedaController; }

    public void setSchermataSelezioneAbbonamento(SchermataSelezioneAbbonamentoController schermataSelezioneAbbonamento) {this.schermataSelezioneAbbonamento=schermataSelezioneAbbonamento;}
    public void setClientMenuController(ClientMenuController clientMenuController) {
        this.clientMenuController = clientMenuController;
    }
    public void setProfiloController(ProfiloController profiloController) {
        this.profiloController = profiloController;
    }
    public void setSchedaController(SchedaController schedaController) {
        this.schedaController = schedaController;
    }

    public void TestConnessione() {
        connessioneDatabase = new ConnessioneDatabase();
        connessioneDatabase.getConnection();
    }
    // ottengo l'instanza di Model (singleton)
    // synchronized per evitare che più thread possano accedere a Model contemporaneamente
    public static synchronized Model getInstance() {
        if (model == null) {
            model = new Model();
        }
        return model;
    }


    public static synchronized void invalidate() {
        model = null;
    }
}