package src.com.dylanhoffman.compsci316.model;

import src.com.dylanhoffman.compsci316.logging.Log;
import src.com.dylanhoffman.compsci316.model.grading.*;
import src.com.dylanhoffman.compsci316.utility.Query;
import src.com.dylanhoffman.compsci316.utility.SelectStudentGrades;

import static src.com.dylanhoffman.compsci316.utility.QueryType.DELETE;
import static src.com.dylanhoffman.compsci316.utility.QueryType.INSERT;
import java.util.ArrayList;

/**
 * Class Course contains 5 private variables
 *  int courseID to hold the course id
 *  int courseNum to hold the course number
 *  String courseName to hold the course name
 *  Arraylist students to hold an arraylist of students
 *  GradeModel to hold the scale of how grades will be calculated
 */
public class Course {

    private int courseID;
    private int courseNUM;
    private String courseName;
    private ArrayList<Student> students = new ArrayList<>();
    private GradeModel gradeModel;

    /**
     * Constructor that requires a valid courseID, courseNum, courseName, and gradeModel
     * @param courseID int courseID to hold the course id
     * @param courseNUM int courseNum to hold the course number
     * @param courseName String courseName to hold the course name
     */
    public Course(int courseID, int courseNUM, String courseName, GradeModel gradeModel){ //
        //course number must be between 100 and 1000
        if (courseNUM <100 || courseNUM > 1000){
            throw new IllegalArgumentException("Invalid course number, must be greater than 100 and less than 1000");
        }
        //course name cannot be blank
        if (courseName.equals("")){
            throw new IllegalArgumentException("course name cannot be blank");
        }

        //check to see if the student id is 8 digits
//        if (!isValidCourseID(courseID,6)) {
//            Log.writeToLog("/Users/dhoffman/Documents/Gradebook/log.txt", "Invalid courseID");
//            throw new IllegalArgumentException("Invalid courseID, must contain 6 digits");
//        }
        this.courseID=courseID;
        this.courseNUM=courseNUM;
        this.courseName=courseName;
        this.gradeModel= gradeModel;
    }

    /**
     * Static method to convert a string (or enum) into a GradeModel Object
     * This is used because we store the grademodel for the course into the database as a enum
     * When we need to create a course object, we can call this method by passing in the String
     * that represents the grade model object we want to create.
     * @param strGradeModel
     * @return
     */
    public static GradeModel convertGradeModel(String strGradeModel){
        //set default to elementary grading model
        GradeModel gradeModel = new ElementaryGradeModel();
        switch (strGradeModel){
            case "CollegeGradeModel": gradeModel = new CollegeGradeModel();
            break;
            case "ElementaryGradeModel": gradeModel = new ElementaryGradeModel();
            break;
            case "HighSchoolGradeModel": gradeModel = new HighSchoolGradeModel();
            break;
        }
        return gradeModel;
    }

    /**
     * private method isValidCourseID is used in the constructor
     * to determine if the courseID is atleast the length specified
     * @param id is the id to be passed (courseID)
     * @param length is how long the courseID should be
     * @return returns a boolean isValid
     */
    //method for testing if the entered student id is the required length
    private boolean isValidCourseID(int id, int length){
        boolean isValid;

        //use the log10 function + 1 to mathematically find if the entered student id is of the required length
        double n = Math.log10(id) + 1;

        if ((int)n == length){
            isValid = true;
        } else {
            isValid = false;
        }
        return isValid;
    }

    /**
     * public method that inserts the course into the Database
     */
    public void insertCourse(){
        String strInsert = "into Courses VALUES ( " + getCourseID() + ", " + getCourseNUM() + ", '" + getCourseName() + "'" + ", '" + getGradeModel().getClass().getSimpleName() +"' )";

//        QueryType queryType = INSERT;
        Query insertQuery = new Query("GradeBook_Application", "root", "Winter I_S Coming!", INSERT, strInsert );
        insertQuery.executeQuery();
    }

    /**
     * public method to set the gradeitem's grade based on the course's gradeModel
     * the GradeItem to be graded must be passed as the parameter
     * catches invalid grade exception
     * @param gradeItem as GradeItem
     *
     */
    public void grade(GradeItem gradeItem){
        try {
            gradeItem.setLetterGrade(this.getGradeModel().calculateGrade(gradeItem.getTotalCorrect(), gradeItem.getTotalPossible()));
        } catch (InvalidGradeException e){
            e.getMessage();
        }
    }

    /**
     * main public method to calculate all grade items per student in the course
     */
    public void calculateAllStudents(){

        //create a new character array to hold the grade values
        Character[] grades = new Character[2];
        //loop through the student array list
        for (Student student: getStudents()) {

            grades = this.retrieveStudentGrades(student);
            System.out.println("Student " + student.getFirstName() + " " + student.getLastName() + " earned a(n) " + grades[0] + grades[1] + " in course " + this);
        }
    }

    /**
     * public method to remove a student from the course
     * method also deletes all the student grades from the course
     * @param student student object to delete from the course
     */
    public void removeStudentFromCourse(Student student){
        String query =  " from GradeItems where StudentID = " + student.getStudentID() + " AND CourseID = " + courseID;
        Query insertQuery = new Query("GradeBook_Application", "root", "Winter I_S Coming!", DELETE, query );
    }

    /**
     * public method to remove all students from a course
     * method utilizes remove student from course method
     * @param students array object of students
     */
    public void removeAllStudentsFromCourse(ArrayList<Student> students){

        for (Student currentStudent: students) {
            removeStudentFromCourse(currentStudent);
        }
    }

    /**
     * public method that retrieves the student grade for the entire course
     * @param student accepts 1 parameter of type student
     * @return returns the grade as a Character Array
     */
    //method for calculating a single students grades for this given course
    public Character[] retrieveStudentGrades(Student student){
        //values to hold totals to be retrieved
        int totalCorrect;
        int totalPossible;
        Character[] letterGrade = null;

        //instantiate the select student grades sql object
        SelectStudentGrades selectStudentGrades = new SelectStudentGrades("GradeBook_Application", "root", "Winter I_S Coming!");

        //set the total correct to the results of the query
        totalCorrect = selectStudentGrades.executeStudentCorrectQuery(student.getStudentID(),this.getCourseID(), "TotalCorrect");

        //set the total possible to the results of the query
        totalPossible = selectStudentGrades.executeStudentCorrectQuery(student.getStudentID(),this.getCourseID(), "TotalPossible");

        //set the letter grade by running the static method calculate letter grade total correct divided by total possible
        //catch invalid grade exception (no assignment from a-f)
        try {
            letterGrade = this.getGradeModel().calculateGrade(totalCorrect,totalPossible);
        } catch (InvalidGradeException e){
            e.getMessage();
        }

        return letterGrade;
//        System.out.println("Student " + student.getFirstName() + " " + student.getLastName() + " earned a(n) " + letterGrade[0] + letterGrade[1] + " in course " + this.getCourseName());
    }

//    public void insertStudentIntoClass(Student student){
//
//        //build the insert query
//        String strInsert = "into Class VALUES ( " + "'" + "NULL" + "' , '" + student.getStudentID() + "' , " + this.getCourseID() + " )";
//        //set the query type to insert
////        QueryType queryType = INSERT;
//        //create the query object
//        Query insertQuery = new Query("GradeBook_Application", "root", "Winter I_S Coming!", INSERT, strInsert );
//        //execute the query
//        insertQuery.executeQuery();
//
//    }
//
//
//    //method for inserting multiple students into the database by accepting an array list of Students
//    public void insertMultipleStudents(ArrayList<Student> student){
//        for (Student currentStudent: student) {
//            this.insertStudentIntoClass(currentStudent);
//        }
//    }

    /**
     * public method to override the toString method
     * @return returns a string format of the course
     *
     */
    @Override
    public String toString(){
        return String.format("Course: ID#%d Number:%d Name: %s", this.getCourseID(), this.getCourseNUM(), this.getCourseName());
    }

    /**
     * public Getter method
     * @return the course number as int
     */
    public int getCourseNUM() {
        return courseNUM;
    }

    /**
     * public Setter method for
     * @param courseNUM as int
     */
    public void setCourseNUM(int courseNUM) {
        this.courseNUM = courseNUM;
    }

    /**
     * public Getter method
     * @return the course Name as string
     */
    public String getCourseName() {
        return courseName;
    }

    /**
     * public Setter method
     * @param courseName as string
     */
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    /**
     * public Getter method
     * @return the student Arraylist
     */
    public ArrayList<Student> getStudents() {
        return students;
    }

    /**
     * public Setter method for the student arraylist
     * @param students arraylist
     */
    public void setStudents(ArrayList<Student> students) {
        this.students.addAll(students);
    }

    /**
     * public Getter method
     * @return the course id as int
     */
    public int getCourseID() {
        return courseID;
    }

    /**
     * public Setter method for the course id
     * @param courseID as int
     */
    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public GradeModel getGradeModel() {
        return gradeModel;
    }

    public void setGradeModel(GradeModel gradeModel) {
        this.gradeModel = gradeModel;
    }
}
