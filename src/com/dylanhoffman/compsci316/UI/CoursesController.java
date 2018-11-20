package src.com.dylanhoffman.compsci316.UI;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import src.com.dylanhoffman.compsci316.model.grading.CollegeGradeModel;
import src.com.dylanhoffman.compsci316.model.grading.ElementaryGradeModel;
import src.com.dylanhoffman.compsci316.model.grading.GradeModel;
import src.com.dylanhoffman.compsci316.model.grading.HighSchoolGradeModel;

public class CoursesController {

    @FXML
    private ListView<GradeModel> listViewGradeModels;

    @FXML
    private TextField coursesCourseID;

    @FXML
    private TextField coursesCourseNum;

    @FXML
    private TextField coursesCourseName;

    private ElementaryGradeModel elementaryGradeModel = new ElementaryGradeModel();
    private HighSchoolGradeModel highSchoolGradeModel = new HighSchoolGradeModel();
    private CollegeGradeModel collegeGradeModel = new CollegeGradeModel();

    @FXML
    public void initialize(){

        //initialize the list view with the 3 grade models
        listViewGradeModels.getItems().addAll(elementaryGradeModel,highSchoolGradeModel,collegeGradeModel);
        //ensure the selection mode is single (cannot select more than 1 grademodel!
        listViewGradeModels.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        listViewGradeModels.refresh();
    }

}
