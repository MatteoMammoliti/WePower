package com.wepower.wepower.Controllers.Client.ClientViewsController;

import com.wepower.wepower.Models.DatiPalestra.Abbonamento;
import com.wepower.wepower.Views.BannerAbbonamentiInSeleziona;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SchermataSelezioneAbbonamentoController implements Initializable {
    @FXML
    private AnchorPane anchorPaneContenitore;
    @FXML
    private VBox contenitoreAbbonamenti;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        anchorPaneContenitore.getStyleClass().add("anchorPaneContenitore");
        try {
            caricaAbbonamenti();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void caricaAbbonamenti() throws SQLException {
        ArrayList<Abbonamento> abbonamenti= Abbonamento.getAbbonamentiDb();
        for(int i=0;i<abbonamenti.size();i++){
            BannerAbbonamentiInSeleziona nuovo= new BannerAbbonamentiInSeleziona(abbonamenti.get(i).getNomeAbbonamento(),abbonamenti.get(i).getDescrizioneAbbonamento(),abbonamenti.get(i).getCosto(),abbonamenti.get(i).getDurataAbbonamento(),onClickAbbonati(abbonamenti.get(i).getNomeAbbonamento(),abbonamenti.get(i).getCosto()));
            VBox.setMargin(nuovo,new Insets(10,0,10,0));
            contenitoreAbbonamenti.getChildren().add(nuovo);
        }
    }

    public EventHandler<ActionEvent> onClickAbbonati(String nomeAbb,int PrezzoAbb) {
        return e -> {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Client/ClientMenuView/InserimentoDatiPagamento.fxml"));
            Stage stage = new Stage();
            try {
                Scene scene=new Scene(loader.load());
                PagamentoAbbonamentoController controllerPagamentoAbbonamento = loader.getController();
                controllerPagamentoAbbonamento.setNomeEPrezzoAbb(nomeAbb,PrezzoAbb);

                stage.setScene(scene);
                stage.setTitle("Seleziona Abbonamento");
                stage.setResizable(false);
                stage.showAndWait();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

        };
    }

    }

