package com.safefaces.safefaces.Javafx.View;

import com.safefaces.safefaces.Core.DatabaseConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class LoginView extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/safefaces/safefaces/LoginView.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root, 400, 640);
        stage.setTitle("SafeFaces");
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                "/com/safefaces/safefaces/images/logo.png"))));
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest(e -> {
            DatabaseConnection.closeConnection();
            System.out.println("DB-conncetion closed.");
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
