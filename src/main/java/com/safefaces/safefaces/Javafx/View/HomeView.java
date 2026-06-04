package com.safefaces.safefaces.Javafx.View;
 import javafx.fxml.FXMLLoader;
 import javafx.scene.Parent;
 import java.io.IOException;
public class HomeView {
    public Parent getView() throws IOException{
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/safefaces/safefaces/HomeView.fxml"));
        return loader.load();
    }
}
