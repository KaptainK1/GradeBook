package src.com.dylanhoffman.compsci316.model;

import src.com.dylanhoffman.compsci316.logging.Log;

/**
 * Quiz class that extends the gradeitem class
 */
public class Quiz extends GradeItem {

    /**
     * constructor for the Quiz class
     * has 3 parameters
     * @param name string to hold the name of the quiz
     * @param totalCorrect int to hold the number of points correct
     * @param totalPossible int to hold the number of points possible
     * totalPossible must be greater than 10 and less than 20 points
     */
    public Quiz(String name, int totalCorrect, int totalPossible){
        super(name, totalCorrect, totalPossible);
            if (totalPossible > 21 || totalPossible < 9) {
                Log.writeToLog("/Users/dhoffman/Documents/Gradebook/log.txt","A Quiz must be less than 20 total possible points");
                throw new IllegalArgumentException("A Quiz must be less than 20 total possible points");
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
