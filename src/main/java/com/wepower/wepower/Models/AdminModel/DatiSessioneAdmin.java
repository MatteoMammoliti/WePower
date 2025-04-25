package com.wepower.wepower.Models.AdminModel;

import com.wepower.wepower.Views.SchedaAllenamento.RigaEsercizioListaAdmin;
import com.wepower.wepower.Views.SchedaAllenamento.RigaEsercizioSchedaAdmin;

import java.util.ArrayList;

public class DatiSessioneAdmin {
    private static ArrayList<RigaEsercizioSchedaAdmin> eserciziSchedaTemp = new  ArrayList<>();


    public static boolean addEsercizioInScheda(RigaEsercizioSchedaAdmin es){

        for(RigaEsercizioSchedaAdmin e : eserciziSchedaTemp)  {
            if(e.equals(es)) return false;
        }
        eserciziSchedaTemp.add(es);
        return true;
    }

    public static ArrayList<RigaEsercizioSchedaAdmin>  getEserciziSchedaTemp() {
        return  eserciziSchedaTemp;
    }

    public static void pulisciScheda () {
        eserciziSchedaTemp.clear();
    }

    public static void eliminaEsercizio(RigaEsercizioSchedaAdmin es){
        eserciziSchedaTemp.remove(es);
    }

    public void logout() {
        pulisciScheda();
    }
}
