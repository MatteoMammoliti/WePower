package com.wepower.wepower.Models.DatiPalestra;

public class PrenotazioneSalaPesiCliente {
    private int IdCliente;
    private int IdSalaPesi;
    private String DataPrenotazione;
    private String OrarioPrenotazione;


    public PrenotazioneSalaPesiCliente(int idCliente,String dataPrenotazione, String orarioPrenotazione) {
        if (orarioPrenotazione.matches("\\d{1,2}")) {
            int ora = Integer.parseInt(orarioPrenotazione);
            orarioPrenotazione = String.format("%02d:00", ora);
        }
        this.IdCliente = idCliente;
        this.IdSalaPesi = 1;
        this.DataPrenotazione = dataPrenotazione;
        this.OrarioPrenotazione = orarioPrenotazione;
    }





    //Getter
    public int getIdCliente() {
        return IdCliente;
    }
    public int getIdSalaPesi() {
        return IdSalaPesi;
    }
    public String getDataPrenotazione() {
        return DataPrenotazione;
    }
    public String getOrarioPrenotazione() {
        return OrarioPrenotazione;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PrenotazioneSalaPesiCliente that = (PrenotazioneSalaPesiCliente) obj;
        return IdSalaPesi == that.IdSalaPesi && DataPrenotazione.equals(that.DataPrenotazione) && OrarioPrenotazione.equals(that.OrarioPrenotazione);
    }
    @Override
    public int hashCode() {
        return 31 * IdSalaPesi + DataPrenotazione.hashCode() + OrarioPrenotazione.hashCode();
    }

}
