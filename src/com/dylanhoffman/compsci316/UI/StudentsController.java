package src.com.dylanhoffman.compsci316.UI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import src.com.dylanhoffman.compsci316.Constants;
import src.com.dylanhoffman.compsci316.logging.Log;
import src.com.dylanhoffman.compsci316.model.Student;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.InputMismatchException;


public class StudentsController extends MainController{


    @FXML
    private TextField studentLastName;

    @FXML
    private TextField studentStudentID;

    @FXML
    private TextField studentFirstName;

    @FXML
    void insertStudent(ActionEvent event) {

        try {
            Student.insertStudent(studentFirstName.getText().trim(), studentLastName.getText().trim(), Integer.parseInt(studentStudentID.getText().trim()));
            super.displayAlertBox("Student Added! ", studentFirstName.getText().trim() + " " + studentLastName.getText().trim());
        } catch (InputMismatchException e){
            Log.writeToLog(Constants.getLogPath(),e.getMessage());
            clearFieldsOnDataException();
            super.displayAlertBox("Error with Student Data!", "Error with data entered for the student. Please check your input and try again.");
        } catch (IllegalArgumentException e){
            Log.writeToLog(Constants.getLogPath(),e.getMessage());
            clearFieldsOnDataException();
            super.displayAlertBox("Error with Student Data!", e.getMessage());
        } catch (SQLIntegrityConstraintViolationException e){
            Log.writeToLog(Constants.getLogPath(),e.getMessage());
            clearFieldsOnDataException();
            super.displayAlertBox("Cannot Add Student with same ID", "Student ID entered matches another id already entered.");
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

        try {
            Student.deleteStudent(Integer.parseInt(studentStudentID.getText().trim()));
            super.displayAlertBox("Student Deleted! ", studentFirstName.getText().trim() + " " + studentLastName.getText().trim());
        } catch (InputMismatchException e){
            Log.writeToLog(Constants.getLogPath(),e.getMessage());
            clearFieldsOnDataException();
            super.displayAlertBox("Error with Student Data!", "Error with data entered for the student. Please check your input and try again.");
        } catch (IllegalArgumentException e){
            Log.writeToLog(Constants.getLogPath(),e.getMessage());
            clearFieldsOnDataException();
            super.displayAlertBox("Error with Student Data!", e.getMessage());
        } catch (Exception e){
            Log.writeToLog(Constants.getLogPath(),e.getMessage());
            clearFieldsOnDataException();
            super.displayAlertBox("Generic Error with Adding Student", "Please Check the data entered for the student and try again.");
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
    void importObjects(ActionEvent event) {

    }


}
