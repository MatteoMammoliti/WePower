package com.wepower.wepower.Models.DatiPalestra;

public class PrenotazioneSalaPesi {

    private String dataPrenotazione;
    private String orarioPrenotazione;

    public PrenotazioneSalaPesi(String dataPrenotazione, String orarioPrenotazione) {
        if (orarioPrenotazione.matches("\\d{1,2}")) {
            int ora = Integer.parseInt(orarioPrenotazione);
            orarioPrenotazione = String.format("%02d:00", ora);
        }
        this.dataPrenotazione = dataPrenotazione;
        this.orarioPrenotazione = orarioPrenotazione;
    }

    public boolean equals(Object obj){
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PrenotazioneSalaPesi that = (PrenotazioneSalaPesi) obj;
        return dataPrenotazione.equals(that.dataPrenotazione) && orarioPrenotazione.equals(that.orarioPrenotazione);
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
