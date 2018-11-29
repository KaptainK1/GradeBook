package src.com.dylanhoffman.compsci316.model;

import src.com.dylanhoffman.compsci316.Constants;
import src.com.dylanhoffman.compsci316.logging.Log;
import src.com.dylanhoffman.compsci316.utility.Query;
import src.com.dylanhoffman.compsci316.utility.SelectStudentGrades;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;

import static src.com.dylanhoffman.compsci316.utility.QueryType.DELETE;
import static src.com.dylanhoffman.compsci316.utility.QueryType.INSERT;

/**
 * Student Class for creating a student object to be used with
 * the GradeItem Class and the Course Class.
 * Contains 3 private variables
 * String firstName to hold the Student's first name
 * String lastName to hold the Student's last name
 * int studentID to hold the Student's ID
 *
 */
public class Student {
    private final String firstName;
    private final String lastName;
    private final int studentID;
    /**
     * Constructor that accepts 3 parameters
     * @param firstName String firstName to hold the Student's first name
     * @param lastName String lastName to hold the Student's last name
     * @param studentID int studentID to hold the Student's ID
     */
    public Student(String firstName, String lastName, int studentID){

        if (firstName.equals("")){
            Log.writeToLog(Constants.getLogPath(), "First name cannot be blank");
            throw new IllegalArgumentException("First name cannot be blank");
        }
        if (lastName.equals("")) {
            Log.writeToLog(Constants.getLogPath(), "Last name cannot be blank");
            throw new IllegalArgumentException("Last name cannot be blank");
        }
        //check to see if the student id is 8 digits
        if (!isValidStudentID(studentID,8)) {
            Log.writeToLog(Constants.getLogPath(), "Invalid student number");
            throw new IllegalArgumentException("Invalid student number");
        }

        this.firstName=firstName;
        this.lastName=lastName;
        this.studentID=studentID;

    }

    /**
     * private method to check if the student ID is a valid length
     * to be used within the constructor
     * @param id is the student id to check
     * @param length is the length the id is required to be
     * @return returns true if the calculation matches the length specified
     */
    //method for testing if the entered student id is the required length
    private boolean isValidStudentID(int id, int length){
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
     * method for inserting multiple grade items for a single course into the Database
     * makes use of the insertGradeItem method
     * accepts 2 parameters
     * @param gradeItems an array of GradeItem objects
     * @param course a course that the gradeitem is for
     */
    public void insertMultipleGradeItems(ArrayList<GradeItem> gradeItems, Course course) throws SQLException{
        for (GradeItem currentGradeItem: gradeItems) {
            //insert the grade item into the db
            insertGradeItem(currentGradeItem, course);
        }
    }

    /**
     * method for inserting a single grade item into a single course into the Database
     * accepts two parameters
     * @param gradeItem an GradeItem object to be inserted
     * @param course the course object the gradeitem is for
     */
    public void insertGradeItem(GradeItem gradeItem, Course course) throws SQLException {
        //first, run the course's grade method to assign a grade letter to the grade item
        course.grade(gradeItem);

        String strInsert = "into GradeItems VALUES ( " + gradeItem.getTotalCorrect() + "," + gradeItem.getTotalPossible() + ", '" +
                gradeItem.printLetterGrade() + "', " + "NULL, " + getStudentID() + ", " + course.getCourseID() + ", '" + gradeItem.getGradeName() + "' )";

        Query insertQuery = new Query(Constants.getDbName(), Constants.getDbUsername(), Constants.getDbPassword(), INSERT, strInsert );
        insertQuery.executeQueryThrows();
    }

    /**
     * method for inserting a single student into the Database
     */
    //method for inserting a single student into the current course
    public void insertStudent() throws SQLException{
        //build the insert query
        String strInsert = "into Students VALUES ( " + "'" + getFirstName() + "' , '" + getLastName() + "' , " + getStudentID() + " )";

        //create the query object
        Query insertQuery = new Query(Constants.getDbName(), Constants.getDbUsername(), Constants.getDbPassword(), INSERT, strInsert );
        //execute the query
        insertQuery.executeQueryThrows();
    }

    /**
     * method for inserting a single student into the Database
     */
    //method for inserting a single student into the current course
    public static void insertStudent(String firstName, String lastName, int studentID) throws SQLException {
        //build the insert query
        String strInsert = "into Students VALUES ( " + "'" + firstName + "' , '" + lastName + "' , " + studentID + " )";

        //create the query object
        Query insertQuery = new Query(Constants.getDbName(), Constants.getDbUsername(), Constants.getDbPassword(), INSERT, strInsert );
        //execute the query
        insertQuery.executeQueryThrows();
    }


    public static void insertGradeItem(Student student, GradeItem gradeItem, Course course) throws SQLException{

        course.grade(gradeItem);

        String strInsert = "into GradeItems VALUES ( " + gradeItem.getTotalCorrect() + "," + gradeItem.getTotalPossible() + ", '" +
                gradeItem.printLetterGrade() + "', " + "NULL, " + student.getStudentID() + ", " + course.getCourseID() + ", '" + gradeItem.getGradeName() + "' )";

        Query insertQuery = new Query(Constants.getDbName(), Constants.getDbUsername(), Constants.getDbPassword(), INSERT, strInsert );
        insertQuery.executeQueryThrows();

    }

    public static String[][] searchStudents(){

        String query = "Select FirstName, LastName, StudentID From Students";

        SelectStudentGrades selectStudentGrades = new SelectStudentGrades(Constants.getDbName(), Constants.getDbUsername(), Constants.getDbPassword());
        return selectStudentGrades.returnArrayOfDataFromQuery(query);

    }

    public static String[][] searchGradeItem(Student student, Course course){

        String query = "Select TotalCorrect, TotalPossible, GradeLetter, Name FROM GradeItems" +
                " WHERE courseID = " + course.getCourseID() +
                " AND studentID = " + student.getStudentID();

        SelectStudentGrades selectStudentGrades = new SelectStudentGrades(Constants.getDbName(), Constants.getDbUsername(), Constants.getDbPassword());
        return selectStudentGrades.returnArrayOfDataFromQuery(query);

    }



    /**
     * public method for inserting multiple students into the Database
     * makes us of method insertStudent
     * @param student accepts the parameter of 1 student ArrayList
     */
    public void insertMultipleStudents(ArrayList<Student> student) throws SQLException{
        for (Student currentStudent: student) {
            currentStudent.insertStudent();
        }
    }

    /**
     * public method to delete all gradeitems from a course for a student
     * @param course the course object
     */
    public void deleteAllGradeItemsInCourse(Course course) throws SQLException{

        String strDelete = "from GradeItems WHERE studentID = " + this.studentID + " AND courseID = " + course.getCourseID();
        Query deleteQuery = new Query(Constants.getDbName(), Constants.getDbUsername(), Constants.getDbPassword(), DELETE, strDelete );
        deleteQuery.executeQueryThrows();
    }

    /**
     * public static method to delete all grade items for a student
     */
    public static void deleteAllGradeItemsForStudent(int studentID) throws SQLException{
        String strDelete = "from GradeItems WHERE studentID = " + studentID;
        Query deleteQuery = new Query(Constants.getDbName(), Constants.getDbUsername(), Constants.getDbPassword(), DELETE, strDelete );
        deleteQuery.executeQueryThrows();
    }

    /**
     * public static method to delete all grade items from a course
     * accepts 2 parameters
     * @param studentID the id of the student to delete all the grade items
     * @param courseID the course to delete the student grade items
     */
    public static void deleteAllGradeItemsInCourse(int studentID, int courseID) throws SQLException{

        String strDelete = "from GradeItems WHERE studentID =" + studentID + " AND courseID = " + courseID;
        Query deleteQuery = new Query(Constants.getDbName(), Constants.getDbUsername(), Constants.getDbPassword(), DELETE, strDelete );
        deleteQuery.executeQueryThrows();
    }

    /**
     * public static method to delete a single grade item from a course for a student
     * overloaded method that accepts 3 parameters
     * @param studentID the id of the student
     * @param courseID the id of the course
     * @param nameOfGradeItem the name of the gradeitem
     */
    public static void deleteSingleGradeItem(int studentID, int courseID, String nameOfGradeItem, int totalCorrect, int totalPossible) throws SQLException{
        //build the query
        String strDelete = " FROM GradeItems WHERE TotalCorrect = " + totalCorrect + " AND TotalPossible = " + totalPossible +
                " AND NAME LIKE %'" + nameOfGradeItem + "'% AND CourseID = " + courseID +
                " AND StudentID = " + studentID;

        //create the query object
        Query deleteQuery = new Query(Constants.getDbName(), Constants.getDbUsername(), Constants.getDbPassword(), DELETE, strDelete);

        //execute the query
        deleteQuery.executeQueryThrows();
    }

    /**
     * public method to delete a single grade item from a course for a student
     * overloaded method that accepts 2 parameters
     * @param course the course object
     * @param gradeItem the gradeitem object to be deleted
     */
    public void deleteSingleGradeItem(Course course, GradeItem gradeItem) throws SQLException{
        //build the query
        String strDelete = "from GradeItems WHERE studentID =" + this.studentID + " AND courseID = " + course.getCourseID() + " AND GradeName = " + gradeItem.getGradeName();

        //create the query object
        Query deleteQuery = new Query(Constants.getDbName(), Constants.getDbUsername(), Constants.getDbPassword(), DELETE, strDelete );

        //execute the query
        deleteQuery.executeQueryThrows();
    }

    /**
     * public static method to delete a student from the Database
     * method also deletes all gradeitems for the student
     */
    public static void deleteStudent(int studentID) throws SQLException{
        //first delete all grade items for the student
//        deleteAllGradeItemsForStudent(studentID);

        //build the query
        String strDelete = "from Students WHERE studentID = " + studentID;

        //create the query object
        Query deleteQuery = new Query(Constants.getDbName(), Constants.getDbUsername(), Constants.getDbPassword(), DELETE, strDelete );

        //execute the query
        deleteQuery.executeQueryThrows();
    }

    /**
     * overridden method toString
     * @return returns the string representation of a student object
     * @return in the format studentID, firstname, lastname
     */
    @Override
    public String toString(){
        return String.format("Student ID#%d Name: %s %s", this.getStudentID(), this.getFirstName(), this.getLastName());
    }

    /**
     * public getter method for first name
     * @return returns the first name as string
     */
    //Getters and Setters
    public String getFirstName() {
        return firstName;
    }

    /**
     * public getter method for last name
     * @return returns the last name as string
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * public getter method for the student id
     * @return
     */
    public int getStudentID() {
        return studentID;
    }

}