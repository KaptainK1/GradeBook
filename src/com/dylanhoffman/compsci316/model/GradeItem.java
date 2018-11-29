package src.com.dylanhoffman.compsci316.model;

import src.com.dylanhoffman.compsci316.Constants;
import src.com.dylanhoffman.compsci316.logging.Log;

/**
 * Class for holding a single GradeItem
 * GradeItems belong to a single student
 */
public class GradeItem {

    private int totalCorrect;
    private int totalPossible;
    private Character[] letterGrade;
    private String gradeName;
    private static int totalGradeItems = 0;

    /**
     * Constructor that takes 4 params:
     * @param name variable to hold the name of the grade item
     * @param totalCorrect variable to hold the number of points correct
     * @param totalPossible variable to hold the number of points possible
     */
    public GradeItem(String name, int totalCorrect, int totalPossible){
        //check to see if the name is null
        if (name.equals("")) {
            Log.writeToLog(Constants.getLogPath(), "Please enter a grade name");
            throw new IllegalArgumentException("Please enter a grade name");
        }
        //check to see if the total correct is a negative number or greater than the total possible
        if (totalCorrect < 0 || totalCorrect > totalPossible) {
            Log.writeToLog(Constants.getLogPath(), "the total correct amount is less than 0 or greater than the total possible");
            throw new IllegalArgumentException("the total correct amount is less than 0 or greater than the total possible");
        }

        this.gradeName=name;
        this.totalPossible=totalPossible;
        this.totalCorrect=totalCorrect;
        totalGradeItems++;
    }

    //override the tostring method to return the name of the assignment, the letter grade, and the percentage earned
    @Override
    public String toString(){
        return getGradeName() + " " + printLetterGrade() + " %" + (float)getTotalCorrect()/getTotalPossible();
    }

    /**
     * public method to print the letter grade of the assignment
     * @return returns the grade as a string from a char array
     */
    public String printLetterGrade(){
        String grade = "";

        for (int i = 0; i < getLetterGrade().length; i++) {
            grade=grade.concat(getLetterGrade()[i].toString());
        }
        return grade;
    }

    //getters and setters

    /**
     * method to return the totalCorrect private variable
     * @return returns int totalCorrect
     */
    public int getTotalCorrect() {
        return totalCorrect;
    }

    /**
     * public Setter method to set the totalCorrect private variable
     * @param totalCorrect as int
     */
    public void setTotalCorrect(int totalCorrect) {
        if (totalCorrect < 0 || totalCorrect > this.getTotalPossible()){
            throw new IllegalArgumentException("the total correct amount is less than 0 or greater than the total possible");
        }
        this.totalCorrect = totalCorrect;
    }

    /**
     * method to return the totalPossible private variable
     * @return returns the private int totalPossible
     */
    public int getTotalPossible() {
        return totalPossible;
    }

    /**
     *  method to set the totalPossible private variable
     * @param totalPossible as int
     */
    public void setTotalPossible(int totalPossible) {
        if (totalPossible < 10){
            throw new IllegalArgumentException("The total possible points must be greater than or equal to 10");
        }
        this.totalPossible = totalPossible;
    }

    /**
     * method to return the private character array for the LetterGrade
     * @return returns the character array
     */
    public Character[] getLetterGrade() {
        return letterGrade;
    }

    /**
     * method to set the private character array LetterGrade
     * @param letterGrade as char array
     */
    public void setLetterGrade(Character[] letterGrade) {
        this.letterGrade = letterGrade;
    }

    /**
     * method to get the name of the Grade Item
     * @return the grade name as String
     */
    public String getGradeName() {
        return gradeName;
    }

    /**
     * method for setting the name of the grade item
     * @param gradeName as string
     */
    public void setGradeName(String gradeName) {

        this.gradeName = gradeName;
    }

    /**
     * static method for getting the total grade items count
     * @return the static int for total grade items created
     */
    public static int getTotalGradeItems() {
        return totalGradeItems;
    }

}
