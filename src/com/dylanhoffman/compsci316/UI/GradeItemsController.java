package src.com.dylanhoffman.compsci316.UI;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import src.com.dylanhoffman.compsci316.logging.Log;
import src.com.dylanhoffman.compsci316.model.Course;
import src.com.dylanhoffman.compsci316.model.GradeItem;
import src.com.dylanhoffman.compsci316.model.Student;
import src.com.dylanhoffman.compsci316.utility.SelectStudentGrades;
import java.util.ArrayList;

/**
 * Class to control the Grade Item UI
 */
public class GradeItemsController extends MainController{


    //variables to interact with javaFX elements
    @FXML
    private TextField gradeItemName;

    @FXML
    private TextField gradeItemPointsCorrect;

    @FXML
    private TextField totalPoints;

    @FXML
    private ListView<Course> courseListView;

    @FXML
    private ListView<Student> studentsListView;


    //private observable list to hold courses and students
    private ObservableList<Course> courses = FXCollections.observableArrayList();
    private ObservableList<Student> students = FXCollections.observableArrayList();

    //private array list to hold all students and all courses
    private ArrayList<Student> allStudents = new ArrayList<>();
    private ArrayList<Course> allCourses = new ArrayList<>();


    /**
     * method to initialize the UI
     * initialize method to first populate the course list
     * then populate the student list based on the course selected
     */

    public void initialize(){
        initializeCourseList();

        //use a listener for when the course selected item changes
        //then populate the student list
        courseListView.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Course>() {
                    @Override
                    public void changed(ObservableValue<? extends Course> observable, Course oldValue, Course newValue) {
                        //populate the student list based on the new value selected
                        try {
                            initializeStudentList(newValue);
                        } catch (NullPointerException e){
                            displayAlertBox("A course was unselected!", "The Student List is now clear");
                            allStudents.clear();
                            students.removeAll();
                            studentsListView.getItems().clear();
                        }

                    }
                }
        );
    }

    /**
     * method to insert a grade item
     * @param event
     */
    @FXML
    void insertGradeItem(ActionEvent event) {

        //variables to hold user input data

        int pointsCorrect = 0;
        int pointsPossible = 0;
        String name = "";

        //try to catch a number format exception with the user data
        try {

            pointsCorrect  = Integer.parseInt(gradeItemPointsCorrect.getText());
            pointsPossible = Integer.parseInt(totalPoints.getText());
            name = gradeItemName.getText().trim().toUpperCase();


        } catch (NumberFormatException e){
            clearFieldsOnDataException();
        }

        // try to create a grade item object and insert it via the static student method insert grade item
        //also try to catch a null pointer exception if a course or student is not selected
        //also try to catch an illegal argument exception thrown when the object is created
        //finally default catch all exception
        try {
            GradeItem gradeItem = new GradeItem(name,pointsCorrect,pointsPossible);
            Student.insertGradeItem(studentsListView.getSelectionModel().getSelectedItem(),gradeItem,courseListView.getSelectionModel().getSelectedItem());
        } catch (NullPointerException e) {
            super.displayAlertBox("Error - Student or Course Not Selected", "A Course and a Student must be selected!!");
        } catch (IllegalArgumentException e){
            super.displayAlertBox("Error with GradeItem Data", e.getMessage());
        } catch (Exception e){
            super.displayAlertBox("General Error", "An error has occurred, please check the data and try again!");
        }
    }

    /**
     * method to search for grade items
     * @param event
     */
    @FXML
    void searchGradeItem(ActionEvent event) {

        //2 dimensional array to hold the grade items
        String arrayGradeItems[][];

        //also try to catch a null pointer exception if a course or student is not selected
        try {

            //set the array to the static method of Student to search all grade items for that student and course which is selected

            arrayGradeItems = Student.searchGradeItem(studentsListView.getSelectionModel().getSelectedItem(),courseListView.getSelectionModel().getSelectedItem());

            //create a display box object to be shown to the user
            DisplayBox displayBox = new DisplayBox();
            //run the display boxes display grid pane method which will display a grid pane using a 2 dimension array
            displayBox.displayGridPane( arrayGradeItems, "GradeItems For " + studentsListView.getSelectionModel().getSelectedItem(),"src/com/dylanhoffman/compsci316/UI/stylesheet.css");
        } catch (NullPointerException e){
            super.displayAlertBox("Error While Searching", "A Student and a Course must be selected!");
        }

    }

    /**
     * method to delete a grade item
     * @param event the action event
     */
    @FXML
    void deleteGradeItem(ActionEvent event) {

        //variables to hold the input data
        int totalCorrect;
        int totalPossible;
        String name;

        //try to get the user data, and if there is an error catch the number format exception
        //also try to catch a null pointer exception if a course or student is not selected
        try {
            totalCorrect  = Integer.parseInt(gradeItemPointsCorrect.getText());
            totalPossible = Integer.parseInt(totalPoints.getText());
            name = gradeItemName.getText().trim().toUpperCase();

            //run the Static method for Student to delete the grade item based on the user data
            Student.deleteSingleGradeItem(studentsListView.getSelectionModel().getSelectedItem().getStudentID(),
                    courseListView.getSelectionModel().getSelectedItem().getCourseID(),
                    name, totalCorrect, totalPossible
            );

        } catch (NumberFormatException e) {
            clearFieldsOnDataException();
            super.displayAlertBox("Error with Data", "All fields have been cleared, please try again");
        } catch (NullPointerException e){
            super.displayAlertBox("Error While Searching", "A Student and a Course must be selected!");
        }
    }

    /**
     * public method to initialize the course list to all courses
     */
    public void initializeCourseList(){
        //first clear the all courses array list
        allCourses.clear();

        //create the select student query object
        SelectStudentGrades selectStudentGrades = new SelectStudentGrades("GradeBook_Application", "root", "Winter I_S Coming!");

        //set the courses array list to the result of the course query
        allCourses = selectStudentGrades.executeCourseQuery();

        //add the array list to the observable array list
        courses.addAll(allCourses);

        //
        courseListView.setItems(courses);
    }

    /**
     * public method to initialize the student course list based on the course passed in
     * @param course the course the students are in
     */
    public void initializeStudentList(Course course){

        //first clear the array to be sure no duplicates or leftover students exist
        allStudents.clear();
        //create the select student query
        SelectStudentGrades selectStudentGrades = new SelectStudentGrades("GradeBook_Application", "root", "Winter I_S Coming!");

        //run the student query method which builds an array list of student objects belonging to the course
        allStudents = selectStudentGrades.executeStudentQuery(course);

        //set the observable array list to the returned array list
        students.addAll(allStudents);

        //set the items of the list view to the observable array list
        studentsListView.setItems(students);
    }


    /**
     * public method to clear all the fields
     * if there is an exception throw likely due to bad data
     */
    public void clearFieldsOnDataException(){

        gradeItemName.setPromptText("Enter Grade Name");
        gradeItemPointsCorrect.setPromptText("Enter Points Correct");
        totalPoints.setPromptText("Enter Total Points");
        studentsListView.getSelectionModel().clearSelection();
        studentsListView.getItems().removeAll();
        studentsListView.refresh();
        allStudents.clear();
        gradeItemPointsCorrect.selectAll();
        gradeItemPointsCorrect.requestFocus();

    }

    @FXML
    void calculateStudentGrades(ActionEvent event) {
        ArrayList<Student> students = new ArrayList<>();
        DisplayBox displayBox = new DisplayBox();

        try {
            //create the select student query
            SelectStudentGrades selectStudentGrades = new SelectStudentGrades("GradeBook_Application", "root", "Winter I_S Coming!");
            students = selectStudentGrades.executeStudentQuery(courseListView.getSelectionModel().getSelectedItem());
            courseListView.getSelectionModel().getSelectedItem().setStudents(students);
            displayBox.display("Course Grades for Course" + courseListView.getSelectionModel().getSelectedItem(),
                    courseListView.getSelectionModel().getSelectedItem().calculateAllStudentsInCourse(),
                    "src/com/dylanhoffman/compsci316/UI/stylesheet.css");
        } catch (NullPointerException e){
            super.displayAlertBox("Error While Calculating", "A Course must be selected!");
        }


    }

}

