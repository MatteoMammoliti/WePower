package com.wepower.wepower.Models;

import com.wepower.wepower.Views.AlertHelper;
import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

public class ModelValidazione {

    public static boolean controlloEmailvalida(String email){
        return Pattern.matches("^[A-Za-z0-9](?:[A-Za-z0-9+_.-]*[A-Za-z0-9])?@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$",email);
    }

    //Controllo se esiste già un email uguale registrata
    public static boolean controlloEmailEsistente(String email) {
        Connection conn = ConnessioneDatabase.getConnection();
        String emailLower= email.toLowerCase();

        if(email.equals(DatiSessioneCliente.getEmail())) return false;

        String cerco="SELECT Email FROM CredenzialiCliente WHERE Email=?";

        try {
            PreparedStatement ps=conn.prepareStatement(cerco);
            ps.setString(1,emailLower);
            ResultSet rs=ps.executeQuery();

            if(rs.next()) return true;
        } catch (SQLException e) {
            AlertHelper.showAlert("Questo non doveva succedere", " Errore durante la verifica campo email", null, Alert.AlertType.ERROR);
        }
        return false;
    }

    public static boolean controlloNome(String nome){return Pattern.matches("^[A-Za-z]+(\\s+[A-Za-z]+)*$", nome.trim()); }

    public static boolean controlloCognome(String cognome){ return Pattern.matches("^[a-zA-Z-]+",cognome); }

    public static boolean controllonomeCognome(String nomeCognome){
        String regexNomeCognome="^[a-zA-Z]+(\\s[a-zA-Z]+)+$";
        return Pattern.matches(regexNomeCognome,nomeCognome);
    }
    public static boolean controlloNumeroCarta(String nCarta){
        return Pattern.matches("^\\d{13,19}$",nCarta);
    }

    public static boolean controlloNumeroCVC(String cvc){ return Pattern.matches("^\\d{3}$",cvc); }

    public static boolean controlloDataScadenzacarta(String data){
            // verifica sintattica (01-12)/(00-99)
            data=data.trim();
            if (!Pattern.matches("^(0[1-9]|1[0-2])/\\d{2}$",data)) return false;

            // parsing sicuro con YearMonth
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MM/yy");
            YearMonth scadenza;

            try {
                scadenza = YearMonth.parse(data, fmt);
            } catch (DateTimeParseException ex) {
                return false;
            }

            // confronto con il mese/anno corrente
            YearMonth adesso = YearMonth.now();

            return !scadenza.isBefore(adesso);
    }

    public static boolean controlloAltezza(String altezza){
        if (altezza == null || altezza.isEmpty()) { return true; }

        if (!Pattern.matches("^[0-9]{1,3}$", altezza)) { return false; }

        return Integer.parseInt(altezza) >= 50 && Integer.parseInt(altezza) <= 250;
    }

    public static boolean controlloData(LocalDate data){
        LocalDate dataMassima = LocalDate.of(2015, 12, 31); // limite massimo: fine 2015
        LocalDate dataMinima = LocalDate.now().minusYears(80); // limite minimo: massimo 80 anni fa

        return (data.isEqual(dataMassima) || data.isBefore(dataMassima)) && (data.isEqual(dataMinima) || data.isAfter(dataMinima));
    }

    public static boolean controlloPeso(String peso) {
        if (peso == null || peso.isEmpty()) { return true; }
        if (!Pattern.matches("^[0-9]{1,3}$", peso)) { return false; }
        return Integer.parseInt(peso) >= 30 && Integer.parseInt(peso) <= 200;
    }

    public static boolean controlloNomeOfferta(String nome){ return Pattern.matches("^[A-Za-zÀ-ÿ\s]{2,50}$",nome); }

    public static boolean controlloNumeroTelefono(String telefono){ return Pattern.matches("^(\\+39)?\\s?\\d{9,10}$",telefono) || telefono.isEmpty(); }

    public static boolean controlloPrezzoOfferta(String prezzo){ return Pattern.matches("^[1-9][0-9]{0,3}$",prezzo); }

    public static boolean controlloDurataOfferta(String durata){ return Pattern.matches("^[1-9][0-9]*$",durata); }
}