package com.wepower.wepower.Views.AdminView;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class RigaDashboardAdmin {

    private final SimpleIntegerProperty idCliente;
    private final SimpleStringProperty nome;
    private final SimpleStringProperty cognome;
    private final SimpleStringProperty dataNascita;
    private final SimpleStringProperty statoAbbonamento;
    private final SimpleStringProperty dataScadenza;
    private final SimpleStringProperty dataRinnovo;
    private final SimpleStringProperty sesso;
    private final SimpleStringProperty email;
    private final SimpleStringProperty statoCertificato;


    public RigaDashboardAdmin(int id, String nome, String cognome, String dataNascita, int statoAbbonamento, int statoCertificato, String dataRinnovo, String dataScadenza,String email, String sesso) {
        this.idCliente = new SimpleIntegerProperty(id);
        this.nome = new SimpleStringProperty(nome);
        this.cognome = new SimpleStringProperty(cognome);
        this.dataNascita=new SimpleStringProperty(dataNascita);
        this.dataRinnovo = new SimpleStringProperty(String.valueOf(dataRinnovo));
        this.dataScadenza = new SimpleStringProperty(String.valueOf(dataScadenza));
        this.email = new SimpleStringProperty(email);
        this.sesso = new SimpleStringProperty(sesso);
        if (statoAbbonamento==1){
            this.statoAbbonamento = new SimpleStringProperty("Attivo");
        }
        else{
            this.statoAbbonamento = new SimpleStringProperty("Non Attivo");

        }

        if (statoCertificato==1){
            this.statoCertificato = new SimpleStringProperty("OK");
        }
        else{
            this.statoCertificato = new SimpleStringProperty("X");
        }

    }

    public SimpleIntegerProperty idClienteProperty() {
        return idCliente;
    }

    public SimpleStringProperty nomeProperty() {
        return nome;
    }

    public SimpleStringProperty cognomeProperty() {
        return cognome;
    }
    public SimpleStringProperty dataNascitaProperty() {
        return this.dataNascita;
    }

    public SimpleStringProperty statoAbbonamentoProperty() {
        return statoAbbonamento;
    }

    public SimpleStringProperty dataRinnovoProperty() {
        return dataRinnovo;
    }

    public SimpleStringProperty dataScadenzaProperty() {
        return dataScadenza;
    }

    public SimpleStringProperty emailProperty() {
        return email;
    }

    public SimpleStringProperty sessoProperty() {
        return sesso;
    }

    public SimpleStringProperty statoCertificatoProperty() {
        return statoCertificato;
    }
    public int getIdCliente() {
        return idCliente.get();
    }
    public String getNome() {
        return nome.get();
    }
    public String getCognome() {
        return cognome.get();
    }
    public String getDataNascita() {
        return dataNascita.get();
    }
    public String getStatoAbbonamento() {
        return statoAbbonamento.get();
    }
    public String getDataRinnovo() {
        return dataRinnovo.get();
    }
    public String getEmail() {
        return email.get();
    }
    public String getStatoCertificato() {
        return statoCertificato.get();
    }
    public String getDataScadenza() {
        return dataScadenza.get();
    }

    public String getSesso() {
        return sesso.get();
    }

    public void setIdCliente(int idCliente) {
        this.idCliente.set(idCliente);
    }
    public void setNome(String nome) {
        this.nome.set(nome);
    }
    public void setCognome(String cognome) {
        this.cognome.set(cognome);
    }
    public void setDataNascita(String dataNascita) {
        this.dataNascita.set(dataNascita);
    }
    public void setStatoAbbonamento(String statoAbbonamento) {
        this.statoAbbonamento.set(statoAbbonamento);
    }
    public void setDataRinnovo(String dataRinnovo) {
        this.dataRinnovo.set(dataRinnovo);
    }
    public void setEmail(String email) {
        this.email.set(email);
    }
    public void setStatoCertificato(String statoCertificato) {
        this.statoCertificato.set(statoCertificato);
    }
    public void setDataScadenza(String dataScadenza) {
        this.dataScadenza.set(dataScadenza);
    }
    public void setSesso(String sesso) {
        this.sesso.set(sesso);
    }


}
