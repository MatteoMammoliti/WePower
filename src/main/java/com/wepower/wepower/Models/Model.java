package com.wepower.wepower.Models;

import com.wepower.wepower.Controllers.Admin.SchedeAdminController;
import com.wepower.wepower.Controllers.Admin.SchermataCreazioneSchedaAdmin;
import com.wepower.wepower.Controllers.Admin.TabellaCertificatiAdminController;
import com.wepower.wepower.Controllers.Client.ClientDashboardController;
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
    private ClientMenuController clientMenuController;
    private ClientDashboardController  clientDashboardController;
    private SchedaController schedaController;
    private SchermataCreazioneSchedaAdmin  schermataCreazioneSchedaAdmin;
    private SchedeAdminController schedeAdminController;
    private TabellaCertificatiAdminController tabellaCertificatiAdminController;

    // la classe Model (di business) è collegata alla gestione delle viste (viewFactory)
    private final ViewFactoryClient viewFactoryClient;
    private final ViewFactoryAdmin viewFactoryAdmin;

    // il costruttore di Model DEVE essere privato (singleton)
    private Model() {
        this.viewFactoryClient = new ViewFactoryClient();
        this.viewFactoryAdmin = new ViewFactoryAdmin();
    }

    // GETTER
    public ViewFactoryClient getViewFactoryClient() {
        return viewFactoryClient;
    }
    public ProfiloController getProfiloController() {return profiloController;}
    public ViewFactoryAdmin getViewFactoryAdmin() { return viewFactoryAdmin; }
    public ClientMenuController getClientMenuController() { return clientMenuController; }
    public SchedaController getSchedaController() { return schedaController; }
    public ClientDashboardController getClientDashboardController() {
        return clientDashboardController;
    }
    public SchermataCreazioneSchedaAdmin getSchermataCreazioneSchedaAdminController() { return schermataCreazioneSchedaAdmin; }
    public SchedeAdminController getSchedeAdminController() { return schedeAdminController; }
    public TabellaCertificatiAdminController getTabellaCertificatiAdminController() { return tabellaCertificatiAdminController; }

    // SETTER
    public void setClientMenuController(ClientMenuController clientMenuController) { this.clientMenuController = clientMenuController; }
    public void setProfiloController(ProfiloController profiloController) { this.profiloController = profiloController; }
    public void setSchedaController(SchedaController schedaController) {
        this.schedaController = schedaController;
    }
    public void setSchermataCreazioneSchedaAdminController(SchermataCreazioneSchedaAdmin schermataCreazioneSchedaAdmin) { this.schermataCreazioneSchedaAdmin = schermataCreazioneSchedaAdmin; }
    public void setClientDashboardController(ClientDashboardController clientDashboardController) { this.clientDashboardController = clientDashboardController; }
    public void setSchedeAdminController(SchedeAdminController schedeAdminController) { this.schedeAdminController = schedeAdminController; }
    public void setTabellaCertificatiAdminController(TabellaCertificatiAdminController tabellaCertificatiAdminController) { this.tabellaCertificatiAdminController = tabellaCertificatiAdminController; }

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