package com.wepower.wepower.Views.PrenotazioniCorsiSalaPesi;

import com.wepower.wepower.Models.DatiPalestra.DatiSessionePalestra;
import com.wepower.wepower.Models.DatiPalestra.PrenotazioneSalaPesiCliente;
import com.wepower.wepower.Models.DatiSessioneCliente;
import com.wepower.wepower.Models.ModelPrenotazioni;
import com.wepower.wepower.Models.DatiPalestra.PrenotazioneSalaPesi;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

import static com.wepower.wepower.Models.DatiPalestra.DatiSessionePalestra.getNumeroPrenotazioniDataOraResidue;

public class SchermataPrenotazioniCliente extends VBox {
    private static LocalDate data;
    static Label giornoSettimana;
    static Label giornoDelMese;
    private static VBox contenitoreFascieOrario=new VBox(10);
    private static Button btnGiornoPrecedente= new Button("← Giorno precedente");
    private static Button btnGiornoSuccessivo= new Button("Giorno successivo →");



    public SchermataPrenotazioniCliente(LocalDate dataGiorno){
        this.data=dataGiorno;


        //Parte superiore giorno settimana,mese e pulsanti precedente e successivo
        giornoSettimana=new Label();
        giornoDelMese=new Label();
        aggiornaLabel();

        aggiornaPrecSucc();

        VBox contenitoreCorpo = new VBox(10);
        HBox contenitoreGiorno = new HBox(10);


        contenitoreGiorno.getChildren().addAll(btnGiornoPrecedente,giornoSettimana,btnGiornoSuccessivo);
        contenitoreCorpo.getChildren().add(giornoDelMese);
        contenitoreCorpo.getChildren().add(contenitoreGiorno);
        this.getChildren().add(contenitoreCorpo);

        contenitoreFascieOrario.setPrefWidth(800);

        //Creo e aggiungo le fasce orarie per le prenotazioni
        this.getChildren().add(contenitoreFascieOrario);
        aggiornaFasceorarie();


    }




    public static HBox creaRigaPrenotazione(String inizio, String fine, Boolean prenotato, LocalDate data, String ora){
        HBox rigaCompleta = new HBox(5);

        //Sezione orario-Parte sinistra della riga(Inizio e fine turno)
        VBox sezioneOrario=new VBox();
        sezioneOrario.setAlignment(Pos.CENTER);
        Label labelInizio = new Label(inizio);
        Label labelFine = new Label(fine);
        labelFine.setStyle("-fx-font-weight: bold;-fx-text-fill: white;");
        labelInizio.setStyle("-fx-font-weight: bold; -fx-text-fill: white;");
        sezioneOrario.setPrefWidth(100);
        sezioneOrario.setPrefHeight(100);
        sezioneOrario.getChildren().addAll(labelInizio,labelFine);
        sezioneOrario.setStyle("-fx-background-color: blue; -fx-padding: 10;");

        //Sezione informazioni-parte destra della riga(Sala pesi,Posti liberi,bottone registrati)
        VBox dettagliRiga=new VBox(5);
        dettagliRiga.setPrefWidth(400);
        dettagliRiga.setPrefHeight(100);
        Label Sala=new Label("Sala Pesi");
        Sala.setAlignment(Pos.TOP_LEFT);
        PrenotazioneSalaPesiCliente temp=new PrenotazioneSalaPesiCliente(DatiSessioneCliente.getIdUtente(),data.toString(),ora);
        Label PostiLiberi=new Label("Posti Liberi" + " "+ getNumeroPrenotazioniDataOraResidue(temp));
        PostiLiberi.setAlignment(Pos.TOP_LEFT);
        Button btnPrenota=new Button("Prenotati");

        //Controllo se l'utente è già prenotato, se i posti sono tutti o prenotati o se è possibile prenotare
        if(prenotato){
            btnPrenota.setDisable(true);
            String orarioPrenotazione=DatiSessioneCliente.getOrarioPrenotazione(data.toString());
            btnPrenota.setStyle("-fx-background-color: green;-fx-text-fill: white;");


            btnPrenota.setText("Prenotazione già effettuata in questa data alle ore"+" "+orarioPrenotazione);

        }

        btnPrenota.setOnAction(e -> {
            try {
                boolean andataABuonFine=ModelPrenotazioni.aggiuntiPrenotazioneSalaPesi(data.toString(),ora,DatiSessioneCliente.getIdUtente());
                if(andataABuonFine){
                    System.out.println("Prenotazione effettuata");
                    DatiSessionePalestra.aggiungiPrenotazioneSalaPesi(temp);
                    DatiSessioneCliente.aggiungiPrenotazione(new PrenotazioneSalaPesi(data.toString(), ora));
                    aggiornaFasceorarie();
                }


            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        //Caso in cui è prenotabile
        btnPrenota.setAlignment(Pos.CENTER);
        dettagliRiga.setStyle("-fx-background-color: white; -fx-padding: 10; -fx-pref-height: 100; -fx-width: 300;");
        dettagliRiga.getChildren().addAll(Sala,PostiLiberi,btnPrenota);

        rigaCompleta.getChildren().addAll(sezioneOrario,dettagliRiga);


        return rigaCompleta;
    }

    public static void aggiornaGiorno(LocalDate nuovaData){
        data=nuovaData;
        aggiornaLabel();
        aggiornaPrecSucc();
        aggiornaFasceorarie();

    }

    private static void aggiornaLabel(){
        giornoSettimana.setText(data.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ITALIAN));
        giornoDelMese.setText(data.getMonth().getDisplayName(TextStyle.FULL, Locale.ITALIAN)+" "+data.getDayOfMonth());
    }

    private static void aggiornaFasceorarie() {
        contenitoreFascieOrario.getChildren().clear();
        for(int ora=8;ora<=20;ora+=2){
            String inizio=String.format("%02d:00",ora);
            String fine=String.format("%02d:00",ora+2);

            HBox quadratino=creaRigaPrenotazione(inizio,fine, DatiSessioneCliente.controlloDataPrenotazioneSalaPesi(data),data, String.valueOf(ora));
            contenitoreFascieOrario.getChildren().add(quadratino);
        }
    }

    private static void aggiornaPrecSucc(){


        if (data.isBefore(LocalDate.now())){
            btnGiornoPrecedente.setDisable(true);
            btnGiornoPrecedente.setStyle("-fx-background-color: red;-fx-text-fill: white;");
        }else{
            btnGiornoPrecedente.setDisable(false);
            btnGiornoPrecedente.setOnAction(e -> aggiornaGiorno(data.minusDays(1)));
            btnGiornoPrecedente.setStyle("-fx-background-color:white;");
        }
        btnGiornoSuccessivo.setStyle("-fx-background-color: white;");
        btnGiornoSuccessivo.setOnAction(e -> aggiornaGiorno(data.plusDays(1)));
    }
}
