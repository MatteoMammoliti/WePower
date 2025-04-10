package com.wepower.wepower.Models.DatiPalestra;

public class Corso {
    private static int idCorso;
    private static String nomeCorso;
    private static String durataCorso;
    private static String descrizioneCorso;
    private static int PrezzoCorso;

    public Corso(int id, String nome, String durata, String descrizione, int prezzo) {
        idCorso = id;
        nomeCorso = nome;
        durataCorso = durata;
        descrizioneCorso = descrizione;
        PrezzoCorso = prezzo;
    }

    public boolean equals(Object obj){
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Corso that = (Corso) obj;
        return nomeCorso.equals(that.nomeCorso);


    }
    //Getter
    public static int getIdCorso() {return idCorso;}
    public static String getNomeCorso() {return nomeCorso;}
    public static String getDurataCorso() {return durataCorso;}
    public static String getDescrizioneCorso() {return descrizioneCorso;}
    public static int getPrezzoCorso() {return PrezzoCorso;}
}
