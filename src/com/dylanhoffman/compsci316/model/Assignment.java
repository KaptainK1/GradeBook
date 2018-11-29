package src.com.dylanhoffman.compsci316.model;

import src.com.dylanhoffman.compsci316.Constants;
import src.com.dylanhoffman.compsci316.logging.Log;

/**
 * Assignment Class that extends GradeItem class
 */
public class Assignment extends GradeItem {


    /**
     * constructor for the Assignment class
     * has 3 parameters
     * @param name string to hold the name of the quiz
     * @param totalCorrect int to hold the number of points correct
     * @param totalPossible int to hold the number of points possible
     * totalPossible must be greater than 10 and less than 100 points
     */
    public Assignment(String name, int totalCorrect, int totalPossible){
        super(name, totalCorrect, totalPossible );

        if (totalPossible > 101 || totalPossible < 9) {
            Log.writeToLog(Constants.getLogPath(),"An Assignment cannot be greater than 50 points or Less than 10 points");
            throw new IllegalArgumentException("An Assignment cannot be greater than 50 points or Less than 10 points");
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
