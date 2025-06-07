package com.wepower.wepower;

import javafx.collections.ObservableList;
import javafx.scene.Scene;

import java.util.ArrayList;
import java.util.List;

public class ControlloTemi {
    private static ControlloTemi istanza = new ControlloTemi();
    private final List<Scene> scene = new ArrayList<>();
    private String cssTemaCorrente;

    private ControlloTemi() {}

    public static ControlloTemi getInstance() {
        return istanza;
    }

    //Salvo la scena appena creata, e passo anche il nome del tema di LOYAUUT associata alla scena.Questo perchè dopo aver fatto clear devo assicurarmi che
    //il css nell'indice 0(Ovvero quello dello stile del loyaut) venga sempre caricato per primo
    public void aggiungiScena(Scene scena, String cssLoyaut) {
        //Pulisco la scena per non far accumulare i css
        scena.getStylesheets().clear();

        //Aggiungo nuovamento il tema di LOYAUT alla scena.
        scena.getStylesheets().add(cssLoyaut);

        //Aggiungo il tema corrente alla scena
        if (cssTemaCorrente != null) {
            scena.getStylesheets().add(cssTemaCorrente);
        }

        this.scene.add(scena);

    }

    //Cambio il tema corrente
    public void cambiaTema(String temaCss) {
        this.cssTemaCorrente = temaCss;

        //A questo punto devo cambiare il tema a tutte le scene che ho salvato
        for (Scene scena : scene) {
            //Vado a prendermi, per ogni scena, la lista dei file css caricati
            ObservableList<String> stylesheets = scena.getStylesheets();
            //Ora controllo che la scena abbia 2 temi applicati.Il primo, che è quello di LOYAUUT, e il secondo che è il tema corrente.
            //Se ne ha due, vado a sostituire solo il css in posizione 2,ovvero quello del tema corrente
            if (stylesheets.size() > 1) {
                //vado a sostituire il tema corrente, il css in posizione 1.
                stylesheets.set(1, temaCss);
            } else {
                //Se non ha il tema corrente, lo aggiungo, perchè magari viene caricata "prima" la scena.
                stylesheets.add(temaCss);
            }
            //Forzo adesso JavaFX a refreshare subito lo stile css
            scena.getRoot().applyCss();
        }
    }

    public String getCssTemaCorrente() {
        return cssTemaCorrente;
    }
}
