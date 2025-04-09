package com.wepower.wepower.Views;
import com.wepower.wepower.Models.DatiSessioneCliente;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.input.MouseEvent;



import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.function.Consumer;

public class Calendario {
    public static VBox creaCalendario() {
        VBox calendarioBox = new VBox(10);
        calendarioBox.setPrefSize(500, 400);

        YearMonth[] meseCorrente = {YearMonth.now()}; // wrapper per permettere modifica nel listener

        Label meseLabel = new Label();
        meseLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        meseLabel.setAlignment(Pos.CENTER);

        GridPane grigliaGiorni = new GridPane();
        grigliaGiorni.setHgap(5);
        grigliaGiorni.setVgap(5);

        Button btnPrecedente=pulsantePrecSucc("←");
        Button btnSuccessivo =pulsantePrecSucc("→");



        HBox barraNavigazione = new HBox(10, btnPrecedente, meseLabel, btnSuccessivo);
        barraNavigazione.setAlignment(Pos.TOP_LEFT);

        String [] giorniSettimana = {"Lun", "Mar", "Mer", "Gio", "Ven", "Sab", "Dom"};
        HBox barraGiorniSettimana =new HBox();
        for (int i = 0; i < giorniSettimana.length; i++) {
            Label giornoSettimanaLabel = new Label(giorniSettimana[i]);
            giornoSettimanaLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            giornoSettimanaLabel.setPrefWidth(70);
            giornoSettimanaLabel.setAlignment(Pos.CENTER);
            barraGiorniSettimana.getChildren().add(giornoSettimanaLabel);
        }

        calendarioBox.getChildren().addAll(barraNavigazione,barraGiorniSettimana, grigliaGiorni);

        Consumer<YearMonth> aggiornaCalendario = (yearMonth) -> {
            grigliaGiorni.getChildren().clear();

            meseLabel.setText(yearMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.ITALIAN) + " " + yearMonth.getYear());

            LocalDate primoGiornoMese = yearMonth.atDay(1);
            int giorniNelMese = yearMonth.lengthOfMonth();
            int giornoSettimana = primoGiornoMese.getDayOfWeek().getValue(); // 1=Lunedì, 7=Domenica

            for (int i = 1; i <= giorniNelMese; i++) {
                LocalDate data = yearMonth.atDay(i);
                GiornoCalendario giorno= new GiornoCalendario(String.valueOf(i), data.toString());

                // Evidenzio la data di oggi
                if (data.equals(LocalDate.now())) {
                    giorno.setStyle("-fx-background-color: #d1e7dd; -fx-font-weight: bold;");
                }

                //Cerco le date dove il cliente ha prenotato la sala pesi per evidenziarla
                if(DatiSessioneCliente.controlloDataPrenotazioneSalaPesi(data)) {
                    giorno.setStyle("-fx-background-color: #a8e6a3; -fx-font-weight: bold;");
                    giorno.setPrenotato(true);
                }


                int colonna = (giornoSettimana - 1 + i - 1) % 7;
                int riga = (giornoSettimana - 1 + i - 1) / 7;
                grigliaGiorni.add(giorno, colonna, riga);
            }
        };

        btnPrecedente.setOnAction(e -> {
            meseCorrente[0] = meseCorrente[0].minusMonths(1);
            aggiornaCalendario.accept(meseCorrente[0]);
        });

        btnSuccessivo.setOnAction(e -> {
            meseCorrente[0] = meseCorrente[0].plusMonths(1);
            aggiornaCalendario.accept(meseCorrente[0]);
        });

        aggiornaCalendario.accept(meseCorrente[0]);
        return calendarioBox;
    }

    public static Button pulsantePrecSucc(String precedenteOSuccessivo){
        Button pulsante=new Button(precedenteOSuccessivo);
        pulsante.setAlignment(Pos.TOP_RIGHT);
        pulsante.setAlignment(Pos.TOP_RIGHT);

        pulsante.getStyleClass().add("giornoCalendario.css");
        pulsante.getStylesheets().add(Calendario.class.getResource("/Styles/giornoCalendario.css").toExternalForm());
        return pulsante;

    }

}