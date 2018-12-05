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


public class StudentsController extends MainController{


    @FXML
    private TextField studentLastName;

    @FXML
    private TextField studentStudentID;

    @FXML
    private TextField studentFirstName;

    @FXML
    private GridPane studentContainer;

    @FXML
    void insertStudent(ActionEvent event) {

        try {
            Student.insertStudent(studentFirstName.getText().trim(), studentLastName.getText().trim(), Integer.parseInt(studentStudentID.getText().trim()));
            super.displayAlertBox("Student Added! ", studentFirstName.getText().trim() + " " + studentLastName.getText().trim());
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

        super.displayDisplayBox("Student Added Successfully!", studentFirstName.getText().trim() + " Was successfully added!");
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

        ConfirmBox confirmBox = new ConfirmBox();
        confirmBox.displayConfirmBox("Warning! Data loss possible!", "Are you sure you want to delete this Student?",
                Constants.getCssPath());
        if (confirmBox.getIsUserChoice()) {
            try {
                Student.deleteStudent(Integer.parseInt(studentStudentID.getText().trim()));
            } catch (SQLException e) {
                Log.writeToLog(Constants.getLogPath(), e.getMessage());
                super.displayAlertBox("Cannot Delete the Course", e.getMessage());

            } catch (InputMismatchException e) {
                Log.writeToLog(Constants.getLogPath(), e.getMessage());
                clearFieldsOnDataException();
                super.displayAlertBox("Error with Student Data!", "Error with data entered for the student. Please check your input and try again.");
            } catch (IllegalArgumentException e) {
                Log.writeToLog(Constants.getLogPath(), e.getMessage());
                clearFieldsOnDataException();
                super.displayAlertBox("Error with Student Data!", e.getMessage());
            } catch (Exception e) {
                Log.writeToLog(Constants.getLogPath(), e.getMessage());
                clearFieldsOnDataException();
                super.displayAlertBox("Generic Error with Adding Student", "Please Check the data entered for the student and try again.");
            }

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

//    //method for processing an imported file
//    public void processFile(File file){
//
//        //buffer reader object to read the file that is passed in
//        BufferedReader bufferedReader = null;
//
//        //string variable to hold the next line to be read by the buffer reader
//        String nextLine;
//
//        try {
//
//            bufferedReader = new BufferedReader(new FileReader(file));
//
//            while((nextLine = bufferedReader.readLine()) != null){
//
//                //set the current line to the next line split by the delimiter
//                String values[] = nextLine.trim().split(Constants.getDelimiter());
//                int id;
//                String firstName;
//                String lastName;
//
//                try {
//                    //set the id to the first column, need to use substring due to quotes
//                    id = Integer.valueOf(values[0].substring(1));
//
//                    //set the num of the course to the 2nd column
//                    firstName = values[1];
//
//                    //set the name to the 3rd column
//                    lastName = values[2];
//
//                    Student.insertStudent(firstName,lastName,id);
//
//                } catch (NumberFormatException e){
//                    Log.writeToLog(Constants.getLogPath(),e.getMessage());
//                    super.displayAlertBox("Error with Imported Data",
//                            e.getMessage() + "\n Is not valid input. Aborting Row import and continuing");
//
//                } catch (SQLIntegrityConstraintViolationException e) {
//                    Log.writeToLog(Constants.getLogPath(),e.getMessage());
//                    super.displayAlertBox("Error with the Data", "Cannot enter duplicate ID for Course!");
//                    e.printStackTrace();
//                }catch (SQLException e) {
//                    Log.writeToLog(Constants.getLogPath(),e.getMessage());
//                    super.displayAlertBox("Error with the Database", e.getMessage());
//                    e.printStackTrace();
//                }
//            }
//
//        } catch (IOException | NoSuchElementException e) {
//            Log.writeToLog(Constants.getLogPath(),e.getMessage());
//            super.displayAlertBox("Error with Importing", e.getMessage());
//        } catch (NumberFormatException e){
//            Log.writeToLog(Constants.getLogPath(),e.getMessage());
//            super.displayAlertBox("Error with Number", "Record Skipped");
//        }
//
//        //cleanup the buffer reader obj
//        finally {
//            try {
//                if (bufferedReader != null)
//                    bufferedReader.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }


}
