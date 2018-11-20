package src.com.dylanhoffman.compsci316.model.grading;

/**
 * Class for High School Grading Model
 * implements the Grade Model Interface
 * has 1 private variable letterGrade
 * letterGrade is a Character array to hold two chars
 */
public class HighSchoolGradeModel implements GradeModel {

    private Character[] letterGrade = new Character[2];
    /*
        A+ 97-100
        A 93-96
        A- 90-92
        B+ 87-89
        B 83-86
        B- 80-82
        C+ 77-79
        C 73-76
        C- 70-72
        D+ 67-69
        D 65-66
        E/F Below 65
     */

    /**
     * override's the interfaces method calculateGrade
     * @param totalCorrect is the number of points correct
     * @param totalPossible is the total possible points
     * @return returns the letterGrade char array
     * @throws InvalidGradeException if the grade letter cannot be assigned according to the scale
     */
    @Override
    public Character[] calculateGrade(int totalCorrect, int totalPossible) throws InvalidGradeException{
        float percent = (float)totalCorrect/totalPossible;

        if (percent > .89){
            if (percent >.96){
                setLetterGradePosition1('A', '+');
            } else if (percent < .96 && percent > .92 ){
                setLetterGradePosition1('A', '.');
            } else if (percent < .92){
                setLetterGradePosition1('A', '-');
            }

        } else if (percent < .89 && percent > .79){
                if (percent>.86){
                    setLetterGradePosition1('B', '+');
                }
                else if (percent < .86 && percent > .82)
                    setLetterGradePosition1('B', '.');
                else {
                    setLetterGradePosition1('B', '-');
                }

        } else if (percent <.79 && percent >.69){
            if (percent > .76){
                setLetterGradePosition1('C', '+');
            } else if (percent < .76 && percent > .72){
                setLetterGradePosition1('C', '.');
            } else{
                setLetterGradePosition1('C', '-');
            }


        } else if (percent < .69 && percent > .59){
            if (percent > .66){
                setLetterGradePosition1('D', '+');
            } else if (percent < .66 && percent > .62){
                setLetterGradePosition1('D', '.');
            } else {
                setLetterGradePosition1('D', '-');
            }


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
