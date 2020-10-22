package ua.com.foxminded.task7;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

import ua.com.foxminded.task7.dao.SchoolDao;
import ua.com.foxminded.task7.ui.SchoolUi;


public class SchoolApp {
    private static final String WELCOME_MESSAGE = "Write \"a\" if you want find all groups with less or equals student count\n"
                                                + "Write \"b\" if you want find all students related to course with given name\n"
                                                + "Write \"c\" if you want add new student\n" 
                                                + "Write \"d\" if you want delete student by STUDENT_ID\n"
                                                + "Write \"e\" if you want add a student to the course (from a list)\n"
                                                + "Write \"f\" if you want remove the student from one of his or her courses\n";
    
    public static void main(String[] args) throws IOException  {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        SchoolDao school = new SchoolDao();
        SchoolUi ui = new SchoolUi();
        try {
            school.generateTestData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String choice = "";
        do {
            System.out.println(WELCOME_MESSAGE);
            choice = reader.readLine();
            try {
                if (choice.equals("a")) {
                    System.out.println(ui.printGroupsDataDialog());
                }

                if (choice.equals("b")) {
                    System.out.println(ui.printStudentsRelatedToCourseDialog());
                }

                if (choice.equals("c")) {
                    System.out.println(ui.insertStudentDataDialog());
                }

                if (choice.equals("d")) {
                    System.out.println(ui.deleteStudentDialog());
                }

                if (choice.equals("e")) {
                    System.out.println(ui.addStudentToCourseDialog());
                }

                if (choice.equals("f")) {
                    System.out.println(ui.removeStudentFromCourseDialog());
                }
            } catch (SQLException | FileNotFoundException e) {
                e.printStackTrace();
            }

            System.out.println("Would you like to continue? (Write \"n\" if you want finish)");
            choice = reader.readLine();
        } while (!choice.equals("n"));

    }
}
