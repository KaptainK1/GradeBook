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

    public static final String DELIMITER = ",";

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
            Course.insertCourse(Integer.parseInt(coursesCourseID.getText().trim()), Integer.parseInt(coursesCourseNum.getText().trim()),
                    coursesCourseName.getText().trim(), listViewGradeModels.getSelectionModel().getSelectedItem().getClass().getSimpleName());
        } catch (NullPointerException e){
            Log.writeToLog(Constants.getLogPath(),e.getMessage());
            clearFieldsOnDataException();
            super.displayAlertBox("Error with Course Data", "Please select a grade model before continuing");
        } catch (InputMismatchException e){
            Log.writeToLog(Constants.getLogPath(),e.getMessage());
            clearFieldsOnDataException();
            super.displayAlertBox("Error with Data input", "Please check the entered data and try again");
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

        ConfirmBox confirmBox = new ConfirmBox();

        try {
            Course course = new Course(Integer.parseInt(coursesCourseID.getText().trim()), Integer.parseInt(coursesCourseNum.getText().trim()),
                    coursesCourseName.getText().trim(), listViewGradeModels.getSelectionModel().getSelectedItem());
            confirmBox.displayConfirmBox("Warning! Data loss possible!", "Are you sure you want to delete this course? All students and grades will be deleted",
                    Constants.getCssPath() );
            if (confirmBox.getIsUserChoice()){
                course.deleteCourse();
            } else{
                DisplayBox displayBox = new DisplayBox();
                displayBox.display("Course Not Deleted!", "You opted to not delete the course", Constants.getCssPath());
            }
        } catch (NullPointerException e){
            clearFieldsOnDataException();
            Log.writeToLog(Constants.getLogPath(),e.getMessage());
            super.displayAlertBox("Error with Course Data", "Please select a grade model before continuing");
        } catch (InputMismatchException e){
            Log.writeToLog(Constants.getLogPath(),e.getMessage());
            clearFieldsOnDataException();
            super.displayAlertBox("Error with Data input", "Please check the entered data and try again");
        } catch (Exception e){
            Log.writeToLog(Constants.getLogPath(),e.getMessage());
            clearFieldsOnDataException();
            super.displayAlertBox("Generic Error with Adding Course", "Please Check the data entered for the course and try again.");
        }

    }

    public void clearFieldsOnDataException(){
        coursesCourseID.setPromptText("Enter valid Course ID");
        coursesCourseNum.setPromptText("Enter valid Course Num");
        coursesCourseName.setPromptText("Enter valid Course Name");
        listViewGradeModels.getSelectionModel().clearSelection();
        coursesCourseID.selectAll();
        coursesCourseID.requestFocus();
    }

    @FXML
    void importObjects(ActionEvent event) throws IOException {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Courses to Import");

        fileChooser.setInitialDirectory(new File("."));

        File file = fileChooser.showOpenDialog(courseContainer.getScene().getWindow());

        processFile(file);



    }

    //method for processing an imported file
    public void processFile(File file) throws IOException {

        //buffer reader object to read the file that is passed in
        BufferedReader bufferedReader = null;

        //string variable to hold the next line to be read by the buffer reader
        String nextLine;

        try {

            bufferedReader = new BufferedReader(new FileReader(file));

            while((nextLine = bufferedReader.readLine()) != null){

                //set the current line to the next line split by the delimiter
                String values[] = nextLine.trim().split(DELIMITER);
                int id;
                int num;
                String name;
                String gradeModel;

                try {
                    //set the id to the first column, need to use substring due to quotes
                    id = Integer.valueOf(values[0].substring(1));

                    //set the num of the course to the 2nd column
                    num = Integer.valueOf(values[1]);

                    //set the name to the 3rd column
                    name = values[2];

                    //set the grademodel to the 4 column, use substring to remove the last quote
                    gradeModel = values[3].substring(0,values[3].length()-1);

                    Course.insertCourse(id,num,name,gradeModel);

                } catch (NumberFormatException e){
                    Log.writeToLog(Constants.getLogPath(),e.getMessage());
                    super.displayAlertBox("Error with Imported Data",
                            e.getMessage() + "\n Is not valid input. Aborting Row import and continuing");
                }
            }

        } catch (IOException | NoSuchElementException e) {
            Log.writeToLog(Constants.getLogPath(),e.getMessage());
            super.displayAlertBox("Error with Importing", e.getMessage());
        } catch (NumberFormatException e){
            Log.writeToLog(Constants.getLogPath(),e.getMessage());
            super.displayAlertBox("Error with Number", "Record Skipped");
        }

        //cleanup the buffer reader obj
        finally {
            try {
                if (bufferedReader != null)
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
    }
}
