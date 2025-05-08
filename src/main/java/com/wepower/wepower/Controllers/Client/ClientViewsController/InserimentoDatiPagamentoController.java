package com.wepower.wepower.Controllers.Client.ClientViewsController;

import com.wepower.wepower.Models.ConnessioneDatabase;
import com.wepower.wepower.Models.DatiSessioneCliente;
import com.wepower.wepower.Models.Model;
import com.wepower.wepower.Models.ModelValidazione;
import com.wepower.wepower.Views.AlertHelper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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
    @FXML private Label labelNomeAbbonamento;
    @FXML private  Label labelPrezzoAbbonamento;
    @FXML private TextField textFieldProprietarioCarta;
    @FXML private TextField textFieldNumeroCarta;
    @FXML private TextField textFieldDataScadenzacarta;
    @FXML private TextField textFieldCvc;
    @FXML private Button btnPaga;

    public InserimentoDatiPagamentoController() {}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnPaga.setOnAction(e -> {
            try {
                onClickPaga();
            } catch (SQLException ex) {
                AlertHelper.showAlert("Questo non doveva succedere", "Qualcosa è andato storto durante il pagamento", null, Alert.AlertType.ERROR);
            }
        });
    }

    public void setFinestraPrecedente(Stage finestraPrecedente) {this.finestraPrecedente = finestraPrecedente;}

    public void setNomeEPrezzoAbb(String nomeAbbonamento,double PrezzoAbbonamento) {
        labelNomeAbbonamento.setText("Abbonamento: " + nomeAbbonamento);
        labelPrezzoAbbonamento.setText("Prezzo: "+ PrezzoAbbonamento+"€");
    }

    public void onClickPaga() throws SQLException {
        Connection conn = ConnessioneDatabase.getConnection();

        String proprietarioCarta = textFieldProprietarioCarta.getText();
        String numeroCarta = textFieldNumeroCarta.getText().replaceAll("[\\s-]", "");

        String dataScadenza = textFieldDataScadenzacarta.getText();
        String cvc = textFieldCvc.getText();
        int idAbbonamento=-1;
        int durataAbb=-1;
        LocalDate data=LocalDate.now();

        if(dataScadenza.isEmpty() || cvc.isEmpty() || proprietarioCarta.isEmpty() || numeroCarta.isEmpty()){
            AlertHelper.showAlert("Attenzione", "Compila tutti i campi", null, Alert.AlertType.ERROR);
            return;
        }
        if(!ModelValidazione.controlloDataScadenzacarta(dataScadenza)){
            AlertHelper.showAlert("Attenzione", "Data scadenza non valida(mese,anno)", null, Alert.AlertType.ERROR);
            return;
        }
        if(!ModelValidazione.controlloNumeroCVC(cvc)){
            AlertHelper.showAlert("Attenzione", "Numero cvc non valido", null, Alert.AlertType.ERROR);
            return;
        }
        if(!ModelValidazione.controlloNumeroCarta(numeroCarta)){
            AlertHelper.showAlert("Attenzione", "Numero carta non valido", null, Alert.AlertType.ERROR);
            return;
        }
        if(!ModelValidazione.controllonomeCognome(proprietarioCarta)){
            AlertHelper.showAlert("Attenzione", "Proprietario carta non valido,inserire nome completo", null, Alert.AlertType.ERROR);
            return;
        }

        String controlloBonamentoGiàEsistente="SELECT * from AbbonamentoCliente WHERE StatoAbbonamento=1 and IdCliente=?";
        String cercoIdAbbonamento="SELECT IdTipoAbbonamento,Durata from TipoAbbonamento WHERE NomeAbbonamento=?";
        String aggiungoAbbonamento="INSERT INTO AbbonamentoCliente (IdCliente,IdTipoAbbonamento,DataInizioAbbonamento,DataFineAbbonamento,StatoAbbonamento) VALUES (?,?,?,?,?)";

        PreparedStatement caricoDati = null;
        ResultSet risultatoPrimaQuery = null;
        try {
            conn.setAutoCommit(false);
            caricoDati= conn.prepareStatement(controlloBonamentoGiàEsistente);
            caricoDati.setInt(1,DatiSessioneCliente.getIdUtente());
            risultatoPrimaQuery = caricoDati.executeQuery();

            if(risultatoPrimaQuery.next()){
                AlertHelper.showAlert("Attenzione", "Hai già un abbonamento attivo", null, Alert.AlertType.ERROR);
                conn.rollback();
                return;
            }

            PreparedStatement abbonamento = null;
            ResultSet risultatoSecondaQuery = null;
            try {
                abbonamento=conn.prepareStatement(cercoIdAbbonamento);
                String testoLabel = labelNomeAbbonamento.getText();
                String nomeAbbonamento = testoLabel.substring(testoLabel.indexOf(':') + 1).trim();
                abbonamento.setString(1,nomeAbbonamento);

                risultatoSecondaQuery = abbonamento.executeQuery();

                if(risultatoSecondaQuery.next()){
                    idAbbonamento= risultatoSecondaQuery.getInt("IdTipoAbbonamento");
                    durataAbb= risultatoSecondaQuery.getInt("Durata");
                }

                if (durataAbb <= 0) {// sicurezza extra
                    AlertHelper.showAlert("Attenzione", "Durata abbonamento non valida (" + durataAbb + ")", null, Alert.AlertType.ERROR);
                    conn.rollback();
                    return;
                }
            } catch (SQLException e){
                conn.rollback();
                AlertHelper.showAlert("Questo non doveva succedere", "Qualcosa è andato storto", null, Alert.AlertType.ERROR);
                return;
            } finally {
                if(abbonamento != null) {
                    try { abbonamento.close(); } catch (SQLException ignored) {}
                }

                if(risultatoSecondaQuery != null) {
                    try { risultatoSecondaQuery.close(); } catch (SQLException ignored) {}
                }
            }

            PreparedStatement caricoAbbonamento = null;
            try {
                caricoAbbonamento=conn.prepareStatement(aggiungoAbbonamento);
                caricoAbbonamento.setInt(1,DatiSessioneCliente.getIdUtente());
                caricoAbbonamento.setInt(2,idAbbonamento);
                caricoAbbonamento.setString(3, LocalDate.now().toString());
                caricoAbbonamento.setString(4,data.plusMonths(durataAbb).toString());
                caricoAbbonamento.setInt(5,1);

                int aggiunta=caricoAbbonamento.executeUpdate();

                if(aggiunta>0){
                    AlertHelper.showAlert("Attenzione", "Pagamento avvenuto con successo", null, Alert.AlertType.INFORMATION);
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
                    AlertHelper.showAlert("Attenzione", "Errore nell'attivazione dell'abbonamento", null, Alert.AlertType.ERROR);
                    conn.rollback();
                }
            } catch (SQLException e) {
                conn.rollback();
                AlertHelper.showAlert("Questo non doveva succedere", "Qualcosa è andato storto nel caricamento dell'abbonamento", null, Alert.AlertType.ERROR);
            } finally {
                if(caricoAbbonamento != null) {
                    try { caricoAbbonamento.close(); } catch (SQLException ignored) {}
                }
            }

        } catch (SQLException e) {
            AlertHelper.showAlert("Questo non doveva succedere", "Errore durante il pagamento", null, Alert.AlertType.ERROR);
        } finally {
            if(caricoDati != null) {
                try { caricoDati.close(); } catch (SQLException ignored) {}
            }

            if(risultatoPrimaQuery != null) {
                try { risultatoPrimaQuery.close(); } catch (SQLException ignored) {}
            }
        }
    }
}