package src.com.dylanhoffman.compsci316.model.file_processing;

import src.com.dylanhoffman.compsci316.model.GradeItem;
import src.com.dylanhoffman.compsci316.model.Student;

import java.sql.SQLException;

//public class ProcessGradeItemFile implements ProcessFile{



//    @Override
//    public void processFile(String[] values) throws SQLException {
//        //set the id to the first column, need to use substring due to quotes
//        int courseID = Integer.valueOf(values[0].substring(1));
//        int studentID = Integer.valueOf(values[1]);
//
//        String name = values[2];
//        int totalCorrect = Integer.valueOf(values[3]);
//        int totalPossible = Integer.valueOf(values[4].substring(0,values[4].length()-1));
//
//
//        //set the num of the course to the 2nd column
//        String firstName = values[1];
//
//        //set the name to the 3rd column
//        String lastName = values[2];
//
//        GradeItem.(firstName,lastName,id);
//    }
//}
