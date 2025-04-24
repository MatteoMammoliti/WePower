package com.wepower.wepower.Models.AdminModel;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class RigaTabellaCertificati {
    //Usiamo SimpleStringProperty e SimpleIntegerProperty per le proprietà della tabella, in questo modo la stringa
    //può notificare automaticamente i cambiamenti alla tabella.
    //Quando un dato cambia, la tabella viene aggiornata automaticamente, usando la proprietà data binding di JavaFX
    private final SimpleStringProperty nome;
    private final SimpleStringProperty cognome;
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty dataCaricamento;


    public RigaTabellaCertificati(String nome, String cognome, int id,String dataCaricamento) {

        //Inizializzo le SimpleStringProperty e SimpleIntegerProperty, diciamo che queste proprietà siano "osservabili"
        //ogni volta che utilizzeremo il set, javaFx può aggiornare la UI automaticamente
        this.nome = new SimpleStringProperty(nome);
        this.cognome = new SimpleStringProperty(cognome);
        this.id = new SimpleIntegerProperty(id);
        this.dataCaricamento = new SimpleStringProperty(dataCaricamento);
    }

    //Questi metodi getter servono per restituire i valori delle proprietà(nome,cognome,id)
    //Accede al valore attuale del campo e lo restituisce
    public StringProperty nomeProperty() {return nome;}
    public StringProperty cognomeProperty() {return cognome;}
    public IntegerProperty idProperty() {return id;}
    public StringProperty dataCaricamentoProperty() {return dataCaricamento;}

    public String nome(){return nome.get();}
    public String cognome(){return cognome.get();}
    public int id(){return id.get();}
    public String dataCaricamento(){return dataCaricamento.get();}


}
