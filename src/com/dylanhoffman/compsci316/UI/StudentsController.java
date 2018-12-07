package src.com.dylanhoffman.compsci316.UI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import src.com.dylanhoffman.compsci316.Constants;
import src.com.dylanhoffman.compsci316.logging.Log;
import src.com.dylanhoffman.compsci316.model.Course;
import src.com.dylanhoffman.compsci316.model.Student;
import src.com.dylanhoffman.compsci316.model.file_processing.ProcessStudentFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;


/**
 * Class for interacting with the Student UI
 * Extends the main controller class
 */
public class StudentsController extends MainController{


    @FXML
    private TextField studentLastName;

    @FXML
    private TextField studentStudentID;

    @FXML
    private TextField studentFirstName;

    @FXML
    private GridPane studentContainer;

    /**
     * Method for inserting a student into the database
     * @param event button click
     */
    @FXML
    void insertStudent(ActionEvent event) {
        try {
            Student student = new Student(studentFirstName.getText().trim(), studentLastName.getText().trim(), Integer.parseInt(studentStudentID.getText().trim()));
            student.insertStudent();
            super.displayDisplayBox("Student Added Successfully!", studentFirstName.getText().trim() + " Was successfully added!");
        } catch (InputMismatchException e){
            Log.writeToLog(Constants.getLogPath(),e.getMessage());
            clearFieldsOnDataException();
            super.displayAlertBox("Error with Student Data!", "Error with data entered for the student. Please check your input and try again. \n" + e.getMessage());
        } catch (IllegalArgumentException e){
            Log.writeToLog(Constants.getLogPath(),e.getMessage());
            clearFieldsOnDataException();
            super.displayAlertBox("Error with Student Data!", e.getMessage());
        } catch (SQLIntegrityConstraintViolationException e) {
            Log.writeToLog(Constants.getLogPath(), e.getMessage());
            clearFieldsOnDataException();
            super.displayAlertBox("Cannot Add Student with same ID", "Student ID entered matches another id already entered.");
        }catch (SQLException e){
                Log.writeToLog(Constants.getLogPath(),e.getMessage());
                clearFieldsOnDataException();
                super.displayAlertBox("Error with Database", "There was an error with the database, please try again");
        }catch (Exception e){
            Log.writeToLog(Constants.getLogPath(),e.getMessage());
            clearFieldsOnDataException();
            super.displayAlertBox("Generic Error with Adding Student", "Please Check the data entered for the student and try again.");
        }

    }

    @FXML
    void searchStudents(ActionEvent event) {
        String students[][];
        DisplayBox displayBox = new DisplayBox();

        try {
           students = Student.searchStudents();
           displayBox.displayGridPane(students,"All Students", Constants.getCssPath());
        }catch (IllegalArgumentException e){
            Log.writeToLog(Constants.getLogPath(),e.getMessage());
            clearFieldsOnDataException();
            super.displayAlertBox("Error with Student Data!", e.getMessage());
        } catch (Exception e){
            Log.writeToLog(Constants.getLogPath(),e.getMessage());
            clearFieldsOnDataException();
            super.displayAlertBox("Generic Error with Adding Student", "Please Check the data entered for the student and try again.");
        }

    }

    @FXML
    void deleteStudent(ActionEvent event) {

        boolean isError = false;

        ConfirmBox confirmBox = new ConfirmBox();
        confirmBox.displayConfirmBox("Warning! Data loss possible!", "Are you sure you want to delete this Student?",
                Constants.getCssPath());
        if (confirmBox.getIsUserChoice()) {
            try {
                Student student = new Student(studentFirstName.getText().trim(), studentLastName.getText().trim(), Integer.parseInt(studentStudentID.getText().trim()));
                student.deleteStudent();
//                 if (!this.removeStudentFromAllStudentsList(student))
//                        Log.writeToLog(Constants.getLogPath(), "Error with removing Student from gradeitems list");

            } catch (SQLException e) {
                isError=true;
                Log.writeToLog(Constants.getLogPath(), e.getMessage());
                super.displayAlertBox("Cannot Delete the Course", "Student currently has active Gradeitems, please remove those before retrying");
            } catch (InputMismatchException e) {
                isError=true;
                Log.writeToLog(Constants.getLogPath(), e.getMessage());
                clearFieldsOnDataException();
                super.displayAlertBox("Error with Student Data!", "Error with data entered for the student. Please check your input and try again.");
            } catch (IllegalArgumentException e) {
                isError=true;
                Log.writeToLog(Constants.getLogPath(), e.getMessage());
                clearFieldsOnDataException();
                super.displayAlertBox("Error with Student Data!", e.getMessage());
            } catch (Exception e) {
                isError=true;
                Log.writeToLog(Constants.getLogPath(), e.getMessage());
                clearFieldsOnDataException();
                super.displayAlertBox("Generic Error with Adding Student", "Please Check the data entered for the student and try again.");
            }

            if (!isError)
                super.displayAlertBox("Student Deleted! ", studentFirstName.getText().trim() + " " + studentLastName.getText().trim());
        }

    }

    /**
     * public method to clear all the fields
     * if there is an exception throw likely due to bad data
     */
    public void clearFieldsOnDataException(){

        studentFirstName.setPromptText("Enter First Name");
        studentLastName.setPromptText("Enter Last Name");
        studentStudentID.setPromptText("Enter Student ID");
        studentFirstName.selectAll();
        studentFirstName.requestFocus();
    }

    @FXML
    public void importObjects(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Select Students to Import");

        fileChooser.setInitialDirectory(new File("."));

        File file = fileChooser.showOpenDialog(studentContainer.getScene().getWindow());

        super.importFile(file, new ProcessStudentFile());

    }

//
//    public boolean removeStudentFromAllStudentsList(Student student){
//
//        try {
//            super.getGradeItemsController().populateAllStudents.remove(student);
//            super.getGradeItemsController().allStudentsListView.getItems().remove(student);
//            super.getGradeItemsController().allStudentsAvailable.remove(student);
//        } catch (Exception e){
//            return false;
//        }
//        return true;
//    }





}
