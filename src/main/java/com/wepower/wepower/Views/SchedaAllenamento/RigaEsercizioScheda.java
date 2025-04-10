package com.wepower.wepower.Views.SchedaAllenamento;

import com.wepower.wepower.Models.ConnessioneDatabase;
import com.wepower.wepower.Models.DatiSessioneCliente;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    private Button rimuoviSchedaEsercizio;


    public RigaEsercizioScheda(String nomeEsercizio, String descrizioneEsercizio, String muscoloAllenato, String numeroSerie, String numeroRipetizioni, String percorsoImmagine, Runnable aggiornaUI) {
        this.nomeEsercizio = new Label(nomeEsercizio);
        this.descrizioneEsercizio = new Label(descrizioneEsercizio);
        this.muscoloAllenato = new Label("Muscolo allenato: " + muscoloAllenato);
        this.numeroSerie = new Label(numeroSerie);
        this.numeroRipetizioni = new Label(numeroRipetizioni);

        InputStream is = getClass().getResourceAsStream("/" + percorsoImmagine);

        Image image = new Image(is);
        this.imageEsercizio = new ImageView(image);
        this.imageEsercizio.setFitHeight(100);
        this.imageEsercizio.setFitWidth(100);
        this.imageEsercizio.setPreserveRatio(true);

        this.rimuoviSchedaEsercizio = new Button("Rimuovi esercizio dalla scheda");
        this.rimuoviSchedaEsercizio.setOnAction(event -> {
            try {
                onRimuoviEsercizio();
                aggiornaUI.run();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        this.getChildren().addAll(this.nomeEsercizio, this.descrizioneEsercizio, this.muscoloAllenato, this.numeroSerie, this.numeroRipetizioni, this.imageEsercizio, this.rimuoviSchedaEsercizio);
        this.setSpacing(10);
        this.setPadding(new Insets(10));
    }

    private void onRimuoviEsercizio() throws SQLException {
        String eliminaEsercizio = "DELETE FROM ComposizioneSchedaAllenamento WHERE NomeEsercizio = ? AND IdSchedaAllenamento = ?";

        try (Connection conn = ConnessioneDatabase.getConnection()) {
            PreparedStatement eliminazione = conn.prepareStatement(eliminaEsercizio);
            eliminazione.setString(1, this.nomeEsercizio.getText());
            eliminazione.setInt(2, DatiSessioneCliente.getIdSchedaAllenamento());
            eliminazione.executeUpdate();
        }
    }
}