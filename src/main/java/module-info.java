module com.wepower.wepower {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    //Indicano che il progetto necessita di questi moduli per poter funzionare
    requires de.jensd.fx.glyphs.fontawesome; //Icone di fontawesone
    requires java.sql;
    requires org.xerial.sqlitejdbc;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires java.desktop;
    requires java.management;
    requires org.json;
    requires okhttp3;
    requires annotations;
    requires com.google.gson;

    opens com.wepower.wepower.Controllers to javafx.fxml;
    opens com.wepower.wepower.Controllers.Client to javafx.fxml;
    opens com.wepower.wepower.Controllers.Client.ClientViewsController to javafx.fxml;

    opens com.wepower.wepower.Controllers.Admin to javafx.fxml;
    opens com.wepower.wepower.Models.AdminModel to javafx.base;


    //Facciamo exports per rendere visibili le classi publiche di questi package agli altri moduli
    exports com.wepower.wepower;
    exports com.wepower.wepower.Controllers;
    exports com.wepower.wepower.Controllers.Admin;
    exports com.wepower.wepower.Controllers.Client;
    exports com.wepower.wepower.Models;
    exports com.wepower.wepower.Views;
    exports com.wepower.wepower.Controllers.Client.ClientViewsController;
    exports com.wepower.wepower.Views.SchedaAllenamento;
    exports com.wepower.wepower.Models.SchedaAllenamento;
    exports com.wepower.wepower.Models.DatiPalestra;
    exports com.wepower.wepower.Views.PrenotazioniCorsiSalaPesi;
    exports com.wepower.wepower.Views.ComponentiCalendario;
    opens com.wepower.wepower.Views.AdminView to javafx.base;
}