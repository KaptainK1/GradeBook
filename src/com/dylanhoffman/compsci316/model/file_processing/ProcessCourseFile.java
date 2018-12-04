package src.com.dylanhoffman.compsci316.model.file_processing;

import src.com.dylanhoffman.compsci316.model.Course;

import java.sql.SQLException;

public class ProcessCourseFile implements ProcessFile{


    @Override
    public void processFile(String[] values) throws SQLException {
        //set the id to the first column, need to use substring due to quotes
        int id = Integer.valueOf(values[0].substring(1));

        //set the num of the course to the 2nd column
        int num = Integer.valueOf(values[1]);

        //set the name to the 3rd column
        String name = values[2];

        //set the grademodel to the 4 column, use substring to remove the last quote
        String gradeModel = values[3].substring(0,values[3].length()-1);

        Course.insertCourse(id,num,name,gradeModel);
    }
}