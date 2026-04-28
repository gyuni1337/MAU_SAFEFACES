package com.safefaces.safefaces.Javafx.View;

import com.safefaces.safefaces.Backend.DatabaseConnection;
import com.safefaces.safefaces.Javafx.Controller.UserViewController;
import com.safefaces.safefaces.Javafx.Model.Role;
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

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/safefaces/safefaces/ContactView.fxml"));
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/safefaces/safefaces/LoginView.fxml"));
            Parent root = loader.load();
//            UserViewController controller = loader.getController();
//            controller.setUser(
//                    new User(1, "Henry", "oldmanexample.jpg", "henry1", "password", Role.USER)
//            );


           // var url = getClass().getResource("/com/safefaces/safefaces/main-view.fxml");
           // FXMLLoader fxmlLoader = new FXMLLoader(url);


            Scene scene = new Scene(root, 400, 640);
            Scene scene = new Scene(root, 400, 700);
            stage.setTitle("SafeFaces");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        }

        public static void main (String[]args){launch();
    @Override
    public void stop() {
        DatabaseConnection.closeConnection();
    }

        public static void main (String[]args){
            launch();
        }
}
