package com.wepower.wepower.Models.DatiPalestra;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DatiSessionePalestra {
    private static int numeroMassimePrenotazioniPerFascieOrarie=50;
    private static Map<PrenotazioneSalaPesiCliente, Integer> prenotazioniSalePesi= new HashMap<>();

    //Aggiungo una prenotazione, se in quella data e ora sono già state fatte delle prenotazioni, incremento il contatore
    public static void aggiungiPrenotazioneSalaPesi(PrenotazioneSalaPesiCliente prenotazione) {
        if(prenotazioniSalePesi.containsKey(prenotazione)) {
            prenotazioniSalePesi.put(prenotazione, prenotazioniSalePesi.get(prenotazione) + 1);
        } else {
            prenotazioniSalePesi.put(prenotazione, 1);
        }
    }

    //Funzione che mi serve per capire quanti posti prenotabili rimangono per ogni fascia oraria/giorno
    public static int getNumeroPrenotazioniDataOraResidue(PrenotazioneSalaPesiCliente prenotazione) {
        if(prenotazioniSalePesi.containsKey(prenotazione)) {
            return numeroMassimePrenotazioniPerFascieOrarie - prenotazioniSalePesi.get(prenotazione);
        } else {
            return numeroMassimePrenotazioniPerFascieOrarie;
        }
    }

    public static boolean rimuoviPrenotazioneSalaPesi(PrenotazioneSalaPesiCliente prenotazione) {
        if(prenotazioniSalePesi.containsKey(prenotazione)) {
            int contatore = prenotazioniSalePesi.get(prenotazione);
            if(contatore > 1) {
                prenotazioniSalePesi.put(prenotazione, contatore - 1);
            } else {
                prenotazioniSalePesi.remove(prenotazione);
            }
            return true;
        } else {
            return false;
        }

    }
}
