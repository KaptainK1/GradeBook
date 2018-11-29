package src.com.dylanhoffman.compsci316.model;

import src.com.dylanhoffman.compsci316.Constants;
import src.com.dylanhoffman.compsci316.logging.Log;

/**
 * Exam class that extends the gradeitem class
 */
public class Exam extends GradeItem {

    /**
     * Constructor for the gradeItem class
     * accepts 3 parameters
     * @param name string to hold the name of the quiz
     * @param totalCorrect int to hold the number of points correct
     * @param totalPossible int to hold the number of points possible
     * totalPossible must be greater than 50 and less than 200 points
     */
    public Exam(String name, int totalCorrect, int totalPossible){
        super(name, totalCorrect, totalPossible);

        if (totalPossible > 201 || totalPossible < 51) {
            Log.writeToLog(Constants.getLogPath(),"An Exam must be between 50 and 200 points");
            throw new IllegalArgumentException("An Exam must be between 50 and 200 points");
        }
    }

    /**
     * overridden method of the tostring method
     * @return returns the string of the object as
     * class name + the gradeitem representation of the object
     */
    @Override
    public String toString(){
        return getClass().toString() + " " + super.toString();
    }

}
