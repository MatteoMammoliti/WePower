package com.wepower.wepower.Views.SchedaAllenamento;

import com.wepower.wepower.Models.ConnessioneDatabase;
import com.wepower.wepower.Models.DatiSessioneCliente;
import com.wepower.wepower.Models.Model;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class RigaEsercizioScheda extends HBox {
    private Label nomeEsercizio;
    private Label descrizioneEsercizio;
    private Label muscoloAllenato;
    private Label numeroSerie;
    private Label numeroRipetizioni;
    private ImageView imageEsercizio;
    private Label massimaleAttuale;
    private Label dataImpostazioneMassimale;
    private TextField aggiungiNuovoMassimale;
    private Button aggiungiMassimale;
    private Button rimuoviSchedaEsercizio;


    public RigaEsercizioScheda(String nomeEsercizio, String descrizioneEsercizio, String muscoloAllenato, String numeroSerie, String numeroRipetizioni, String percorsoImmagine, String massimaleAttuale, String dataImpostazioneMassimale) {
        this.nomeEsercizio = new Label(nomeEsercizio);
        this.descrizioneEsercizio = new Label(descrizioneEsercizio);
        this.muscoloAllenato = new Label("Muscolo allenato: " + muscoloAllenato);
        this.numeroSerie = new Label(numeroSerie);
        this.numeroRipetizioni = new Label(numeroRipetizioni);

        this.massimaleAttuale = new Label("Massimale attuale: " + massimaleAttuale);
        this.dataImpostazioneMassimale = new Label("Data: " + dataImpostazioneMassimale);

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
        this.aggiungiMassimale.setOnAction(event -> onAggiungiNuovoMassimale());

        this.rimuoviSchedaEsercizio = new Button("Rimuovi esercizio dalla scheda");
        this.rimuoviSchedaEsercizio.setOnAction(event -> onRimuoviEsercizio());

        this.getChildren().addAll(this.nomeEsercizio, this.descrizioneEsercizio, this.muscoloAllenato, this.numeroSerie, this.numeroRipetizioni, this.imageEsercizio, this.massimaleAttuale ,this.aggiungiNuovoMassimale, this.dataImpostazioneMassimale,
                this.aggiungiMassimale,this.rimuoviSchedaEsercizio);
        this.setSpacing(10);
        this.setPadding(new Insets(10));
    }

    private void onRimuoviEsercizio() {
        String eliminaEsercizio = "DELETE FROM ComposizioneSchedaAllenamento WHERE NomeEsercizio = ? AND IdSchedaAllenamento = ?";

        try (Connection conn = ConnessioneDatabase.getConnection()) {
            PreparedStatement eliminazione = conn.prepareStatement(eliminaEsercizio);
            eliminazione.setString(1, this.nomeEsercizio.getText());
            eliminazione.setInt(2, DatiSessioneCliente.getIdSchedaAllenamento());
            eliminazione.executeUpdate();

            Model.getInstance().getSchedaController().loadSchedaAllenamento();
            Model.getInstance().getSchedaController().loadEsercizi();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void onAggiungiNuovoMassimale () {
        this.aggiungiNuovoMassimale.setVisible(true);
        String massimale = "INSERT INTO MassimaleImpostatoCliente (IdCliente, NomeEsercizio, DataInserimento, Peso) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnessioneDatabase.getConnection()) {
            PreparedStatement inserimento = conn.prepareStatement(massimale);
            inserimento.setInt(1, DatiSessioneCliente.getIdUtente());
            inserimento.setString(2, this.nomeEsercizio.getText());
            inserimento.setDate(3, Date.valueOf(LocalDate.now().toString()));
            inserimento.setDouble(4, Double.parseDouble(this.aggiungiNuovoMassimale.getText()));
            inserimento.executeUpdate();

            Model.getInstance().getSchedaController().loadSchedaAllenamento();
            Model.getInstance().getSchedaController().loadEsercizi();
            Model.getInstance().getViewFactoryClient().invalidateDashboard();
            DatiSessioneCliente.aggiungiEsercizioConMassimale(this.nomeEsercizio.getText());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}