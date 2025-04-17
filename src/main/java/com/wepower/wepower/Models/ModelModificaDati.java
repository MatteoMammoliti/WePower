package com.wepower.wepower.Models;

import com.wepower.wepower.Controllers.Client.ClientViewsController.ProfiloController;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;

public class ModelModificaDati {


    public static boolean onClickModificaDati(int id,String nome,String cogn,String Data,String sesso,String alt,String email,String telefono) throws IOException, SQLException {
        String aggiornaDati="UPDATE Cliente SET Nome=?, Cognome=?, DataNascita=?, Sesso=?, Altezza=? WHERE IdCliente=?";
        String aggiornaDatiCredenziali="UPDATE CredenzialiCliente SET Email=?,Telefono=? WHERE IdCliente=?";
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

                int righeModificate = datiCredenziali.executeUpdate();
                if(righeModificate > 0){
                    System.out.println("Dati modificati con successo");
                    conn.commit();
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

                    return true;
                }else{
                    conn.rollback();
                    System.out.println("Nessun dato modificato");
                    return false;
                }
            }

        }catch (SQLException e){
            System.out.println("Errore nella connessione al database: " + e.getMessage());
        }
        return false;
    }


}
