package src.com.dylanhoffman.compsci316.model.file_processing;

import src.com.dylanhoffman.compsci316.model.Student;

import java.sql.SQLException;

public class ProcessStudentFile implements ProcessFile{

    @Override
    public void processFile(String[] values) throws SQLException {
        //set the id to the first column, need to use substring due to quotes
        int id = Integer.valueOf(values[0].substring(1));

        //set the num of the course to the 2nd column
        String firstName = values[1];

        //set the name to the 3rd column
        String lastName = values[2].substring(0,values[2].length()-1);

        Student.insertStudent(firstName,lastName,id);
    }
}
