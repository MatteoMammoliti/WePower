package com.wepower.wepower;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class WePower extends Application
{

    @Override
    public void start(Stage stage) throws Exception {

        // creaimo un oggetto fxml loader per caricare la scena del login in memoria
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);

        stage.setResizable(false); // non permette di ridimensionare la finestra
        stage.setTitle("WePower - Login Utente"); // titolo della finestra
        stage.show();
    }
}