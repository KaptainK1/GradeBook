package src.com.dylanhoffman.compsci316.model.grading;

/**
 * Class for Elementary School Grading Model
 * implements the Grade Model Interface
 * has 1 private variable letterGrade
 * letterGrade is a Character array to hold two chars
 */
public class ElementaryGradeModel implements GradeModel {

    private Character[] letterGrade = new Character[2];

    /**
     * override's the interfaces method calculateGrade
     * @param totalCorrect is the number of points correct
     * @param totalPossible is the total possible points
     * @return returns the letterGrade char array
     * @throws InvalidGradeException if the grade letter cannot be assigned according to the scale
     */
    @Override
    public Character[] calculateGrade(int totalCorrect, int totalPossible) throws InvalidGradeException {

        float percent = (float)totalCorrect/totalPossible;

        if (percent > 0.89){
            setLetterGradePosition1('A', '.');
        } else if (percent < .89 && percent > .79){
            setLetterGradePosition1('B' , '.');
        } else if (percent <.79 && percent >.69){
            setLetterGradePosition1('C', '.');
        } else if (percent < .69 && percent > .59){
            setLetterGradePosition1('D', '.');
        } else if (percent <.59){
            setLetterGradePosition1('F', '.');
        }else{
            //set the grade to E if there is an error
            throw new InvalidGradeException();
        }
        return getLetterGrade();
    }

    /**
     * private method to set both char array's positions
     * @param letterGrade1 as char[0]
     * @param letterGrade2 as char[1]
     */
    public void setLetterGradePosition1(char letterGrade1, char letterGrade2){
        letterGrade[0] = letterGrade1;
        letterGrade[1] = letterGrade2;
    }

    /**
     * public method to get the letterGrade char array
     * @return the char array which was set in the overridden interface method
     */
    public Character[] getLetterGrade(){
        return this.letterGrade;
    }

    @Override
    public String toString(){
        return this.getClass().getSimpleName();
    }

}
