package ua.com.foxminded.task7;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

import ua.com.foxminded.task7.dao.SchoolDao;


public class SchoolApp {
    
    public static void main(String[] args) throws SQLException, IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        SchoolDao school = new SchoolDao();
        String choice = "";
        do {
            System.out.println("Write \"a\" if you want find all groups with less or equals student count\n"
                    + "Write \"b\" if you want find all students related to course with given name\n"
                    + "Write \"c\" if you want add new student\n"
                    + "Write \"d\" if you want delete student by STUDENT_ID\n"
                    + "Write \"e\" if you want add a student to the course (from a list)\n"
                    + "Write \"f\" if you want remove the student from one of his or her courses\n");
            switch (reader.readLine()) {
            case "a": {
                System.out.print("Please, enter students count: ");
                String studentCount = reader.readLine();
                System.out.println(school.findeGroups(studentCount));
                break;
            }
            case "b": {
                System.out.println("list of courses:\n" + school.printTableOfCourses());
                System.out.print("Please, enter name of course: ");
                String courseName = reader.readLine();
                System.out.println(school.findStudents(courseName));
                break;
            }
            case "c": {
                System.out.print("Please, entrer first name: ");
                String firstName = reader.readLine();
                System.out.print("Please, entrer last name: ");
                String lastName = reader.readLine();
                school.addNewStudentIntoDB(firstName, lastName);
                break;
            }
            case "d": {
                System.out.print("List of students:\n" + school.printTableOfStudents() + "\n" + "Enter student ID: ");
                String id = reader.readLine();
                System.out.println(school.deleteStudent(id));                
                break;                
            }
            case "e": {
                System.out.print("List of students:\n" + school.printTableOfStudents() + "\n" + 
                "List of courses:\n" + school.printTableOfCourses());
                System.out.print("Enter student ID: ");
                String id = reader.readLine();
                System.out.print("Enter name of course:  ");
                String courseName = reader.readLine();
                System.out.println(school.addStudentToCourse(id, courseName));
                break;
            }
            case "f": {
                System.out.print("List of students:\n" + school.printTableOfStudents());
                System.out.print("Enter student ID: ");
                String id = reader.readLine();
                System.out.print("Enter name of course:  ");
                String courseName = reader.readLine();
                System.out.println(school.removeFromCourse(id, courseName));
                break;
            }
            default:
                break;
            }
            System.out.println("Would you like to continue? (Write \"n\" if you want finish)");
            choice = reader.readLine();
        } while (!choice.equals("n"));

    }
}
