package src.com.dylanhoffman.compsci316;

import javafx.application.Application;
import javafx.stage.Stage;
import src.com.dylanhoffman.compsci316.UI.DisplayBox;
import src.com.dylanhoffman.compsci316.model.Course;
import src.com.dylanhoffman.compsci316.model.Student;
import src.com.dylanhoffman.compsci316.model.grading.CollegeGradeModel;
import src.com.dylanhoffman.compsci316.model.grading.GradeModel;
import src.com.dylanhoffman.compsci316.utility.SelectStudentGrades;

public class Tester extends Application {

        @Override
        public void start(Stage stage) throws Exception {
            GradeModel gradeModel = new CollegeGradeModel();
            Student student = new Student("Dylan","Hoffman",12345678);
            Course course = new Course(1,123,"Test", gradeModel);

        String query = "Select TotalCorrect, TotalPossible, GradeLetter, Name FROM GradeItems" +
                " WHERE courseID = " + course.getCourseID() +
                " AND studentID = " + student.getStudentID();

        String otherQuery = "Select FirstName, LastName, StudentID From Students";

            SelectStudentGrades selectStudentGrades = new SelectStudentGrades("GradeBook_Application", "root", "Winter I_S Coming!");
            DisplayBox gridPane = new DisplayBox();
            String array[][] = selectStudentGrades.returnArrayOfDataFromQuery(otherQuery);
            gridPane.displayGridPane(array,"test","");


        }

        public static void main(String[] args) {
            // create a Gradebook object and call its start method


            launch(args);
        }

        }
