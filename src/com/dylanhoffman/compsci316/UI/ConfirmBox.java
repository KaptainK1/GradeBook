package src.com.dylanhoffman.compsci316.UI;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ConfirmBox {
    private Button yesButton = new Button("YES");
    private Button noButton = new Button("NO");
    private boolean userChoice;

    public Boolean displayConfirmBox(String title, String message, String stylesheet){

        Stage window = new Stage();
        window.setTitle(title);
        window.setMinWidth(200);
        window.setMinHeight(200);

        yesButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                setUserChoice(true);
                window.close();
            }
        });

        noButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                setUserChoice(false);
                window.close();
            }
        });
        Label label = new Label(message);
        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(label,yesButton,noButton);
        Scene scene = new Scene(vbox);
        scene.getStylesheets().add(stylesheet);
        window.setScene(scene);
        window.showAndWait();

        return getIsUserChoice();
    }

    public boolean getIsUserChoice() {
        return userChoice;
    }

    public void setUserChoice(boolean userChoice) {
        this.userChoice = userChoice;
    }
}
