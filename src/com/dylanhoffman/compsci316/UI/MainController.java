package src.com.dylanhoffman.compsci316.UI;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import src.com.dylanhoffman.compsci316.Constants;
import src.com.dylanhoffman.compsci316.logging.Log;
import src.com.dylanhoffman.compsci316.model.Student;
import src.com.dylanhoffman.compsci316.model.file_processing.ProcessFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.NoSuchElementException;


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
     * Method to be used by other controller classes
     * creates a display box and shows it to the screen
     * @param title title of the window
     * @param message message to be displayed
     */
    public void displayDisplayBox(String title, String message){
        DisplayBox displayBox = new DisplayBox();
        displayBox.display(title,message,Constants.getCssPath());
    }

    /**
     * Method for refreshing data for the gradeitems tab
     * @param e tab selected
     */
    @FXML
    public void initializeGradesTab(Event e){
        gradeItemsController.initializeCourseList();
//        gradeItemsController.initializeAllStudents();
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

    /**
     * method to be used for other controller classes
     * method that takes a file, and imports it (should be CSV)
     * @param file the file to be uploaded
     * @param processFile the process for which the file's contents needs to be processed
     */
    //method for processing an imported file
    public void importFile(File file, ProcessFile processFile) {

        //buffer reader object to read the file that is passed in
        BufferedReader bufferedReader = null;

        //string variable to hold the next line to be read by the buffer reader
        String nextLine;

        try {

            bufferedReader = new BufferedReader(new FileReader(file));

            while((nextLine = bufferedReader.readLine()) != null){

                //set the current line to the next line split by the delimiter
                String values[] = nextLine.split(Constants.getDelimiter().trim());

                try {

                    processFile.processFile(values);

                } catch (NumberFormatException e){
                    Log.writeToLog(Constants.getLogPath(),e.getMessage());
                    displayAlertBox("Error with Imported Data",
                            e.getMessage() + "\n Is not valid input. Aborting Row import and continuing");

                } catch (SQLIntegrityConstraintViolationException e) {
                    Log.writeToLog(Constants.getLogPath(),e.getMessage());
                    displayAlertBox("Error with the Data", "Cannot enter duplicate ID for Course!");
                    e.printStackTrace();
                }catch (SQLException e) {
                    Log.writeToLog(Constants.getLogPath(),e.getMessage());
                    displayAlertBox("Error with the Database", e.getMessage());
                    e.printStackTrace();
                }
            }

        } catch (IOException | NoSuchElementException e) {
            Log.writeToLog(Constants.getLogPath(),e.getMessage());
            displayAlertBox("Error with Importing", e.getMessage());
        } catch (NumberFormatException e){
            Log.writeToLog(Constants.getLogPath(),e.getMessage());
            displayAlertBox("Error with Number", "Record Skipped");
        }

        //cleanup the buffer reader obj
        finally {
            try {
                if (bufferedReader != null)
                    bufferedReader.close();
            } catch (IOException | NullPointerException e) {
                Log.writeToLog(Constants.getLogPath(), e.getMessage());
                e.printStackTrace();
            }
        }
    }


//    public CoursesController getCoursesController() {
//        return coursesController;
//    }
//
//    public GradeItemsController getGradeItemsController() {
//        return gradeItemsController;
//    }
//
//    public StudentsController getStudentsController() {
//        return studentsController;
//    }
}
