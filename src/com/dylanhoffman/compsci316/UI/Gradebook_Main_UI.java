package src.com.dylanhoffman.compsci316.UI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Gradebook_Main_UI extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root =
                FXMLLoader.load(getClass().getResource("Main.fxml"));

        Scene scene = new Scene(root); // attach scene graph to scene

        stage.setTitle("Gradebook Application"); // displayed in window's title bar
        stage.setScene(scene); // attach scene to stage
        stage.show(); // display the stage
    }

    public static void main(String[] args) {
        // create a Gradebook object and call its start method
        launch(args);
    }


}
