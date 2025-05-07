package com.wepower.wepower.Views.AdminView;


import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class RigaTabellaRichiesteScheda {
    private SimpleIntegerProperty idUtente;
    private SimpleStringProperty nome;
    private SimpleStringProperty cognome;
    private SimpleIntegerProperty peso;
    private SimpleStringProperty sesso;

    public RigaTabellaRichiesteScheda(int id,String nome,String cognome,int peso,String sesso) {
        this.idUtente = new SimpleIntegerProperty(id);
        this.nome = new SimpleStringProperty(nome);
        this.cognome = new SimpleStringProperty(cognome);
        this.peso = new SimpleIntegerProperty(peso);
        this.sesso = new SimpleStringProperty(sesso);
    }

    public int getIdUtente() {return idUtente.get();}
}
