package src.com.dylanhoffman.compsci316.utility;
import src.com.dylanhoffman.compsci316.Constants;
import src.com.dylanhoffman.compsci316.logging.Log;
import src.com.dylanhoffman.compsci316.model.Course;
import src.com.dylanhoffman.compsci316.model.GradeItem;
import src.com.dylanhoffman.compsci316.model.Student;
import src.com.dylanhoffman.compsci316.model.grading.ElementaryGradeModel;
import src.com.dylanhoffman.compsci316.model.grading.GradeModel;

import java.sql.*;
import java.util.ArrayList;

/**
 * Class for holding a select students query to be used:
 * to either select all students in a course via the executeStudentQuery
 * or select all student grades in a course via executeStudentCorrectQuery
 */
public class SelectStudentGrades {

    private String url;
    private String dbName;
    private String dbUser;
    private String dbPass;
    //overloaded method that does not include the url connection object passed as a parm, so the default is localhost

    /**
     * Constructor initialized with below params
     * The url for the database is initialized to the local host
     * @param dbName to hold the database name
     * @param dbUser to hold the database username
     * @param dbPass to hold the database password
     */
    public SelectStudentGrades(String dbName,String dbUser, String dbPass) {
        this.dbName = dbName;
        this.dbUser = dbUser;
        this.dbPass = dbPass;
        //default url for localhost, run setter method to update as necessary
        this.url = ("jdbc:mysql://localhost:3306/");
    }

    /**
     *  method to run a select query on the GradeItems table and return all
     *  results with the passed in student id from the passed in course id
     *  column to be returned is either the totalCorrect column or the totalPossible column which must be specified
     * @param studentID to hold the student's id, a unique identifier
     * @param courseID to hold the course's id, a unique identifier
     * @param dbColumn to hold the the database column to retrieve
     * @return returns the value of all the rows in the column param
     */
    public int executeStudentCorrectQuery(int studentID, int courseID, String dbColumn){
//        String query = (new StringBuilder().append(getQueryType().toString()).append(" ").append(getStrSelect())).toString();
        String query = "SELECT " + dbColumn +  " from GradeItems where StudentID = " + studentID + " AND CourseID = " + courseID;
        int rowCount = 0;
        int total = 0;

        try (
                //create the connection object, then create the statement object
                Connection connection = DriverManager.getConnection( getUrl() + getDbName() + "?useSSL=false&serverTimezone=UTC", getDbUser(), getDbPass());
                Statement statement = connection.prepareStatement(query)
        ) {

            System.out.println("SQL Select Statement is " + query);
            ResultSet result = statement.executeQuery(query);
            ResultSetMetaData resultDataSet = result.getMetaData();
            int columns = resultDataSet.getColumnCount();

            while(result.next()) {   // Move the cursor to the next row, return false if no more rows
                for (int i = 1; i <= columns; i++) {
                    //print out each cell value for all columns and rows
                        total+=result.getInt(dbColumn);
                    }
                }
                ++rowCount;

            System.out.println("Total number of records = " + rowCount);
        }
        catch (SQLException ex){
            Log.writeToLog(Constants.getLogPath(), ex.getMessage());
            ex.printStackTrace();
        }
        System.out.println(total);
        return total;
    }

    /**
     * Method for adding all students in a given course id
     * then returns the arraylist of students
     * @param course object to hold the course object for all the students you wish to grade
     * @return returns an arraylist of student objects
     */
    public ArrayList<Student> executeStudentQuery(Course course){
//        String query = (new StringBuilder().append(getQueryType().toString()).append(" ").append(getStrSelect())).toString();
//        String query = "SELECT * from Students where CourseID = " + course.getCourseID();

        String query = "SELECT DISTINCT s.firstname, s.lastname, s.studentid, c.coursename FROM gradeitems " +
        "INNER JOIN courses AS c ON c.courseid = gradeitems.courseid " +
        "INNER JOIN students AS s ON s.studentid = gradeitems.studentid " +
        "WHERE c.courseid = " + course.getCourseID();

        int rowCount = 1;
        ArrayList<Student> allStudents = new ArrayList<>();
        String firstName;
        String lastName;
        int studentID;

        try (
                //create the connection object, then create the statement object
                Connection connection = DriverManager.getConnection( getUrl() + getDbName() + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true", getDbUser(), getDbPass());
                Statement statement = connection.prepareStatement(query)
        ) {

            System.out.println("SQL Select Statement is " + query);
            ResultSet result = statement.executeQuery(query);
            ResultSetMetaData resultDataSet = result.getMetaData();
            int columns = resultDataSet.getColumnCount();

            System.out.println("The records selected are:");

            while(result.next()) {   // Move the cursor to the next row, return false if no more rows
//                for (int i = 1; i <= columns; i++) {
                    //print out each cell value for all columns and rows
                    firstName=result.getString("FirstName");
                    lastName=result.getString("LastName");
                    studentID=result.getInt("StudentID");
                    allStudents.add(new Student(firstName,lastName,studentID));
//                }
            }
            ++rowCount;

            System.out.println("Total number of records = " + rowCount);
        }
        catch (SQLException ex){
            Log.writeToLog(Constants.getLogPath(), ex.getMessage());
            ex.printStackTrace();
        }

        return allStudents;
    }

    public ArrayList<Course> executeCourseQuery(){
//        String query = (new StringBuilder().append(getQueryType().toString()).append(" ").append(getStrSelect())).toString();
//        String query = "SELECT * from Students where CourseID = " + course.getCourseID();

        String query = "SELECT CourseID, CourseNum, CourseName, CourseGradeModel FROM Courses";

        int rowCount = 1;
        ArrayList<Course> allCourses = new ArrayList<>();
        int courseid;
        int courseNum;
        String courseName;
        String courseGradeModel;

        try (
                //create the connection object, then create the statement object
                Connection connection = DriverManager.getConnection( getUrl() + getDbName() + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true", getDbUser(), getDbPass());
                Statement statement = connection.prepareStatement(query)
        ) {

            System.out.println("SQL Select Statement is " + query);
            ResultSet result = statement.executeQuery(query);
            ResultSetMetaData resultDataSet = result.getMetaData();
            int columns = resultDataSet.getColumnCount();

            System.out.println("The records selected are:");

            while(result.next()) {   // Move the cursor to the next row, return false if no more rows
//                for (int i = 1; i <= columns; i++) {
                //print out each cell value for all columns and rows
                courseid=result.getInt("CourseID");
                courseNum=result.getInt("CourseNum");
                courseName=result.getString("CourseName");
                courseGradeModel=result.getString("CourseGradeModel");
                allCourses.add(new Course(courseid,courseNum,courseName,Course.convertGradeModel(courseGradeModel)));
//                }
            }
            ++rowCount;

            System.out.println("Total number of records = " + rowCount);
        }
        catch (SQLException ex){
            Log.writeToLog(Constants.getLogPath(), ex.getMessage());
            ex.printStackTrace();
        }

        return allCourses;
    }

    /**
     * Public method for executing a query and building a grid of the data in columns and rows
     * @param query the query as a string to be used to retrieve the data
     * @return returns a String array in the format of [columns][rows]
     */
    public String[][] returnArrayOfDataFromQuery(String query){

        int rowCount = 0;
        String array[][] = new String[0][0];

        try (
                //create the connection object, then create the statement object
                Connection connection = DriverManager.getConnection( getUrl() + getDbName() + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true", getDbUser(), getDbPass());
                Statement statement = connection.prepareStatement(query)
        ) {

            //create the result set and the metadata set based on the query
            System.out.println("SQL Select Statement is " + query);
            ResultSet result = statement.executeQuery(query);
            ResultSetMetaData resultDataSet = result.getMetaData();

            int columns = resultDataSet.getColumnCount();

            //move the result to the last row to get the row number
            result.last();

            //set the row variable to the row number plus 1 (need to add 1 for the header row)
            int rows = result.getRow() + 1;

            //move to the first row now
            result.beforeFirst();

            //set the 2 deminsonal array to the size of the columns and the rows (columns is the outer number)
            array = new String[columns][rows];

            //loop through each column, and set the first row to the column header name (needs to be column plus 1 as sql starts at 1 not 0)
            for (int i = 0; i < array.length; i++) {
                array[i][0] = resultDataSet.getColumnName(i + 1);
            }

            System.out.println("The records selected are:");
            //counter variable to hold what row we are on
            int rowCounter = 1;

            while(result.next()) {   // Move the cursor to the next row, return false if no more rows
                //print out each cell value for all columns and rows
                for (int i = 1; i <= array.length; i++) {
                    //column needs to be i minus 1 since we set i to 1
                    //row needs to be the row counter plus 1 since we need to skip the header row
                    array[i-1][rowCounter] = String.valueOf(result.getObject(resultDataSet.getColumnName(i)));
                }
                rowCounter++;
            }
            ++rowCount;

            System.out.println("Total number of records = " + rowCount);
        }
        catch (SQLException ex){
            Log.writeToLog(Constants.getLogPath(), ex.getMessage());
            ex.printStackTrace();
        }

        return array;
    }

    /**
     *
     * @param url sets the url to the passed in string
     */
    public void setUrl(String url){
        this.url = url;
    }

    /**
     *
     * @return returns the private string url
     */
    public String getUrl() {
        return url;
    }

    /**
     *
     * @return returns the private string db name
     */
    public String getDbName() {
        return dbName;
    }

    /**
     *
     * @param dbName sets the db name to the passed in string
     */
    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    /**
     *
     * @return returns the string for the db username
     */
    public String getDbUser() {
        return dbUser;
    }

    /**
     *
     * @param dbUser sets the db user to the passed in string
     */
    public void setDbUser(String dbUser) {
        this.dbUser = dbUser;
    }

    /**
     *
     * @return returns the string for the db password
     */
    public String getDbPass() {
        return dbPass;
    }

    /**
     *
     * @param dbPass sets the db password to the passed in string
     */
    public void setDbPass(String dbPass) {
        this.dbPass = dbPass;
    }

}