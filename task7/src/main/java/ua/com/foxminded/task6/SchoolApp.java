package ua.com.foxminded.task6;


import java.io.IOException;
import java.sql.SQLException;

import ua.com.foxminded.task6.school.SchoolDao;


public class SchoolApp {
    
    public static void main(String[] args) throws SQLException, IOException {
        SchoolDao school = new SchoolDao();
        //school.addNewStudentInDB("Viktor", "Prokhorenko");
        System.out.println(school.printTableOfStudents());
    }
}
