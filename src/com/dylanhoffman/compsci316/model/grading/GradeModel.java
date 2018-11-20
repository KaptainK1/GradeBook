package src.com.dylanhoffman.compsci316.model.grading;

/**
 * Public interface that requires 1 method implementation
 *  of calculateGrade that takes 2 paramaters
 *  first paramater is the number of points correct as integer
 *  second paramater is the number of points possible as integer
 *  method throws invalid grade exception
 */
public interface GradeModel {
    Character[] calculateGrade(int totalCorrect, int totalPossible) throws InvalidGradeException;

}
