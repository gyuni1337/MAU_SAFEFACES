package com.safefaces.safefaces.Javafx.View;


import com.safefaces.safefaces.Javafx.Controller.UserViewController;
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
            Parent root = loader.load();
//            controller.setUser(
//                    new User(1, "Henry", "oldmanexample.jpg", "henry1", "password", Role.USER)
//            );


           // var url = getClass().getResource("/com/safefaces/safefaces/main-view.fxml");
           // FXMLLoader fxmlLoader = new FXMLLoader(url);
            Scene scene = new Scene(root, 400, 700);
            stage.setTitle("SafeFaces");
            stage.setScene(scene);
            stage.show();
        }

        public static void main (String[]args){launch();
        }
}
