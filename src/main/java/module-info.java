module com.gauravdahal.ais.r.initial {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;

    opens com.gauravdahal.ais.r.initial to javafx.fxml;
    exports com.gauravdahal.ais.r.initial;
}
