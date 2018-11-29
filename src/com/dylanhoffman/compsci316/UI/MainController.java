package src.com.dylanhoffman.compsci316.UI;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import src.com.dylanhoffman.compsci316.Constants;


public class MainController {

    @FXML
    private CoursesController coursesController;

    @FXML
    private GradeItemsController gradeItemsController;

    @FXML
    private StudentsController studentsController;

    @FXML
    public AnchorPane mainContainer;

    @FXML
    public void initialize(){

    }

    /**
     * public method to display an alert box when an exception is thrown
     * @param title title of the alert box
     * @param message message to be displayed
     */
    public void displayAlertBox(String title, String message){
        AlertBox alertBox = new AlertBox();
        alertBox.display(title, message, Constants.getCssPath());
    }


}
