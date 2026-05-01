package com.safefaces.safefaces.Javafx.View;

import com.safefaces.safefaces.Backend.DatabaseConnection;
import com.safefaces.safefaces.Javafx.Controller.UserViewController;
//import com.safefaces.safefaces.Javafx.Model.Role;
import com.safefaces.safefaces.Javafx.Model.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import static javafx.application.Application.launch;



public class MainView extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        long startTime = System.currentTimeMillis();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/safefaces/safefaces/LoginView.fxml"));
//        FXMLLoader loader = new FXMLLoader(
//                getClass().getResource("/com/safefaces/safefaces/LoginView.fxml"));
        Parent root = loader.load();
//            UserViewController controller = loader.getController();
//            controller.setUser(
//                    new User(1, "Henry", "oldmanexample.jpg", "henry1", "password", Role.USER)
//            );


        // var url = getClass().getResource("/com/safefaces/safefaces/main-view.fxml");
        // FXMLLoader fxmlLoader = new FXMLLoader(url);


        Scene scene = new Scene(root, 400, 640);
        stage.setTitle("SafeFaces");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();

        long elapsed = System.currentTimeMillis() - startTime;

        stage.setOnCloseRequest(e -> {
            DatabaseConnection.closeConnection();
            System.out.println("DB-conncetion closed.");
        });
    }

    public static void main(String[] args) {
        launch();

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
