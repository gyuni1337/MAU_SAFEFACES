module com.safefaces.safefaces {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;
    requires java.sql;

    requires org.postgresql.jdbc;

    opens com.safefaces.safefaces to javafx.fxml;
    exports com.safefaces.safefaces.Javafx.Model;
    opens com.safefaces.safefaces.Javafx.Model to javafx.fxml;
    exports com.safefaces.safefaces.Javafx.Controller;
    opens com.safefaces.safefaces.Javafx.Controller to javafx.fxml;
    exports com.safefaces.safefaces.Javafx.View;
    opens com.safefaces.safefaces.Javafx.View to javafx.fxml;
    exports com.safefaces.safefaces.Javafx.App;
    opens com.safefaces.safefaces.Javafx.App to javafx.fxml;
    exports com.safefaces.safefaces.Backend;
    opens com.safefaces.safefaces.Backend to javafx.fxml;

    //kopplat till repositories and databas
    exports com.safefaces.safefaces.Backend.Repository;
    opens com.safefaces.safefaces.Backend.Repository to javafx.fxml;
    exports com.safefaces.safefaces.Backend.Service;
    opens com.safefaces.safefaces.Backend.Service to javafx.fxml;
}