package com.wepower.wepower.Controllers.Client.ClientViewsController;
import com.wepower.wepower.ControlloTemi;
import com.wepower.wepower.Models.DatiPalestra.Abbonamento;
import com.wepower.wepower.Views.AlertHelper;
import com.wepower.wepower.Views.Bannerini.BannerAbbonamentiInSeleziona;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SchermataSelezioneAbbonamentoController implements Initializable {
    private static SchermataSelezioneAbbonamentoController instance;

    private Stage stage;
    @FXML private AnchorPane anchorPaneContenitore;
    @FXML private VBox contenitoreAbbonamenti;

    public SchermataSelezioneAbbonamentoController() {}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instance=this;
        anchorPaneContenitore.getStyleClass().add("anchorPaneContenitore");
        try {
            caricaAbbonamenti();
        } catch (SQLException e) {
            AlertHelper.showAlert("Questo non doveva succedere", "Errore durante il caricamento degli abbonamenti", null, Alert.AlertType.ERROR);
        }
    }

    public static SchermataSelezioneAbbonamentoController getInstance() {return instance;}

    public void setStage(Stage stage){ this.stage=stage; }

    public void caricaAbbonamenti() throws SQLException {
        ArrayList<Abbonamento> abbonamenti= Abbonamento.getAbbonamentiDb();

        for (Abbonamento abbonamento : abbonamenti) {
            BannerAbbonamentiInSeleziona nuovo = new BannerAbbonamentiInSeleziona(abbonamento.getNomeAbbonamento(), abbonamento.getDescrizioneAbbonamento(), abbonamento.getCosto(), abbonamento.getDurataAbbonamento(), onClickAbbonati(abbonamento.getNomeAbbonamento(), abbonamento.getCosto()));
            VBox.setMargin(nuovo, new Insets(10, 0, 10, 0));
            contenitoreAbbonamenti.getChildren().add(nuovo);
        }
    }

    public EventHandler<ActionEvent> onClickAbbonati(String nomeAbb,double PrezzoAbb) {
        return e -> {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Client/ClientMenuView/InserimentoDatiPagamento.fxml"));

            try {
                Scene scena=new Scene(loader.load());
                InserimentoDatiPagamentoController controller = loader.getController();
                controller.setNomeEPrezzoAbb(nomeAbb,PrezzoAbb);

                String cssTema=getClass().getResource("/Styles/inserimentoDatiPagamento.css").toExternalForm();
                ControlloTemi.getInstance().aggiungiScena(scena,cssTema);
                //Passo il riferimento alla finestra corrente(In modo da chiuderla dopo il "pagamento")
                controller.setFinestraPrecedente(stage);

                Stage stage = new Stage();
                stage.setScene(scena);
                stage.setTitle("Pagamento abbonamento");
                stage.setResizable(false);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();
            } catch (IOException ex) {
                AlertHelper.showAlert("Questo non doveva succedere", "Errore durante l'apertura della schermata", null, Alert.AlertType.ERROR);
            }
        };
    }
}