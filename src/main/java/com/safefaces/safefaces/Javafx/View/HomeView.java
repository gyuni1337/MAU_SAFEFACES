package com.safefaces.safefaces.Javafx.View;
 import javafx.fxml.FXMLLoader;
 import javafx.scene.Parent;
 import java.io.IOException;

/**
 * View class responsible for loading the Home view from an FXML file.
 *
 * This class encapsulates the logic for creating and returning
 * the JavaFX UI component representing the home screen.
 *
 * @author Hamdi Ahmed
 * @author Gyundyuz Sadulov
 */
public class HomeView {

    /**
     * Loads and returns the Home view from the FXML file.
     *
     * @return the root {@link Parent} node of the loaded view
     * @throws IOException if the FXML file cannot be loaded
     */
    public Parent getView() throws IOException{
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/safefaces/safefaces/HomeView.fxml"));
        return loader.load();
    }
}
