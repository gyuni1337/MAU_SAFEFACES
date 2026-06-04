module com.safefaces.safefaces {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome5;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;
    requires java.sql;
    requires java.management;
    requires  javafx.media;

    requires org.postgresql.jdbc;
    requires jbcrypt;

    opens com.safefaces.safefaces to javafx.fxml;
    exports com.safefaces.safefaces.Javafx.Model;
    opens com.safefaces.safefaces.Javafx.Model to javafx.fxml;
    exports com.safefaces.safefaces.Javafx.Controller;
    opens com.safefaces.safefaces.Javafx.Controller to javafx.fxml;
    exports com.safefaces.safefaces.Javafx.View;
    opens com.safefaces.safefaces.Javafx.View to javafx.fxml;
    exports com.safefaces.safefaces.Javafx.App;
    opens com.safefaces.safefaces.Javafx.App to javafx.fxml;
    exports com.safefaces.safefaces.Core;
    opens com.safefaces.safefaces.Core to javafx.fxml;

    exports com.safefaces.safefaces.Core.Model;
    opens com.safefaces.safefaces.Core.Model to javafx.fxml;
    exports com.safefaces.safefaces.Core.Repository;
    opens com.safefaces.safefaces.Core.Repository to javafx.fxml;
    exports com.safefaces.safefaces.Core.Service;
    opens com.safefaces.safefaces.Core.Service to javafx.fxml;
}