package src.com.dylanhoffman.compsci316.UI;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import src.com.dylanhoffman.compsci316.Constants;
import src.com.dylanhoffman.compsci316.logging.Log;
import src.com.dylanhoffman.compsci316.model.Course;
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

    public void displayDisplayBox(String title, String message){
        DisplayBox displayBox = new DisplayBox();
        displayBox.display(title,message,Constants.getCssPath());
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


}
