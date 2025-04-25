package com.wepower.wepower.Views.SchedaAllenamento;

import com.wepower.wepower.Models.AdminModel.DatiSessioneAdmin;
import com.wepower.wepower.Models.Model;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.Objects;

public class RigaEsercizioSchedaAdmin extends HBox {
    private Label nomeEsercizio;
    private Label numeroSerie;
    private Label numeroRipetizioni;
    private ImageView imageEsercizio;
    private Button rimuoviSchedaEsercizio;

    public RigaEsercizioSchedaAdmin(String nomeEsercizio, String numeroSerie, String numeroRipetizioni, String percorsoImmagine) {
        this.nomeEsercizio = new Label(nomeEsercizio);
        this.numeroSerie = new Label("Numero serie: " + numeroSerie);
        this.numeroRipetizioni = new Label("Numero ripetizioni: " + numeroRipetizioni);

        InputStream is = getClass().getResourceAsStream("/" + percorsoImmagine);
        Image image = new Image(is);
        this.imageEsercizio = new ImageView(image);
        this.imageEsercizio.setFitHeight(150);
        this.imageEsercizio.setFitWidth(150);
        this.imageEsercizio.setPreserveRatio(true);

        this.rimuoviSchedaEsercizio = new Button("Rimuovi esercizio dalla scheda");
        this.rimuoviSchedaEsercizio.setOnAction(event -> {
            try {
                onRimuoviEsercizio();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        VBox sinistra = new VBox();
        sinistra.getChildren().addAll(this.nomeEsercizio, this.numeroSerie, this.numeroRipetizioni);
        sinistra.setSpacing(10);
        sinistra.setPadding(new Insets(10));

        HBox centro = new HBox();
        VBox centroSinistra = new VBox();
        centroSinistra.getChildren().add(this.imageEsercizio);
        centro.getChildren().add(centroSinistra);

        VBox destra = new VBox();
        destra.getChildren().addAll(this.rimuoviSchedaEsercizio);
        destra.setSpacing(10);
        destra.setPadding(new Insets(10));

        this.getChildren().addAll(sinistra, centro, destra);
        this.setSpacing(10);
        this.setPadding(new Insets(10));
    }

    private void onRimuoviEsercizio() throws SQLException {
        DatiSessioneAdmin.eliminaEsercizio(this);
        Model.getInstance().getSchermataCreazioneSchedaAdminController().loadSchedaAllenamento();
        Model.getInstance().getSchermataCreazioneSchedaAdminController().loadEsercizi();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RigaEsercizioSchedaAdmin that = (RigaEsercizioSchedaAdmin) o;
        return Objects.equals(nomeEsercizio.getText(), that.nomeEsercizio.getText());
    }

    @Override
    public int hashCode() {
        return Objects.hash(nomeEsercizio, numeroSerie, numeroRipetizioni, imageEsercizio, rimuoviSchedaEsercizio);
    }

    public Label getNomeEsercizio() {
        return nomeEsercizio;
    }

    public Label getNumeroSerie() {
        return numeroSerie;
    }

    public Label getNumeroRipetizioni() {
        return numeroRipetizioni;
    }
}