package src.com.dylanhoffman.compsci316.UI;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import src.com.dylanhoffman.compsci316.Constants;
import src.com.dylanhoffman.compsci316.logging.Log;
import src.com.dylanhoffman.compsci316.model.Course;
import src.com.dylanhoffman.compsci316.model.GradeItem;
import src.com.dylanhoffman.compsci316.model.Student;
import src.com.dylanhoffman.compsci316.utility.SelectStudentGrades;
import java.sql.SQLException;
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
    public ListView<Course> courseListView;

    @FXML
    public ListView<Student> studentsListView;

    @FXML
    public ListView<Student> allStudentsListView;

    //private observable list to hold courses and students
    public ObservableList<Course> courses = FXCollections.observableArrayList();
    public ObservableList<Student> students = FXCollections.observableArrayList();
    public ObservableList<Student> allStudentsAvailable = FXCollections.observableArrayList();

    //private array list to hold all students and all courses
    public ArrayList<Student> allStudents = new ArrayList<>();
    public ArrayList<Course> allCourses = new ArrayList<>();
    public ArrayList<Student> populateAllStudents = new ArrayList<>();

    /**
     * method to initialize the UI
     * initialize method to first populate the course list
     * then populate the student list based on the course selected
     */

    public void initialize(){

        initializeAllStudents();
        initializeCourseList();

        //use a listener for when the course selected item changes
        //then populate the student list
        courseListView.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Course>() {
                    @Override
                    public void changed(ObservableValue<? extends Course> observable, Course oldValue, Course newValue) {
                        //populate the student list based on the new value selected
                        try {
                            allStudents.clear();
                            students.removeAll();
                            studentsListView.getItems().clear();
                            initializeStudentList(newValue);
                        } catch (NullPointerException e){
                            Log.writeToLog(Constants.getLogPath(),e.getMessage());
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
     * @param event button click
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
            Log.writeToLog(Constants.getLogPath(),e.getMessage());
            clearFieldsOnDataException();
        }

        // try to create a grade item object and insert it via the static student method insert grade item
        //also try to catch a null pointer exception if a course or student is not selected
        //also try to catch an illegal argument exception thrown when the object is created
        //finally default catch all exception
        try {

                if(allStudentsListView.getSelectionModel().getSelectedItem()!=null && studentsListView.getSelectionModel().getSelectedItem()==null){
                    GradeItem gradeItem = new GradeItem(name,pointsCorrect,pointsPossible);
                    Student.insertGradeItem(allStudentsListView.getSelectionModel().getSelectedItem(),gradeItem,courseListView.getSelectionModel().getSelectedItem());
                    super.displayDisplayBox("GradeItem Added Successfully!", gradeItemName.getText().trim() + " Was successfully added!");
                } else if (allStudentsListView.getSelectionModel().getSelectedItem()==null && studentsListView.getSelectionModel().getSelectedItem()!=null){
                    GradeItem gradeItem = new GradeItem(name,pointsCorrect,pointsPossible);
                    Student.insertGradeItem(studentsListView.getSelectionModel().getSelectedItem(),gradeItem,courseListView.getSelectionModel().getSelectedItem());
                    super.displayDisplayBox("GradeItem Added Successfully!!", gradeItemName.getText().trim() + " Was successfully added!");
                } else {
                    throw new IllegalArgumentException("You may only select a student from the all students list, or the students in course list!");
                }

        } catch (NullPointerException e) {
            Log.writeToLog(Constants.getLogPath(),e.getMessage());
            super.displayAlertBox("Error - Student or Course Not Selected", "A Course and a Student must be selected!!");
        } catch (IllegalArgumentException e) {
            Log.writeToLog(Constants.getLogPath(), e.getMessage());
            super.displayAlertBox("Error with GradeItem Data", e.getMessage());
        }catch (SQLException e){
                Log.writeToLog(Constants.getLogPath(),e.getMessage());
                super.displayAlertBox("Error with the Database", "Please check your data and try again.");
        } catch (Exception e){
            Log.writeToLog(Constants.getLogPath(),e.getMessage());
            super.displayAlertBox("General Error", "An error has occurred, please check the data and try again!");
        }

        allStudents.clear();
        students.removeAll();
        studentsListView.getItems().clear();
        allStudentsListView.getSelectionModel().clearSelection();
        initializeStudentList(courseListView.getSelectionModel().getSelectedItem());
    }

    /**
     * method to search for grade items
     * @param event button click
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
            displayBox.displayGridPane( arrayGradeItems, "GradeItems For " + studentsListView.getSelectionModel().getSelectedItem(),Constants.getCssPath());
        } catch (NullPointerException e){
            Log.writeToLog(Constants.getLogPath(),e.getMessage());
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

            super.displayDisplayBox("GradeItem Deleted Successfully!", gradeItemName.getText().trim() + " Was Deleted!");
        } catch (NumberFormatException e) {
            Log.writeToLog(Constants.getLogPath(),e.getMessage());
            clearFieldsOnDataException();
            super.displayAlertBox("Error with Data", "All fields have been cleared, please try again");
        } catch (NullPointerException e){
            Log.writeToLog(Constants.getLogPath(),e.getMessage());
            super.displayAlertBox("Error While Searching", "A Student and a Course must be selected!");
        } catch (SQLException e) {
            Log.writeToLog(Constants.getLogPath(), e.getMessage());
            super.displayAlertBox("Error with the Database", "Please check your data and try again");
        } catch (Exception e){
            Log.writeToLog(Constants.getLogPath(), e.getMessage());
            super.displayAlertBox("Generic Error","Please try your action again");
        }

        allStudents.clear();
        students.removeAll();
        studentsListView.getItems().clear();
        initializeStudentList(courseListView.getSelectionModel().getSelectedItem());
    }

    /**
     * public method to initialize the course list to all courses
     */
    public void initializeCourseList(){
        //first clear the all courses array list
        allCourses.clear();
        courses.removeAll();
        courseListView.getItems().clear();


        //create the select student query object
        SelectStudentGrades selectStudentGrades = new SelectStudentGrades("GradeBook_Application", "root", "Winter I_S Coming!");

        //set the courses array list to the result of the course query
        try {
            allCourses = selectStudentGrades.executeCourseQueryThrows();
        } catch (SQLException e) {
            Log.writeToLog(Constants.getLogPath(),e.getMessage());
            super.displayAlertBox("Error with initializing course list", "Error with the database, please try again");
        }

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
        try {
            allStudents = selectStudentGrades.executeStudentQueryThrows(course);
        } catch (SQLException e) {
           Log.writeToLog(Constants.getLogPath(), e.getMessage());
           super.displayAlertBox("Error with initializing course list", "There was an error with the database, please try again.");
        }

        //set the observable array list to the returned array list
        students.addAll(allStudents);

        //set the items of the list view to the observable array list
        studentsListView.setItems(students);
    }

    public void initializeAllStudents(){

        populateAllStudents.clear();
        allStudentsAvailable.removeAll();
        allStudentsListView.getItems().clear();

        //create the select student query
        SelectStudentGrades selectStudentGrades = new SelectStudentGrades("GradeBook_Application", "root", "Winter I_S Coming!");

        //run the student query method which builds an array list of student objects belonging to the course
        try {
            populateAllStudents = selectStudentGrades.executeALLStudentQueryThrows();
        } catch (SQLException e) {
            Log.writeToLog(Constants.getLogPath(), e.getMessage());
            super.displayAlertBox("Error with initializing course list", "There was an error with the database, please try again.");
        }

        //set the observable array list to the returned array list
        allStudentsAvailable.addAll(populateAllStudents);

        //set the items of the list view to the observable array list
        allStudentsListView.setItems(allStudentsAvailable);

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

    /**
     * Method to calculate all student grades based on the course selected
     * @param event button click
     */
    @FXML
    void calculateStudentGrades(ActionEvent event) {
        ArrayList<Student> students = new ArrayList<>();
        DisplayBox displayBox = new DisplayBox();

        try {
            //create the select student query
            SelectStudentGrades selectStudentGrades = new SelectStudentGrades("GradeBook_Application", "root", "Winter I_S Coming!");

            //assign the student array to the result of query
            students = selectStudentGrades.executeStudentQueryThrows(courseListView.getSelectionModel().getSelectedItem());

            //set course's students array list equal to the current students array list
            courseListView.getSelectionModel().getSelectedItem().setStudents(students);

            //run the course's calculate all students in course method
            displayBox.display("Course Grades for Course" + courseListView.getSelectionModel().getSelectedItem(),
                    courseListView.getSelectionModel().getSelectedItem().calculateAllStudentsInCourse(),
                    Constants.getCssPath());
        } catch (NullPointerException e){
            Log.writeToLog(Constants.getLogPath(),e.getMessage());
            super.displayAlertBox("Error While Calculating", "A Course must be selected!");
        } catch (SQLException e) {
            Log.writeToLog(Constants.getLogPath(), e.getMessage());
            super.displayAlertBox("Error with Database", "Please check your data and try again");
        }

    }
//
//    public boolean removeStudentFromAllStudentsList(Student student){
//
//        try {
//            populateAllStudents.remove(student);
//            allStudentsListView.getItems().remove(student);
//            allStudentsAvailable.remove(student);
//        } catch (Exception e){
//            return false;
//        }
//        return true;
//    }
}