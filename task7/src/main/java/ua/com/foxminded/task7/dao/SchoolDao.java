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
    private SingleConnection singleConnection = SingleConnection.getInstance();

    public void generateTestData() throws IOException, SQLException {
        TestData testData = new TestData();
        testData.refreshDataBase();
        testData.generateTestData();
    }
    
    public StringBuilder findGroups(String countOfStudent) throws SQLException, IOException {
        int studentCount = Integer.parseInt(countOfStudent);
        StringBuilder result = new StringBuilder();
        String insertQuery =                 
                "SELECT\n" + 
                "    group_name\n" + 
                "  , COUNT(*) AS f_count\n" + 
                "FROM\n" + 
                "    t_groups\n" + 
                "    LEFT JOIN\n" + 
                "        t_students\n" + 
                "        ON\n" + 
                "            t_groups.group_id = t_students.group_id\n" + 
                "GROUP BY\n" + 
                "    group_name\n" + 
                "HAVING\n" + 
                "    COUNT(*) <= ?";
        
        try (Connection connection = singleConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setInt(1, studentCount);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String groupName = resultSet.getString(1);
                int studentsCount = resultSet.getInt(2);
                String line = String.format("%-15s", groupName) + String.format("%-15s", studentsCount) + "\n";
                result.append(line);
            }
            return result;
        }
    }
    
    public StringBuilder findCourses() throws SQLException, IOException {
        String insertQuery = 
                "SELECT *\n" + 
                "FROM\n" + 
                "    t_courses";
        StringBuilder result = new StringBuilder("");
        
        try (Connection connection = singleConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String courseName = resultSet.getString(2);
                String courseDescription = resultSet.getString(3);
                String line = String.format("%-5s", id) + String.format("%-15s", courseName) + String.format("%-10s", courseDescription) + "\n";
                result.append(line);
            }
            return result;
        }
    } 

    public StringBuilder findStudentsRelatedToCourse(String courseName) throws SQLException, IOException {
        StringBuilder result = new StringBuilder();
        String insertQuery = 
                "SELECT\n" + 
                "    first_name\n" + 
                "  , last_name\n" + 
                "FROM\n" + 
                "    t_courses\n" + 
                "    JOIN\n" + 
                "        t_courses_students\n" + 
                "        ON\n" + 
                "            t_courses.course_id = t_courses_students.course_id\n" + 
                "    LEFT JOIN\n" + 
                "        t_students\n" + 
                "        ON\n" + 
                "            t_courses_students.student_id = t_students.student_id\n" + 
                "WHERE\n" + 
                "    course_name = ?";

        try (Connection connection = singleConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, courseName);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String firstName = resultSet.getString(1);
                String lastName = resultSet.getString(2);
                String line = String.format("%-15s", firstName) + String.format("%-15s", lastName) + "\n";
                result.append(line);
            }
            return result;
        }
    }

    public void insertStudent(String firstName, String lastName) throws IOException, SQLException {
        String insertQuery = "INSERT INTO t_students (group_id, first_name, last_name) VALUES(?,?,?)";
        try (Connection connection = singleConnection.getConnection();
                Statement statement = connection.createStatement();
                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);) {
            Student student = new Student(firstName, lastName);
            preparedStatement.setInt(1, student.getGroupId());
            preparedStatement.setString(2, student.getFirstName());
            preparedStatement.setString(3, student.getLastName());
            preparedStatement.executeUpdate();
        }
    }
    
    public StringBuilder findAllStudents() throws SQLException, IOException {
        String insertQuery = 
                "SELECT\n" + 
                "    student_id\n" + 
                "  , first_name\n" + 
                "  , last_name\n" + 
                "FROM\n" + 
                "    t_students";
        StringBuilder result = new StringBuilder();

        try (Connection connection = singleConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String firstName = resultSet.getString(2);
                String lastName = resultSet.getString(3);
                String line = String.format("%-5s", id) + String.format("%-15s", firstName) + String.format("%-10s", lastName) + "\n";
                result.append(line);
            }
            return result;
        }
    }

    public void deleteStudent(int studentId) throws SQLException, IOException {
        String insertQuery = 
                "DELETE\n" + 
                "FROM\n" + 
                "    t_students\n" + 
                "WHERE\n" + 
                "    student_id = ?";
        
        try (Connection connection = singleConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setInt(1, studentId);
            preparedStatement.executeUpdate();
        }
    }

    public void addStudentToCourse(int studentId, String courseName) throws SQLException, IOException {
        String insertQuery = 
                "INSERT INTO t_courses_students\n" + 
                "    (student_id\n" + 
                "      , course_id\n" + 
                "    )\n" + 
                "    VALUES\n" + 
                "    (?\n" + 
                "      , (\n" + 
                "            SELECT\n" + 
                "                course_id\n" + 
                "            FROM\n" + 
                "                t_courses\n" + 
                "            WHERE\n" + 
                "                course_name = ?\n" + 
                "        )\n" + 
                "    )";
        
        try (Connection connection = singleConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setInt(1, studentId);
            preparedStatement.setString(2, courseName);
            preparedStatement.executeUpdate();
        }
    }

    public void removeFromCourse(int studentId, String courseName) throws SQLException, IOException {
        String insertQuery = 
                "DELETE\n" + 
                "FROM\n" + 
                "    t_courses_students\n" + 
                "USING t_courses\n" + 
                "WHERE\n" + 
                "    t_courses_students.course_id = t_courses.course_id\n" + 
                "    AND student_id = ?\n" + 
                "    AND course_name = ?";
        
        try (Connection connection = singleConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setInt(1, studentId);
            preparedStatement.setString(2, courseName);
            preparedStatement.executeUpdate();
        }
    }
    
    public boolean checkStudentInDB(int id) throws SQLException, IOException {
        String query = 
                "SELECT *\n" + 
                "FROM\n" + 
                "    t_students\n" + 
                "WHERE\n" + 
                "    student_id = ?";
        
        try (Connection connection = singleConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return true;
            }
        }
        return false;
    }

    public boolean checkCourseInDB(String courseName) throws SQLException, IOException {
        String query = 
                "SELECT *\n" + 
                "FROM\n" + 
                "    t_courses\n" + 
                "WHERE\n" + 
                "    course_name = ?";
        
        try (Connection connection = singleConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, courseName);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return true;
            }
        }
        return false;
    }

    public boolean checkStudentCourseRelation(int id, String courseName) throws SQLException, IOException {
        String query = 
                "SELECT DISTINCT\n" + 
                "    course_name\n" + 
                "FROM\n" + 
                "    t_students\n" + 
                "    LEFT JOIN\n" + 
                "        t_courses_students\n" + 
                "        ON\n" + 
                "            t_students.student_id = t_courses_students.student_id\n" + 
                "    LEFT JOIN\n" + 
                "        t_courses\n" + 
                "        ON\n" + 
                "            t_courses_students.course_id = t_courses.course_id\n" + 
                "WHERE\n" + 
                "    course_name = 'biology'\n" + 
                "    and t_students.student_id = 7";
        
        try (Connection connection = singleConnection.getConnection();
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
