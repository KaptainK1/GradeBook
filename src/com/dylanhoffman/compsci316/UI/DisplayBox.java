package src.com.dylanhoffman.compsci316.UI;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class DisplayBox {

    //stylesheet must be in the format DisplayBox.class.getResource("main_style.css").toExternalForm()
    public void display(String strTitle, String strMessage, String stylesheet){

        Stage window = new Stage();
        window.setTitle(strTitle);
        window.setMinWidth(600);
        window.setMinHeight(400);

        Button closeButton = new Button("OKAY");
        closeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                window.close();
            }
        });

        BorderPane border = new BorderPane();
        VBox vbox = new VBox();
        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setText(strMessage);
        vbox.getChildren().addAll(textArea);
        vbox.getStyleClass().add("vbox");

        border.setCenter(vbox);
        border.setBottom(closeButton);
        Scene scene = new Scene(border);
        scene.getStylesheets().add(stylesheet);
        window.setScene(scene);
        window.show();

    }

    public void displayGridPane(String gradeItems[][], String strTitle, String stylesheet){

        Stage window = new Stage();
        window.setTitle(strTitle);
        window.setMinWidth(600);
        window.setMinHeight(400);


        Button closeButton = new Button("OKAY");
        closeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                window.close();
            }
        });

        //test this
        GridPane gridPane = new GridPane();
        gridPane.setGridLinesVisible(true);
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        for (int column = 0; column < gradeItems.length; column++) {
            for (int row = 0; row < gradeItems[column].length; row++) {
                Label label = new Label(gradeItems[column][row]);
                GridPane.setConstraints(label,column,row);
                gridPane.getChildren().addAll(label);
            }
        }

        Scene scene = new Scene(gridPane, 400,400);
        window.setScene(scene);
        window.show(); // display the stage

    }
}
