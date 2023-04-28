module pl.kompo.view {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires pl.kompo.model;
    requires java.annotation;
    requires org.slf4j;

    opens pl.kompo.view to javafx.fxml;
    exports pl.kompo.view;
}