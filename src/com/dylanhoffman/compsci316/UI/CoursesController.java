package src.com.dylanhoffman.compsci316.UI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import src.com.dylanhoffman.compsci316.Constants;
import src.com.dylanhoffman.compsci316.logging.Log;
import src.com.dylanhoffman.compsci316.model.Course;
import src.com.dylanhoffman.compsci316.model.file_processing.ProcessCourseFile;
import src.com.dylanhoffman.compsci316.model.grading.CollegeGradeModel;
import src.com.dylanhoffman.compsci316.model.grading.ElementaryGradeModel;
import src.com.dylanhoffman.compsci316.model.grading.GradeModel;
import src.com.dylanhoffman.compsci316.model.grading.HighSchoolGradeModel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class CoursesController extends MainController{

    @FXML
    private ListView<GradeModel> listViewGradeModels;

    @FXML
    private TextField coursesCourseID;

    @FXML
    private TextField coursesCourseNum;

    @FXML
    private TextField coursesCourseName;

    @FXML
    private AnchorPane courseContainer;

    //create 1 of each grade model
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

    @FXML
    void insertCourse(ActionEvent event) {

        try {
            Course course = new Course(Integer.parseInt(coursesCourseID.getText().trim()), Integer.parseInt(coursesCourseNum.getText().trim()),
                    coursesCourseName.getText().trim(), listViewGradeModels.getSelectionModel().getSelectedItem());
            course.insertCourse();
            super.displayDisplayBox("Course Added Successfully!", coursesCourseName.getText().trim() + " Was successfully added!");

        } catch (NullPointerException e) {

            Log.writeToLog(Constants.getLogPath(), e.getMessage());
            clearFieldsOnDataException();
            super.displayAlertBox("Error with Course Data", "Please select a grade model before continuing");

        } catch (IllegalArgumentException e){

            Log.writeToLog(Constants.getLogPath(), e.getMessage());
            clearFieldsOnDataException();
            super.displayAlertBox("Error with Data input", e.getMessage());

        } catch (InputMismatchException e){

            Log.writeToLog(Constants.getLogPath(),e.getMessage());
            clearFieldsOnDataException();
            super.displayAlertBox("Error with Data input", "Please check the entered data and try again");

        } catch (SQLIntegrityConstraintViolationException e) {

            Log.writeToLog(Constants.getLogPath(), e.getMessage());
            clearFieldsOnDataException();
            super.displayAlertBox("Error with Adding Course", "Cannot add a course with the same ID");

        } catch (SQLException e){

            Log.writeToLog(Constants.getLogPath(), e.getMessage());
            clearFieldsOnDataException();
            super.displayAlertBox("Error with Adding Course into Database", "Please check your data and try again");

        } catch (Exception e){

            Log.writeToLog(Constants.getLogPath(),e.getMessage());
            clearFieldsOnDataException();
            super.displayAlertBox("Generic Error with Adding Course", "Please Check the data entered for the course and try again.");
        }

    }

    @FXML
    void searchCourses(ActionEvent event) {
        //2 dimensional array to hold the grade items
        String arrayOfCourses[][];

        //also try to catch a null pointer exception if a course or student is not selected
        try {

            //set the array to the static method of Student to search all grade items for that student and course which is selected

            arrayOfCourses = Course.searchCourses();

            //create a display box object to be shown to the user
            DisplayBox displayBox = new DisplayBox();
            //run the display boxes display grid pane method which will display a grid pane using a 2 dimension array
            displayBox.displayGridPane( arrayOfCourses, "Returning All Courses" ,Constants.getCssPath());
        } catch (NullPointerException e){
            Log.writeToLog(Constants.getLogPath(),e.getMessage());
            super.displayAlertBox("Error While Searching", "A Student and a Course must be selected!");
        }

    }

    @FXML
    void deleteCourse(ActionEvent event) {

        boolean isError = false;
        ConfirmBox confirmBox = new ConfirmBox();

        try {
            Course course = new Course(Integer.parseInt(coursesCourseID.getText().trim()), Integer.parseInt(coursesCourseNum.getText().trim()),
                    coursesCourseName.getText().trim(), listViewGradeModels.getSelectionModel().getSelectedItem());
            confirmBox.displayConfirmBox("Warning! Data loss possible!", "Are you sure you want to delete this course? All students and grades will be deleted",
                    Constants.getCssPath() );
            if (confirmBox.getIsUserChoice()){
                try {
                    course.deleteCourse();
                } catch (SQLException e){
                    isError=true;
                    Log.writeToLog(Constants.getLogPath(), e.getMessage());
                    super.displayAlertBox("Cannot Delete the Course", e.getMessage());
                }

            } else{
                DisplayBox displayBox = new DisplayBox();
                displayBox.display("Course Not Deleted!", "You opted to not delete the course", Constants.getCssPath());
            }
        } catch (NullPointerException e){
            isError=true;
            clearFieldsOnDataException();
            Log.writeToLog(Constants.getLogPath(),e.getMessage());
            super.displayAlertBox("Error with Course Data", "Please select a grade model before continuing");
        } catch (InputMismatchException e){
            isError=true;
            Log.writeToLog(Constants.getLogPath(),e.getMessage());
            clearFieldsOnDataException();
            super.displayAlertBox("Error with Data input", "Please check the entered data and try again");
        } catch (Exception e){
            isError=true;
            Log.writeToLog(Constants.getLogPath(),e.getMessage());
            clearFieldsOnDataException();
            super.displayAlertBox("Generic Error with Adding Course", "Please Check the data entered for the course and try again.");
        }
        if (!isError)
            super.displayDisplayBox("Course Deleted Successfully!", coursesCourseName.getText().trim() + " Was successfully Deleted!");

    }

    public void clearFieldsOnDataException(){

        coursesCourseID.setText("");
        coursesCourseNum.setText("");
        coursesCourseName.setText("");

        coursesCourseID.setPromptText("Enter valid Course ID");
        coursesCourseNum.setPromptText("Enter valid Course Num");
        coursesCourseName.setPromptText("Enter valid Course Name");
        listViewGradeModels.getSelectionModel().clearSelection();
        coursesCourseID.selectAll();
        coursesCourseID.requestFocus();
    }

    @FXML
    public void importObjects(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Courses to Import");

        fileChooser.setInitialDirectory(new File("."));

        File file = fileChooser.showOpenDialog(courseContainer.getScene().getWindow());

        super.importFile(file,new ProcessCourseFile());

    }
}
