package ua.com.foxminded.task6;


import java.io.IOException;
import java.sql.SQLException;

import ua.com.foxminded.task6.school.School;


public class SchoolApp {
    
    public static void main(String[] args) throws SQLException, IOException {
        School school = new School();
        school.refreshDatabase();
        school.generateTestData();

    }
}
