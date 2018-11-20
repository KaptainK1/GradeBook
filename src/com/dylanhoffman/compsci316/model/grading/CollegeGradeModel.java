package src.com.dylanhoffman.compsci316.model.grading;

/**
 * class CollegeGradeModel implements Grademodel interface
 * has 1 private variable letterGrade
 * letterGrade is a Character array to hold two chars
 */
public class CollegeGradeModel implements GradeModel {

    private Character[] letterGrade = new Character[2];

    /*
    A	93 - 100	4.0
    AB	88 - 92	3.5
    B	83 - 87	3.0
    BC	78 - 82	2.5
    C	73 - 77	2.0
    CD	68 - 72	1.5
    D	60 - 67	1.0
    F	< 60	0.0
     */

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

        if (percent > .92){
            setLetterGradePosition1('A', '.');
        } else if (percent < .92 && percent > .86){
            setLetterGradePosition1('A' , 'B');
        } else if (percent <.86 && percent >.81){
            setLetterGradePosition1('B', '.');
        } else if (percent < .81 && percent >.76){
            setLetterGradePosition1('B', 'C');
        } else if (percent < .76 && percent > .72){
            setLetterGradePosition1('C', '.');
        } else if (percent < .72 && percent > .67){
            setLetterGradePosition1('C', 'D');
        } else if (percent <.67 && percent > .59){
            setLetterGradePosition1('D', '.');
        } else if (percent <.59){
            setLetterGradePosition1('F', '.');
        }
        else{
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
