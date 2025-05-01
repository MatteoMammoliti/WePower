package com.wepower.wepower.Controllers.Client.ClientViewsController;

import com.wepower.wepower.Models.ConnessioneDatabase;
import com.wepower.wepower.Models.DatiSessioneCliente;
import com.wepower.wepower.Models.Model;
import com.wepower.wepower.Models.ModelValidazione;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class InserimentoDatiPagamentoController implements Initializable {
    private Stage finestraPrecedente;
    @FXML
    private Label labelNomeAbbonamento;
    @FXML
    private  Label labelPrezzoAbbonamento;
    @FXML
    private Label labelProprietarioCarta;
    @FXML
    private TextField textFieldProprietarioCarta;
    @FXML
    private Label labelNumeroCarta;
    @FXML
    private TextField textFieldNumeroCarta;
    @FXML
    private Label labelDataScadenzaCarta;
    @FXML
    private TextField textFieldDataScadenzacarta;
    @FXML
    private Label labelCvc;
    @FXML
    private TextField textFieldCvc;
    @FXML
    private Button btnPaga;
    @FXML
    private AnchorPane contenitoreDatiPagamento;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnPaga.setOnAction(e -> {
            try {
                onClickPaga();
            } catch (SQLException ex) {
                throw new RuntimeException(ex); // gestione non necessaria
            }
        });
    }

    public void setFinestraPrecedente(Stage finestraPrecedente) {this.finestraPrecedente = finestraPrecedente;}


    public void setNomeEPrezzoAbb(String nomeAbbonamento,double PrezzoAbbonamento) {
        labelNomeAbbonamento.setText("Abbonamento: " + nomeAbbonamento);
        labelPrezzoAbbonamento.setText("Prezzo: "+ PrezzoAbbonamento+"€");
    }

    public void onClickPaga() throws SQLException {
        String proprietarioCarta = textFieldProprietarioCarta.getText();
        String numeroCarta = textFieldNumeroCarta.getText().replaceAll("[\\s-]", "");
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        ImageView icon = new ImageView(new Image(getClass().getResourceAsStream("/Images/IconeAlert/error.png")));
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/Styles/alertStyle.css").toExternalForm());
        alert.setGraphic(icon);

        String dataScadenza = textFieldDataScadenzacarta.getText();
        String cvc = textFieldCvc.getText();
        int idAbbonamento;
        int durataAbb;
        LocalDate data=LocalDate.now();

        if(dataScadenza.equals("") || cvc.equals("") || proprietarioCarta.equals("") || numeroCarta.equals("")){
            alert.setContentText("Compila tutti i campi");
            alert.setTitle("Attenzione");
            alert.showAndWait();
            return;
        }
        if(!ModelValidazione.controlloDataScadenzacarta(dataScadenza)){
            alert.setContentText("Data scadenza non valida(Mese,anno)");
            alert.setTitle("Attenzione");
            alert.showAndWait();
            return;
        }
        if(!ModelValidazione.controlloNumeroCVC(cvc)){
            alert.setContentText("Numero cvc non valida");
            alert.setTitle("Attenzione");
            alert.showAndWait();
            return;
        }
        if(!ModelValidazione.controlloNumeroCarta(numeroCarta)){
            alert.setContentText("Numero carta non valida");
            alert.setTitle("Attenzione");
            alert.showAndWait();
            return;
        }
        if(!ModelValidazione.controllonomeCognome(proprietarioCarta)){
            alert.setContentText("Proprietario carta non valido,inserire nome completo");
            alert.setTitle("Attenzione");
            alert.showAndWait();
            return;
        }

        String controlloBonamentoGiàEsistente="SELECT * from AbbonamentoCliente WHERE StatoAbbonamento=1 and IdCliente=?";
        String cercoIdAbbonamento="SELECT IdTipoAbbonamento,Durata from TipoAbbonamento WHERE NomeAbbonamento=?";
        String aggiungoAbbonamento="INSERT INTO AbbonamentoCliente (IdCliente,IdTipoAbbonamento,DataInizioAbbonamento,DataFineAbbonamento,StatoAbbonamento) VALUES (?,?,?,?,?)";
        try(Connection conn= ConnessioneDatabase.getConnection()){
            conn.setAutoCommit(false);
            try (PreparedStatement caricoDati= conn.prepareStatement(controlloBonamentoGiàEsistente)){
                caricoDati.setInt(1,DatiSessioneCliente.getIdUtente());
                ResultSet risultato= caricoDati.executeQuery();
                if(risultato.next()){
                    alert.setContentText("Hai già un abbonamento attivo");
                    alert.setTitle("Attenzione");
                    alert.showAndWait();
                    conn.rollback();
                    return;
                }
            }
            try(PreparedStatement abbonamento=conn.prepareStatement(cercoIdAbbonamento)){
                abbonamento.setString(1,labelNomeAbbonamento.getText());
                ResultSet risultato= abbonamento.executeQuery();
                idAbbonamento=risultato.getInt("IdTipoAbbonamento");
                durataAbb=risultato.getInt("Durata");
            } catch (SQLException e){
                conn.rollback();
                return;
            }
            try (PreparedStatement caricoAbbonamento=conn.prepareStatement(aggiungoAbbonamento)){
                caricoAbbonamento.setInt(1,DatiSessioneCliente.getIdUtente());
                caricoAbbonamento.setInt(2,idAbbonamento);
                caricoAbbonamento.setString(3, LocalDate.now().toString());
                caricoAbbonamento.setString(4,data.plusMonths(durataAbb).toString());
                caricoAbbonamento.setInt(5,1);
                int aggiunta=caricoAbbonamento.executeUpdate();
                if(aggiunta>0){
                    alert.setContentText("Aggiunta con successo");
                    alert.setTitle("Attenzione");
                    alert.showAndWait();
                    conn.commit();
                    Stage stage=(Stage) btnPaga.getScene().getWindow();
                    DatiSessioneCliente.setStatoAbbonamento(true);
                    if(Model.getInstance().getProfiloController()!=null){
                        Model.getInstance().getProfiloController().caricaInterfacciaDatiUtente();
                    }
                    Model.getInstance().getClientMenuController().caricaMenu();
                    stage.close();
                    if(finestraPrecedente!=null){finestraPrecedente.close();}

                }
                else{
                    alert.setContentText("Errore nell'attivazione dell'abbonamento");
                    alert.setTitle("Attenzione");
                    alert.showAndWait();
                    conn.rollback();
                }
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }
}