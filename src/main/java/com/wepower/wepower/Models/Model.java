package com.wepower.wepower.Models;

import com.wepower.wepower.Views.ViewFactory;

public class Model {

    // unica istanza di Model (singleton)
    private static Model model;
    private ConnessioneDatabase connessioneDatabase;

    // la classe Model (di business) è collegata alla gestione delle viste (viewFactory)
    private final ViewFactory viewFactory;

    // il costruttore di Model DEVE essere privato (singleton)
    private Model() {
        this.viewFactory = new ViewFactory();
    }

    public ViewFactory getViewFactory() {
        return viewFactory;
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

}