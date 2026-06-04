package com.safefaces.safefaces.Javafx.View;

import com.safefaces.safefaces.Core.DatabaseConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX application entry point for the SafeFaces system.
 * Responsible for initializing and displaying the login view.
 *
 * This class loads the FXML layout, sets up the main stage,
 * and ensures proper cleanup of resources when the application closes.
 *
 * @author Noor Nabi
 * @author Hamdi Ahmed
 * @author Gyundyuz Sadulov
 */
public class LoginView extends Application {

    /**
     * Starts the JavaFX application by loading the login view
     * and displaying it on the primary stage.
     *
     * @param stage the primary stage provided by JavaFX
     * @throws IOException if the FXML file cannot be loaded
     */
    @Override
    public void start(Stage stage) throws IOException {
        long startTime = System.currentTimeMillis();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/safefaces/safefaces/LoginView.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root, 400, 640);
        stage.setTitle("SafeFaces");
        stage.getIcons().add(new Image(getClass().getResourceAsStream(
                "/com/safefaces/safefaces/images/logo.png")));
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();

        //long elapsed = System.currentTimeMillis() - startTime;

        stage.setOnCloseRequest(e -> {
            DatabaseConnection.closeConnection();
            System.out.println("DB-conncetion closed.");
        });
    }

    /**
     * Main method used to launch the JavaFX application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        launch(args);

        //        @Override
        //        public void stop() {
        //            DatabaseConnection.closeConnection();
        //        }
        //
        //        public static void main (String[]args){
        //            launch();
        //        }
    }
}
