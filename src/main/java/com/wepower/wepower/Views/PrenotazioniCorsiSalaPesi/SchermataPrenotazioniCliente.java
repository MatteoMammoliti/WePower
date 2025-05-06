package com.wepower.wepower.Views.PrenotazioniCorsiSalaPesi;

import com.wepower.wepower.Controllers.Client.ClientDashboardController;
import com.wepower.wepower.Models.DatiPalestra.DatiSessionePalestra;
import com.wepower.wepower.Models.DatiPalestra.PrenotazioneSalaPesiCliente;
import com.wepower.wepower.Models.DatiSessioneCliente;
import com.wepower.wepower.Models.DatiPalestra.ModelPrenotazioni;
import com.wepower.wepower.Models.DatiPalestra.PrenotazioneSalaPesi;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Objects;

import static com.wepower.wepower.Models.DatiPalestra.DatiSessionePalestra.getNumeroPrenotazioniDataOraResidue;

public class SchermataPrenotazioniCliente extends VBox {
    private  LocalDate data;
    private Label giornoSettimana;
    private Label giornoDelMese;
    private VBox contenitoreFascieOrario=new VBox(10);
    private Button btnGiornoPrecedente= new Button("← Giorno precedente");
    private Button btnGiornoSuccessivo= new Button("Giorno successivo →");
    VisualizzatoreStoricoPrenotazioni storico;
    VisualizzatoreProssimiAllenamenti prossimiAllenamenti;

    public SchermataPrenotazioniCliente(LocalDate dataGiorno,VisualizzatoreStoricoPrenotazioni storico,VisualizzatoreProssimiAllenamenti prossimiAllenamenti){
        this.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/Styles/Prenotazioni.css")).toExternalForm());

        this.prossimiAllenamenti=prossimiAllenamenti;
        this.storico=storico;
        this.data=dataGiorno;
        this.setPrefWidth(500);
        //Parte superiore giorno settimana,mese e pulsanti precedente e successivo
        giornoSettimana=new Label();
        giornoDelMese=new Label();
        aggiornaLabel();

        aggiornaPrecSucc();

        VBox contenitoreCorpo = new VBox(10);
        HBox contenitoreGiorno = new HBox(10);


        contenitoreGiorno.getChildren().addAll(btnGiornoPrecedente,giornoSettimana,btnGiornoSuccessivo);
        contenitoreGiorno.setAlignment(Pos.CENTER);
        contenitoreCorpo.getChildren().add(giornoDelMese);
        contenitoreCorpo.setAlignment(Pos.CENTER);
        contenitoreCorpo.getChildren().add(contenitoreGiorno);
        this.getChildren().add(contenitoreCorpo);

        contenitoreFascieOrario.setPrefWidth(400);
        contenitoreFascieOrario.setFillWidth(true);

        //Creo e aggiungo le fasce orarie per le prenotazioni
        this.getChildren().add(contenitoreFascieOrario);
        aggiornaFasceorarie();
    }

    public HBox creaRigaPrenotazione(String inizio, String fine, Boolean prenotato, LocalDate data, String ora){
        HBox rigaCompleta = new HBox();
        rigaCompleta.getStyleClass().add("rigaCompleta");

        //Sezione orario-Parte sinistra della riga(Inizio e fine turno)
        VBox sezioneOrario=new VBox();
        sezioneOrario.setAlignment(Pos.CENTER);
        Label labelInizio = new Label(inizio);
        Label labelFine = new Label(fine);
        labelFine.getStyleClass().add("labelFineInizio");
        labelInizio.getStyleClass().add("labelFineInizio");
        sezioneOrario.setPrefWidth(200);
        sezioneOrario.setPrefHeight(80);
        sezioneOrario.getChildren().addAll(labelInizio,labelFine);
        sezioneOrario.getStyleClass().add("contenitoreSezioneOrario");

        if(data.getDayOfWeek() == DayOfWeek.SUNDAY){
            VBox dettagliRiga=new VBox(10);
            dettagliRiga.getStyleClass().add("contenitoreDettagliRiga");
            dettagliRiga.setPrefWidth(500);
            dettagliRiga.setPrefHeight(80);
            Button btnPrenota=new Button("Giorno di chiusura");
            btnPrenota.setDisable(true);
            btnPrenota.getStyleClass().add("btnGiornoChiusura");
            dettagliRiga.setAlignment(Pos.CENTER);
            rigaCompleta.getChildren().add(sezioneOrario);
            dettagliRiga.getChildren().addAll(btnPrenota);
            rigaCompleta.getChildren().add(dettagliRiga);
            return rigaCompleta;
        }

        //Sezione informazioni-parte destra della riga(Sala pesi,Posti liberi,bottone registrati)
        VBox dettagliRiga=new VBox(5);
        dettagliRiga.setPrefWidth(500);
        dettagliRiga.setPrefHeight(50);
        Label sala=new Label("Sala Pesi");
        sala.getStyleClass().add("labelSala");
        sala.setAlignment(Pos.TOP_LEFT);
        PrenotazioneSalaPesiCliente temp=new PrenotazioneSalaPesiCliente(DatiSessioneCliente.getIdUtente(),data.toString(),ora);
        Label postiLiberi=new Label("Posti Liberi:" + " "+ getNumeroPrenotazioniDataOraResidue(temp));
        postiLiberi.setAlignment(Pos.TOP_LEFT);
        postiLiberi.getStyleClass().add("labelPostiDisponibili");

        HBox bottoniRiga = new HBox(10);
        Button btnPrenota=new Button("Prenotati");
        btnPrenota.getStyleClass().clear();
        btnPrenota.getStyleClass().add("btnPrenotati");

        Button btnEliminaPrenotazione=new Button("Elimina prenotazione");
        btnEliminaPrenotazione.setVisible(false);
        btnEliminaPrenotazione.setManaged(false);


        //Controllo se l'utente è già prenotato, se i posti sono tutti o prenotati o se è possibile prenotare
        if(getNumeroPrenotazioniDataOraResidue(temp)==0){
            btnPrenota.setDisable(true);
            btnPrenota.getStyleClass().clear();
            btnPrenota.getStyleClass().add("btnPrenotato");
            btnPrenota.setText("Posti non disponibili");
        }
        if(prenotato){
            btnPrenota.setDisable(true);
            btnPrenota.getStyleClass().clear();
            btnPrenota.getStyleClass().add("btnPrenotato");
            btnPrenota.setText("Prenotazione già effettuata");

            sezioneOrario.getStyleClass().add("contenitoreSezioneOrarioPrenotato");

            btnEliminaPrenotazione.setVisible(true);
            btnEliminaPrenotazione.setManaged(true);
            btnEliminaPrenotazione.getStyleClass().add("btnEliminaPrenotazione");
            btnEliminaPrenotazione.setOnAction(e->{
                boolean eliminazioneEffettuata= ModelPrenotazioni.rimuoviPrenotazioneSalaPesi(data.toString(),ora,DatiSessioneCliente.getIdUtente());
                   if (eliminazioneEffettuata){
                         DatiSessionePalestra.rimuoviPrenotazioneSalaPesi(temp);
                         DatiSessioneCliente.rimuoviPrenotazione(new PrenotazioneSalaPesi(data.toString(), ora));
                         ClientDashboardController.getInstance().loadCalendario();
                       try {
                           prossimiAllenamenti.aggiornaLista();
                       } catch (SQLException ex) {
                           throw new RuntimeException(ex);
                       }
                       aggiornaFasceorarie();
                        }
                   });
        }

        btnPrenota.setOnAction(e -> {
            try {
                boolean andataABuonFine=ModelPrenotazioni.aggiuntiPrenotazioneSalaPesi(data.toString(),ora,DatiSessioneCliente.getIdUtente());
                if(andataABuonFine){
                    DatiSessionePalestra.aggiungiPrenotazioneSalaPesi(temp);
                    DatiSessioneCliente.aggiungiPrenotazione(new PrenotazioneSalaPesi(data.toString(), ora));
                    prossimiAllenamenti.aggiornaLista();
                    ClientDashboardController.getInstance().loadCalendario();
                    aggiornaFasceorarie();

                }


            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        //Caso in cui è prenotabile
        btnPrenota.setAlignment(Pos.CENTER);
        dettagliRiga.getStyleClass().add("contenitoreDettagliRiga");
        bottoniRiga.getChildren().addAll(btnPrenota, btnEliminaPrenotazione);
        dettagliRiga.getChildren().addAll(sala,postiLiberi, bottoniRiga);
        VBox.setMargin(btnPrenota, new Insets(12, 0, 0, 0));
        rigaCompleta.getChildren().addAll(sezioneOrario,dettagliRiga);
        return rigaCompleta;
    }

    public void aggiornaGiorno(LocalDate nuovaData){
        data=nuovaData;
        aggiornaLabel();
        aggiornaPrecSucc();
        aggiornaFasceorarie();

    }

    private void aggiornaLabel(){
        giornoSettimana.setText(data.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ITALIAN));
        giornoDelMese.setText(data.getMonth().getDisplayName(TextStyle.FULL, Locale.ITALIAN)+" "+data.getDayOfMonth());

        giornoDelMese.getStyleClass().add("labelInfoStorico");
        giornoSettimana.getStyleClass().add("labelInfoStorico");
    }


    private void aggiornaFasceorarie() {
        contenitoreFascieOrario.getChildren().clear();
        for(int ora=8;ora<=20;ora+=2){
            String inizio=String.format("%02d:00",ora);
            String fine=String.format("%02d:00",ora+2);


            HBox quadratino=creaRigaPrenotazione(inizio,fine, DatiSessioneCliente.controlloDataPrenotazioneSalaPesi(data,inizio),data, String.valueOf(ora));
            contenitoreFascieOrario.getChildren().add(quadratino);
        }
    }

    private void aggiornaPrecSucc(){
        //pulisco gli stili css per il refresh
        btnGiornoPrecedente.getStyleClass().clear();
        btnGiornoPrecedente.setDisable(false);

        if (data.isEqual(LocalDate.now()) || data.isBefore(LocalDate.now())){
            btnGiornoPrecedente.setDisable(true);
            btnGiornoPrecedente.getStyleClass().add("btnPrecedenteNO");
        }else{
            btnGiornoPrecedente.setDisable(false);
            btnGiornoPrecedente.setOnAction(e -> aggiornaGiorno(data.minusDays(1)));
            btnGiornoPrecedente.getStyleClass().add("btnPrecSucc");
        }
        btnGiornoSuccessivo.getStyleClass().add("btnPrecSucc");
        btnGiornoSuccessivo.setOnAction(e -> aggiornaGiorno(data.plusDays(1)));
    }
}