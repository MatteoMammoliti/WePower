package com.wepower.wepower.Models.AdminModel;

import com.wepower.wepower.Views.SchedaAllenamento.RigaEsercizioSchedaAdmin;
import java.util.ArrayList;

public class DatiSessioneAdmin {
    // array temporaneo con all'interno gli esercizi che l'admin inserisce durante la creazione di una scheda. viene automaticamente svuotato dopo la conferma/annullamento
    private static ArrayList<RigaEsercizioSchedaAdmin> eserciziSchedaTemp = new  ArrayList<>();

    public static boolean addEsercizioInScheda(RigaEsercizioSchedaAdmin es){

        for(RigaEsercizioSchedaAdmin e : eserciziSchedaTemp)  { // durante la compilazione di una scheda controllo se l'esercizio è già presenze altrimenti lo inserisco
            if(e.equals(es)) return false;
        }
        eserciziSchedaTemp.add(es);
        return true;
    }

    // prelevo gli esercizi dalla scheda compilata per far partire le query di inserimenti
    public static ArrayList<RigaEsercizioSchedaAdmin>  getEserciziSchedaTemp() {
        return eserciziSchedaTemp;
    }

    public static void pulisciScheda () {
        eserciziSchedaTemp.clear();
    }

    public static void eliminaEsercizio(RigaEsercizioSchedaAdmin es){
        eserciziSchedaTemp.remove(es);
    }

    public static void logout() {
        pulisciScheda();
    }
}