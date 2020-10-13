package ua.com.foxminded.task7.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.IllegalFormatException;

import org.apache.commons.lang3.StringUtils;

import ua.com.foxminded.task7.school.Student;
import ua.com.foxminded.task7.school.TestData;

public class SchoolDao {
    DaoFactory daoFactory = DaoFactory.getInstance();
    private static final String TAB = "%-15s";

    public SchoolDao() throws IOException {
        TestData testData = new TestData();
        testData.refreshDataBase();
        testData.generateTestData();
    }

    public void addNewStudentIntoDB(String firstName, String lastName) {
        if (!StringUtils.isAlpha(firstName) || !StringUtils.isAlpha(lastName)) {
            System.out.println("Please, enter correct data \n" + "You should enter first name and last name in letters");
            return;
        }
        String insertQuery = "insert into school.t_students (student_id, group_id, first_name, last_name) values(?,?,?,?)";
        try (Connection connection = daoFactory.getConnection();
                Statement statement = connection.createStatement();
                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);) {
            Student student = new Student(firstName, lastName);
            preparedStatement.setInt(1, student.getStudentId());
            preparedStatement.setInt(2, student.getGroupId());
            preparedStatement.setString(3, student.getFirstName());
            preparedStatement.setString(4, student.getLastName());
            preparedStatement.executeUpdate();
            System.out.println("Student has been added");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String findeGroups(String countOfStudent) throws SQLException {
        if (!StringUtils.isNumeric(countOfStudent) || StringUtils.isEmpty(countOfStudent)) {
            return "Please, enter numbers";
        }
        int studentCount = Integer.parseInt(countOfStudent);
        String insertQuery = "select group_name, COUNT(*) as f_count "
                + "FROM (select * from school.t_groups left join school.t_students on school.t_groups.group_id = school.t_students.group_id) as t_join "
                + "group by group_name having COUNT(*) <= ? ";
        StringBuilder result = new StringBuilder(
                String.format(TAB, "GROUP:") + String.format(TAB, "STUDENTS_COUNT:") + "\n");
        String line = "";
        try (Connection connection = daoFactory.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setInt(1, studentCount);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                return ("No data");
            }
            while (resultSet.next()) {
                String groupName = resultSet.getString(1);
                int studentsCount = resultSet.getInt(2);
                line = String.format(TAB, groupName) + String.format(TAB, studentsCount) + "\n";
                result.append(line);
            }
            resultSet.close();
            return result.toString();
        }
    }

    public String findStudents(String courseName) throws SQLException {
        if (!StringUtils.isAlpha(courseName)) {
            return "You should enter the name of the course";
        }
        String insertQuery = "select first_name, last_name from (" + "select course_name, student_id "
                + "from school.t_courses join school.t_courses_students on school.t_courses.course_id = school.t_courses_students.course_id "
                + "where course_name = ? ) as _course_data left join school.t_students on _course_data.student_id = t_students.student_id";
        StringBuilder result = new StringBuilder(
                String.format(TAB, "FIRST_NAME:") + String.format(TAB, "LAST_NAME:") + "\n");
        String line = "";
        try (Connection connection = daoFactory.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, courseName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                return ("No data");
            }
            while (resultSet.next()) {
                String firstName = resultSet.getString(1);
                String lastName = resultSet.getString(2);
                line = String.format(TAB, firstName) + String.format(TAB, lastName) + "\n";
                result.append(line);
            }
            resultSet.close();
            return result.toString();
        }
    }

    public String deleteStudent(String id) throws SQLException {
        if (!StringUtils.isNumeric(id) || StringUtils.isEmpty(id)) {
            return "You should enter ID of student";
        }
        int studentId = Integer.parseInt(id);
        if (!checkStudentInDB(studentId)) {
            return "Student not found";
        }
        String insertQuery = "delete from school.t_students where student_id = ?";
        try (Connection connection = daoFactory.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setInt(1, studentId);
            preparedStatement.executeUpdate();
            return "Student deleted";
        }
    }

    public String addStudentToCourse(String id, String courseName) throws SQLException {
        if (!StringUtils.isNumeric(id) || StringUtils.isEmpty(id)) {
            return "You should enter ID of student";
        }
        if (!StringUtils.isAlpha(courseName)) {
            return "You should enter the name of the course";
        }

        int studentId = Integer.parseInt(id);

        if (!checkStudentInDB(studentId)) {
            return "Student not found";
        }
        if (!checkCourseInDB(courseName)) {
            return "Group not found";
        }

        String insertQuery = "insert into school.t_courses_students (student_id, course_id)"
                + "values(?,(select course_id from school.t_courses where course_name = ?))";
        try (Connection connection = daoFactory.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setInt(1, studentId);
            preparedStatement.setString(2, courseName);
            preparedStatement.executeUpdate();
        }
        return "Student added to a course\n" + printOneStudentCorses(studentId);
    }

    public String removeFromCourse(String id, String courseName) throws SQLException {
        if (!StringUtils.isNumeric(id) || StringUtils.isEmpty(id)) {
            return "You should enter ID of student";
        }
        if (!StringUtils.isAlpha(courseName)) {
            return "You should enter the name of the course";
        }

        int studentId = Integer.parseInt(id);

        if (!checkStudentInDB(studentId)) {
            return "Student not found";
        }
        if (!checkCourseInDB(courseName)) {
            return "Group not found";
        }
        if (!checkOneStudentCourse(studentId, courseName)) {
            return "Student not assigned to group " + courseName;
        }

        String insertQuery = "delete from school.t_courses_students "
                + "where student_id = ? and course_id = (select course_id from school.t_courses where  course_name = ?)";
        try (Connection connection = daoFactory.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setInt(1, studentId);
            preparedStatement.setString(2, courseName);
            preparedStatement.executeUpdate();
        }
        return "Student removed from a course" + printOneStudentCorses(studentId);
    }

    private String printOneStudentCorses(int studentId) throws SQLException {
        String insertQuery = "select distinct course_name from("
                + "select * from school.t_students left join school.t_courses_students on school.t_students.student_id = school.t_courses_students.student_id "
                + "where t_students.student_id = ?)"
                + "as _student_data left join school.t_courses on _student_data.course_id = t_courses.course_id";
        StringBuilder result = new StringBuilder("List of student's courses:\n");
        try (Connection connection = daoFactory.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setInt(1, studentId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String courseName = resultSet.getString(1);
                result.append(courseName + "\n");
            }
            resultSet.close();
            return result.toString();
        }
    }

    public String printTableOfCourses() throws SQLException {
        String insertQuery = "select * from school.t_courses";
        StringBuilder result = new StringBuilder("");
        String line = "";
        try (Connection connection = daoFactory.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String courseName = resultSet.getString(2);
                String courseDescription = resultSet.getString(3);
                line = String.format("%-5s", id) + String.format("%-15s", courseName)
                        + String.format("%-10s", courseDescription) + "\n";
                result.append(line);
            }
            resultSet.close();
            return result.toString();
        }
    }

    public String printTableOfStudents() throws SQLException {
        String insertQuery = "select student_id, first_name, last_name from school.t_students";
        StringBuilder result = new StringBuilder(String.format("%-5s", "ID:") + String.format("%-15s", "FIRST_NAME:")
                + String.format("%-10s", "LAST_NAME:") + "\n");
        String line = "";
        try (Connection connection = daoFactory.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String firstName = resultSet.getString(2);
                String lastName = resultSet.getString(3);
                line = String.format("%-5s", id) + String.format("%-15s", firstName) + String.format("%-10s", lastName)
                        + "\n";
                result.append(line);
            }
            resultSet.close();
            return result.toString();
        }
    }

    private boolean checkStudentInDB(int id) throws SQLException {
        String query = "select * from school.t_students where student_id = ?";
        try (Connection connection = daoFactory.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            }
        }
        return false;
    }

    private boolean checkCourseInDB(String courseName) throws SQLException {
        String query = "select * from school.t_courses where course_name = ?";
        try (Connection connection = daoFactory.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, courseName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            }
        }
        return false;
    }

    private boolean checkOneStudentCourse(int id, String courseName) throws SQLException {
        String query = "select distinct course_name from("
                + "select * from school.t_students left join school.t_courses_students on school.t_students.student_id = school.t_courses_students.student_id "
                + "where t_students.student_id = ?) "
                + "as _student_data left join school.t_courses on _student_data.course_id = t_courses.course_id where course_name = ?";
        try (Connection connection = daoFactory.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, courseName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            }
        }
        return false;
    }
}
