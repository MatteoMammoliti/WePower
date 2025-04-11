package com.wepower.wepower.Views.SchedaAllenamento;

import com.wepower.wepower.Models.ConnessioneDatabase;
import com.wepower.wepower.Models.DatiSessioneCliente;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RigaEsercizioScheda extends HBox {
    private Label nomeEsercizio;
    private Label descrizioneEsercizio;
    private Label muscoloAllenato;
    private Label numeroSerie;
    private Label numeroRipetizioni;
    private ImageView imageEsercizio;
    private Label massimaleAttuale;
    private TextField aggiungiNuovoMassimale;
    private Button aggiungiMassimale;
    private Button rimuoviSchedaEsercizio;


    public RigaEsercizioScheda(String nomeEsercizio, String descrizioneEsercizio, String muscoloAllenato, String numeroSerie, String numeroRipetizioni, String percorsoImmagine, String massimaleAttuale, Runnable aggiornaUI) {
        this.nomeEsercizio = new Label(nomeEsercizio);
        this.descrizioneEsercizio = new Label(descrizioneEsercizio);
        this.muscoloAllenato = new Label("Muscolo allenato: " + muscoloAllenato);
        this.numeroSerie = new Label(numeroSerie);
        this.numeroRipetizioni = new Label(numeroRipetizioni);

        this.massimaleAttuale = new Label("Massimale attuale: " + massimaleAttuale);
        // eventualmente inserire data

        this.aggiungiNuovoMassimale = new TextField();
        this.aggiungiNuovoMassimale.setPromptText("Aggiungi nuovo massimale");
        this.aggiungiNuovoMassimale.setVisible(false);


        InputStream is = getClass().getResourceAsStream("/" + percorsoImmagine);
        Image image = new Image(is);
        this.imageEsercizio = new ImageView(image);
        this.imageEsercizio.setFitHeight(100);
        this.imageEsercizio.setFitWidth(100);
        this.imageEsercizio.setPreserveRatio(true);

        this.aggiungiMassimale = new Button("Aggiungi massimale");
        this.aggiungiMassimale.setOnAction(event -> {
            onAggiungiNuovoMassimale();
            aggiornaUI.run();
        });

        this.rimuoviSchedaEsercizio = new Button("Rimuovi esercizio dalla scheda");
        this.rimuoviSchedaEsercizio.setOnAction(event -> {
            try {
                onRimuoviEsercizio();
                aggiornaUI.run();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        this.getChildren().addAll(this.nomeEsercizio, this.descrizioneEsercizio, this.muscoloAllenato, this.numeroSerie, this.numeroRipetizioni, this.imageEsercizio, this.massimaleAttuale, this.aggiungiNuovoMassimale,
                this.aggiungiMassimale,this.rimuoviSchedaEsercizio);
        this.setSpacing(10);
        this.setPadding(new Insets(10));
    }

    private void onRimuoviEsercizio() throws SQLException {
        String eliminaEsercizio = "DELETE FROM ComposizioneSchedaAllenamento WHERE NomeEsercizio = ? AND IdSchedaAllenamento = ?";
        String eliminaMassimale = "DELETE FROM MassimaleImpostatoCliente WHERE NomeEsercizio = ? AND IdCliente = ?";

        try (Connection conn = ConnessioneDatabase.getConnection()) {
            PreparedStatement eliminazione = conn.prepareStatement(eliminaEsercizio);
            eliminazione.setString(1, this.nomeEsercizio.getText());
            eliminazione.setInt(2, DatiSessioneCliente.getIdSchedaAllenamento());
            eliminazione.executeUpdate();

            PreparedStatement eliminazioneMassimale = conn.prepareStatement(eliminaMassimale);
            eliminazioneMassimale.setString(1, this.nomeEsercizio.getText());
            eliminazioneMassimale.setInt(2, DatiSessioneCliente.getIdUtente());
            eliminazioneMassimale.executeUpdate();
        }
    }

    private void onAggiungiNuovoMassimale () {
        this.aggiungiNuovoMassimale.setVisible(true);
        String massimale = "INSERT INTO MassimaleImpostatoCliente (IdCliente, NomeEsercizio, DataInserimento, Peso) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnessioneDatabase.getConnection()) {
            PreparedStatement inserimento = conn.prepareStatement(massimale);
            inserimento.setInt(1, DatiSessioneCliente.getIdUtente());
            inserimento.setString(2, this.nomeEsercizio.getText());
            inserimento.setDate(3, java.sql.Date.valueOf(java.time.LocalDate.now()));
            inserimento.setDouble(4, Double.parseDouble(this.aggiungiNuovoMassimale.getText()));
            inserimento.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}