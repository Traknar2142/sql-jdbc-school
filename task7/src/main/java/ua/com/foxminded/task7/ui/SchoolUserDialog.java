package ua.com.foxminded.task7.ui;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.commons.lang3.StringUtils;

import ua.com.foxminded.task7.dao.SchoolDao;
import ua.com.foxminded.task7.dao.SingleConnection;

public class SchoolUserDialog {
    private static final String WELCOME_MESSAGE = "Write \"a\" if you want find all groups with less or equals student count\n"
            + "Write \"b\" if you want find all students related to course with given name\n"
            + "Write \"c\" if you want add new student\n" + "Write \"d\" if you want delete student by STUDENT_ID\n"
            + "Write \"e\" if you want add a student to the course (from a list)\n"
            + "Write \"f\" if you want remove the student from one of his or her courses\n";
    SingleConnection singleConnection = SingleConnection.getInstance();
    
    private SchoolDao dao = new SchoolDao(singleConnection);
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private Map<String, Callable<String>> mainMenu = new HashMap<String, Callable<String>>();

    {
        mainMenu.put("a", () -> printGroupsDataDialog());
        mainMenu.put("b", () -> printStudentsRelatedToCourseDialog());
        mainMenu.put("c", () -> insertStudentDataDialog());
        mainMenu.put("d", () -> deleteStudentDialog());
        mainMenu.put("e", () -> addStudentToCourseDialog());
        mainMenu.put("f", () -> removeStudentFromCourseDialog());
    }

    public void getAppMenu() throws Exception {
        String choice = "";
        do {
            System.out.println(WELCOME_MESSAGE);
            choice = reader.readLine();

            if (!mainMenu.containsKey(choice)) {
                continue;
            }

            System.out.println(mainMenu.get(choice).call());

            System.out.println("Would you like to continue? (Write \"n\" if you want finish)");
            choice = reader.readLine();

        } while (!choice.equals("n"));
    }

    private String printGroupsDataDialog() throws SQLException, IOException {
        System.out.print("Please, enter students count: ");

        String studentCount = reader.readLine();

        return printGroupsData(studentCount);
    }

    private String printStudentsRelatedToCourseDialog() throws SQLException, IOException {
        System.out.println(printCourses());
        System.out.print("Please, enter name of course: ");

        String courseName = reader.readLine();

        return printStudentsRelatedToCourse(courseName);
    }

    private String insertStudentDataDialog() throws SQLException, IOException {
        System.out.print("Please, entrer first name: ");
        String firstName = reader.readLine();

        System.out.print("Please, entrer last name: ");
        String lastName = reader.readLine();

        return checkIdForInsertStudentData(firstName, lastName);
    }

    private String deleteStudentDialog() throws SQLException, IOException {
        System.out.print("List of students:\n" + printTableOfStudents() + "\n" + "Enter student ID: ");
        String id = reader.readLine();

        return checkIdForDeleteStudentData(id);
    }

    private String addStudentToCourseDialog() throws SQLException, IOException {
        System.out.print("List of students:\n" + printTableOfStudents() + "\n" + "List of courses:\n" + printCourses());

        System.out.print("Enter student ID: ");
        String id = reader.readLine();

        System.out.print("Enter name of course:  ");
        String courseName = reader.readLine();

        return checkIdForAddStidentToCourse(id, courseName);
    }

    private String removeStudentFromCourseDialog() throws IOException, SQLException {
        System.out.print("List of students:\n" + printTableOfStudents());
        System.out.print("Enter student ID: ");
        String id = reader.readLine();

        System.out.print("Enter name of course:  ");
        String courseName = reader.readLine();

        return checkIdForRemoveStudentFromCourse(id, courseName);
    }

    private String printGroupsData(String countOfStudent) throws SQLException, IOException {
        String tableHeader = (String.format("%-15s", "GROUP:") + String.format("%-15s", "STUDENTS_COUNT:") + "\n");
        int headerValuePosition = 0;
        if (!StringUtils.isNumeric(countOfStudent) || StringUtils.isEmpty(countOfStudent)) {
            return "Please, enter numbers";
        }

        StringBuilder result = dao.findGroups(countOfStudent);

        if (result.toString().equals("")) {
            return ("No data");
        }

        result.insert(headerValuePosition, tableHeader);
        return result.toString();
    }
    
    private String printStudentsRelatedToCourse(String courseName) throws SQLException, IOException {
        String tableHeader = (String.format("%-15s", "FIRST_NAME:") + String.format("%-15s", "LAST_NAME:") + "\n");
        int headerValuePosition = 0;
        if (!StringUtils.isAlpha(courseName)) {
            return "You should enter the name of the course";
        }

        StringBuilder result = dao.findStudentsRelatedToCourse(courseName);

        if (result.toString().equals("")) {
            return ("No data");
        }

        result.insert(headerValuePosition, tableHeader);
        return result.toString();
    }

    private String printCourses() throws SQLException, IOException {
        String tableHeader = "List of courses:\n";
        StringBuilder result = dao.findCourses();
        int headerPosition = 0;

        if (result.toString().equals("")) {
            return ("No data");
        }

        result.insert(headerPosition, tableHeader);
        return result.toString();
    }

    private String checkIdForInsertStudentData(String firstName, String lastName) throws SQLException, IOException {
        if (!StringUtils.isAlpha(firstName) || !StringUtils.isAlpha(lastName)) {
            return "Please, enter correct data \n" + "You should enter first name and last name in letters";
        }
        dao.insertStudent(firstName, lastName);
        return "Student has been added";
    }

    private String printTableOfStudents() throws SQLException, IOException {
        String tableHeader = (String.format("%-5s", "ID:") + String.format("%-15s", "FIRST_NAME:")
                + String.format("%-10s", "LAST_NAME:") + "\n");
        StringBuilder result = dao.findAllStudents();

        if (result.toString().equals("")) {
            return ("No data");
        }

        result.insert(0, tableHeader);
        return result.toString();
    }

    private String checkIdForDeleteStudentData(String id) throws SQLException, IOException {
        if (!StringUtils.isNumeric(id) || StringUtils.isEmpty(id)) {
            return "You should enter ID of student";
        }

        int studentId = Integer.parseInt(id);

        if (!dao.checkStudentInDB(studentId)) {
            return "Student not found";
        }

        dao.deleteStudent(studentId);

        return "Student deleted";
    }

    private String checkIdForAddStidentToCourse(String id, String courseName) throws SQLException, IOException {
        if (!StringUtils.isNumeric(id) || StringUtils.isEmpty(id)) {
            return "You should enter ID of student";
        }

        if (!StringUtils.isAlpha(courseName)) {
            return "You should enter the name of the course";
        }

        int studentId = Integer.parseInt(id);

        if (!dao.checkStudentInDB(studentId)) {
            return "Student not found";
        }

        if (!dao.checkCourseInDB(courseName)) {
            return "Group not found";
        }

        dao.addStudentToCourse(studentId, courseName);

        return "Student added to the course\n";
    }

    private String checkIdForRemoveStudentFromCourse(String id, String courseName) throws SQLException, IOException {
        if (!StringUtils.isNumeric(id) || StringUtils.isEmpty(id)) {
            return "You should enter ID of student";
        }

        if (!StringUtils.isAlpha(courseName)) {
            return "You should enter the name of the course";
        }

        int studentId = Integer.parseInt(id);

        if (!dao.checkStudentInDB(studentId)) {
            return "Student not found";
        }

        if (!dao.checkCourseInDB(courseName)) {
            return "Group not found";
        }

        if (!dao.checkStudentCourseRelation(studentId, courseName)) {
            return "Student not assigned to course " + courseName;
        }

        dao.removeFromCourse(studentId, courseName);

        return "Student removed from a course";
    }
}
