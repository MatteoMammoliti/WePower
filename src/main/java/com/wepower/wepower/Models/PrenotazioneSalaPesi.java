package com.wepower.wepower.Models;

public class PrenotazioneSalaPesi {

    private String dataPrenotazione;
    private String orarioPrenotazione;

    public PrenotazioneSalaPesi(String dataPrenotazione, String orarioPrenotazione) {

        this.dataPrenotazione = dataPrenotazione;
        this.orarioPrenotazione = orarioPrenotazione;
    }

    public String getDataPrenotazione() {
        return dataPrenotazione;
    }

    public String getOrarioPrenotazione() {
        return orarioPrenotazione;
    }

    public void setDataPrenotazione(String dataPrenotazione) {
        this.dataPrenotazione = dataPrenotazione;
    }

    public void setOrarioPrenotazione(String orarioPrenotazione) {
        this.orarioPrenotazione = orarioPrenotazione;
    }
}
