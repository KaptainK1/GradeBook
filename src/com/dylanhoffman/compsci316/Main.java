package src.com.dylanhoffman.compsci316;

import src.com.dylanhoffman.compsci316.logging.Log;
import src.com.dylanhoffman.compsci316.model.*;
import src.com.dylanhoffman.compsci316.model.grading.ElementaryGradeModel;
import src.com.dylanhoffman.compsci316.model.grading.GradeModel;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

public class Main {

    public static void main(String[] args) {
	// write your code here

//        //instantiate the grademodel to be used for grading assigments for the courses
        GradeModel gradeModel = new ElementaryGradeModel();
//
//        //instantiate two course objects
        Course englishCourse = new Course(200001, 123, "English_Composition", gradeModel);
        Course mathCourse = new Course(200002, 101, "Math_101", gradeModel);

        //insert the courses into the Database
            englishCourse.insertCourse();
            mathCourse.insertCourse();

        //instantiate a few students to go into each course

        Student sansa = new Student("Sansa", "Stark",67843219);
        Student arya = new Student("Arya", "Stark", 11111111);
        Student jon = new Student("Jon", "Snow", 12345678);

        //put the students into an array list
        ArrayList<Student> englishCourseStudents = new ArrayList<>();
        Collections.addAll(englishCourseStudents,sansa,arya,jon);

        //add the arraylist to the current student list plus the already added students if any
        englishCourse.setStudents(englishCourseStudents);

        //insert the students into the Database
        for (Student currentStudent: englishCourseStudents) {
            currentStudent.insertStudent();
        }
//
        Student luke = new Student("Luke", "Skywalker",18675309);
        Student leia = new Student("Leia", "Solo", 22222222);
        Student han = new Student("Han", "Solo", 33333333);
//
//        //put the students into an array list
        ArrayList<Student> mathCourseStudents = new ArrayList<>();
        Collections.addAll(mathCourseStudents,luke,leia,han);
//
//        //add the arraylist to the current student list plus the already added students if any
        mathCourse.setStudents(mathCourseStudents);
//
//        //insert the students into the Database
        for (Student currentStudent: mathCourseStudents) {
            currentStudent.insertStudent();
        }
//
//
        ArrayList<GradeItem> sansasGradeItems = new ArrayList<>();
            Collections.addAll(sansasGradeItems,
                    new Assignment("First_Assignment",18,20),
                    new Quiz("First_Quiz", 9,10),
                    new Exam("First_Exam", 95, 100));

        ArrayList<GradeItem> aryasGradeItems = new ArrayList<>();
        Collections.addAll(aryasGradeItems,
                new Assignment("First_Assignment",12,20),
                new Quiz("First_Quiz", 7,10),
                new Exam("First_Exam", 70, 100));

        ArrayList<GradeItem> jonsGradeItems = new ArrayList<>();
        Collections.addAll(jonsGradeItems,
                new Assignment("First_Assignment",12,20),
                new Quiz("First_Quiz", 7,10),
                new Exam("First_Exam", 70, 100));
//
//        //insert the arrays
        sansa.insertMultipleGradeItems(sansasGradeItems,englishCourse);
        arya.insertMultipleGradeItems(aryasGradeItems,englishCourse);
        jon.insertMultipleGradeItems(jonsGradeItems,englishCourse);
//
//
//        //create the grade item arrays for each student
        ArrayList<GradeItem> lukesGradeItems = new ArrayList<>();
        Collections.addAll(lukesGradeItems,
                new Assignment("Worksheet1.1",12,15),
                new Quiz("Fractions_Quiz", 8,10),
                new Exam("Chapter1_Exam", 125, 150));

        ArrayList<GradeItem> leiasGradeItems = new ArrayList<>();
        Collections.addAll(leiasGradeItems,
                new Assignment("Worksheet1.1",15,15),
                new Quiz("Fractions_Quiz", 10,10),
                new Exam("Chapter1_Exam", 150, 150));

        ArrayList<GradeItem> hansGradeItems = new ArrayList<>();
        Collections.addAll(hansGradeItems,
                new Assignment("Worksheet1.1",10,15),
                new Quiz("Fractions_Quiz", 7,10),
                new Exam("Chapter1_Exam", 100, 150));
//
//        //insert the arrays into the Database
        luke.insertMultipleGradeItems(lukesGradeItems,mathCourse);
        leia.insertMultipleGradeItems(leiasGradeItems,mathCourse);
        han.insertMultipleGradeItems(hansGradeItems,mathCourse);
//
//
//        //finally calculate all the gradeitems for each student for each course
        englishCourse.calculateAllStudents();
        mathCourse.calculateAllStudents();

        Student.deleteStudent(luke.getStudentID());

    }

}
