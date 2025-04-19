package com.wepower.wepower.Models;

import com.wepower.wepower.Controllers.Client.ClientDashboardController;
import com.wepower.wepower.Controllers.Client.ClientViewsController.ProfiloController;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

public class ModelModificaDati {


    public static boolean onClickModificaDati(int id,String nome,String cogn,String Data,String sesso,String alt,String email,String telefono,Integer peso) throws IOException, SQLException {
        String aggiornaDati="UPDATE Cliente SET Nome=?, Cognome=?, DataNascita=?, Sesso=?, Altezza=? WHERE IdCliente=?";
        String aggiornaDatiCredenziali="UPDATE CredenzialiCliente SET Email=?,Telefono=? WHERE IdCliente=?";
        String aggiornaPeso="INSERT INTO PesoCliente (IdCliente,Peso,DataRegistrazionePeso) VALUES (?,?,?)";
        try(Connection conn=ConnessioneDatabase.getConnection()){
            conn.setAutoCommit(false);
            try(PreparedStatement datiCliente = conn.prepareStatement(aggiornaDati)){
                datiCliente.setString(1,nome);
                datiCliente.setString(2,cogn);
                datiCliente.setString(3,Data);
                if(sesso!=null){
                    datiCliente.setString(4,sesso);}
                else{
                    datiCliente.setNull(4, java.sql.Types.VARCHAR);
                }

               if(alt!=null){
                   datiCliente.setString(5,alt);
               }else{
                   datiCliente.setNull(5, java.sql.Types.VARCHAR);
               }
                datiCliente.setInt(6,id);

                int righeModificate = datiCliente.executeUpdate();
                if(righeModificate > 0){
                    System.out.println("Dati modificati con successo");
                }else{
                    conn.rollback();
                    System.out.println("Nessun dato modificato");
                    return false;
                }

            }try(PreparedStatement datiCredenziali=conn.prepareStatement(aggiornaDatiCredenziali)){
                datiCredenziali.setString(1,email);
                datiCredenziali.setString(2,telefono);
                datiCredenziali.setInt(3,id);

                if(datiCredenziali.executeUpdate() == 0){
                    conn.commit();
                    return false;}
                if(peso != null && peso!=DatiSessioneCliente.getPesoAttuale()){
                    try (PreparedStatement pesoCredenziali = conn.prepareStatement(aggiornaPeso)){
                        pesoCredenziali.setInt(1, id);
                        pesoCredenziali.setInt(2, peso);
                        pesoCredenziali.setString(3, LocalDate.now().toString());
                        if (pesoCredenziali.executeUpdate() == 0) {
                            conn.rollback();
                            return false;
                        }
                    }
                }

                    if(!nome.equals(DatiSessioneCliente.getNomeUtente())){
                        DatiSessioneCliente.setNomeUtente(nome);
                    }
                    if(!cogn.equals(DatiSessioneCliente.getCognome())){
                        DatiSessioneCliente.setCognome(cogn);
                    }
                    if(!Data.equals(DatiSessioneCliente.getDataNascita())){
                        DatiSessioneCliente.setDataNascita(Data);
                    }
                    if(!email.equals(DatiSessioneCliente.getEmail())){
                        DatiSessioneCliente.setEmail(email);
                    }
                    if (!Objects.equals(sesso, DatiSessioneCliente.getGenere())) {
                        DatiSessioneCliente.setGenere(sesso);
                    }
                    if (!Objects.equals(alt, DatiSessioneCliente.getAltezza())) {
                        DatiSessioneCliente.setAltezza(alt);
                    }
                    if (!Objects.equals(telefono, DatiSessioneCliente.getTelefono())) {
                        DatiSessioneCliente.setTelefono(telefono);
                    }
                    if(peso!=null && peso!=DatiSessioneCliente.getPesoAttuale()){
                        DatiSessioneCliente.setPesoAttuale(peso);
                        ClientDashboardController.getInstance().loadGraficoPeso();
                    }
                    conn.commit();
                    return true;
            }

        }catch (SQLException e){
            System.out.println("Errore nella connessione al database: " + e.getMessage());
        }
        return false;
    }


}
