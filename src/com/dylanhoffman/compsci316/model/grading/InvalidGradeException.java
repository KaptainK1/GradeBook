package src.com.dylanhoffman.compsci316.model.grading;

/**
 * Custom Exception Class to be used in the grademodel interface
 */
public class InvalidGradeException extends Exception {

    /**
     * throw the exception if the grade letter cannot not assigned
     */
    InvalidGradeException(){
        super("Invalid grade returned, check the points correct and the points possible values");
    }

}
