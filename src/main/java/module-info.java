module com.gauravdahal.ais.r.initial {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires java.sql;
    requires java.desktop;

    opens com.gauravdahal.ais.r.initial to javafx.fxml;
    opens aisr.model to javafx.base;
    
    exports com.gauravdahal.ais.r.initial;
    requires pdfbox;
}
