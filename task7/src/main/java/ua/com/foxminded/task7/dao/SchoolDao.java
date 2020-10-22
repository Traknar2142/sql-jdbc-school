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
        String line = "";
        StringBuilder result = new StringBuilder();
        String insertQuery = "SELECT group_name, "
                                  + "COUNT(*) AS f_count " 
                           + "FROM "
                             + "(SELECT * "
                             + "FROM t_groups "
                             + "LEFT JOIN t_students ON t_groups.group_id = t_students.group_id) AS t_join "
                           + "GROUP BY group_name " 
                           + "HAVING COUNT(*) <= ?";
        
        try (Connection connection = singleConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setInt(1, studentCount);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()) {
                String groupName = resultSet.getString(1);
                int studentsCount = resultSet.getInt(2);
                line = String.format("%-15s", groupName) + String.format("%-15s", studentsCount) + "\n";
                result.append(line);
            }            
            return result;
        }
    }
    
    public StringBuilder findCourses() throws SQLException, IOException {
        String insertQuery = "SELECT * "
                           + "FROM t_courses";
        StringBuilder result = new StringBuilder("");
        String line = "";
        
        try (Connection connection = singleConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String courseName = resultSet.getString(2);
                String courseDescription = resultSet.getString(3);
                line = String.format("%-5s", id) + String.format("%-15s", courseName) + String.format("%-10s", courseDescription) + "\n";
                result.append(line);
            }
            return result;
        }
    }    

    public StringBuilder findStudentsRelatedToCourse(String courseName) throws SQLException, IOException {
        String line = "";
        StringBuilder result = new StringBuilder();
        String insertQuery = "SELECT first_name, "
                                  + "last_name " 
                           + "FROM"
                             + "(SELECT course_name, "
                                     + "student_id "
                              + "FROM t_courses "
                              + "JOIN t_courses_students ON t_courses.course_id = t_courses_students.course_id "
                             + "WHERE course_name = ? ) AS _course_data "
                           + "LEFT JOIN t_students ON _course_data.student_id = t_students.student_id";

        try (Connection connection = singleConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, courseName);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String firstName = resultSet.getString(1);
                String lastName = resultSet.getString(2);
                line = String.format("%-15s", firstName) + String.format("%-15s", lastName) + "\n";
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
        String insertQuery = "SELECT student_id, "
                                  + "first_name, "
                                  + "last_name "
                           + "FROM t_students";
        StringBuilder result = new StringBuilder();
        String line = "";
        
        try (Connection connection = singleConnection.getConnection();
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
            return result;
        }
    }

    public void deleteStudent(int studentId) throws SQLException, IOException {
        String insertQuery = "DELETE "
                           + "FROM t_students "
                           + "WHERE student_id = ?";
        
        try (Connection connection = singleConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setInt(1, studentId);
            preparedStatement.executeUpdate();            
        }
    }

    public void addStudentToCourse(int studentId, String courseName) throws SQLException, IOException {
        String insertQuery = "INSERT INTO t_courses_students (student_id, course_id) "
                           + "VALUES(?,(SELECT course_id FROM t_courses WHERE course_name = ?))";
        
        try (Connection connection = singleConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setInt(1, studentId);
            preparedStatement.setString(2, courseName);
            preparedStatement.executeUpdate();
        }
    }

    public void removeFromCourse(int studentId, String courseName) throws SQLException, IOException {
        String insertQuery = "DELETE "
                           + "FROM t_courses_students USING t_courses "
                           + "WHERE t_courses_students.course_id = t_courses.course_id "
                             + "AND student_id = ? "
                             + "AND course_name = ?";
        
        try (Connection connection = singleConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setInt(1, studentId);
            preparedStatement.setString(2, courseName);
            preparedStatement.executeUpdate();
        }
    }
    
    public boolean checkStudentInDB(int id) throws SQLException, IOException {
        String query = "SELECT *"
                     + "FROM t_students "
                     + "WHERE student_id = ? ";
        
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
        String query = "SELECT * "
                     + "FROM t_courses "
                     + "WHERE course_name = ?";
        
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
        String query = "SELECT DISTINCT course_name "
                     + "FROM "
                       + "(SELECT * "
                        + "FROM t_students "
                        + "LEFT JOIN t_courses_students ON t_students.student_id = t_courses_students.student_id "
                        + "WHERE t_students.student_id = ?) AS _student_data "
                     + "LEFT JOIN t_courses ON _student_data.course_id = t_courses.course_id "
                     + "WHERE course_name = ?";
        
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
