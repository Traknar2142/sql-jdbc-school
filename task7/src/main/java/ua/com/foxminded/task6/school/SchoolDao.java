package ua.com.foxminded.task6.school;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SchoolDao {
    DaoFactory daoFactory = DaoFactory.getInstance();
    private static final String TAB = "%-15s";
    
    public SchoolDao() throws IOException {
        //TestData testData = new TestData();
        //testData.refreshDataBase();
        //testData.generateTestData();
    }
    
    public void addNewStudentIntoDB(String firstName, String lastName) {
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

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public String findeGroups(int studentCount) throws SQLException {
        String insertQuery = "select group_name, COUNT(*) as f_count "
                + "FROM (select * from school.t_groups left join school.t_students on school.t_groups.group_id = school.t_students.group_id) as t_join "
                + "group by group_name having COUNT(*) <= ? ";
        StringBuilder result = new StringBuilder("");
        String line = "";
        try (Connection connection = daoFactory.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setInt(1, studentCount);
            ResultSet resultSet = preparedStatement.executeQuery();            
            while(resultSet.next()) {
                String groupName = resultSet.getString(1);
                int studentsCount = resultSet.getInt(2);
                line = String.format(TAB, groupName) + String.format(TAB, studentsCount) +"\n";
                result.append(line);
            }
            resultSet.close();
            return result.toString();
        }
    }
    
    public String findeStudents(String courseName) throws SQLException {
        String insertQuery = "select first_name, last_name from ("
                + "select course_name, student_id "
                + "from school.t_courses join school.t_courses_students on school.t_courses.course_id = school.t_courses_students.course_id "
                + "where course_name = ? ) as _course_data left join school.t_students on _course_data.student_id = t_students.student_id";
        StringBuilder result = new StringBuilder("");
        String line = "";
        try (Connection connection = daoFactory.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, courseName);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                String firstName = resultSet.getString(1);
                String lastName = resultSet.getString(2);
                line = String.format(TAB, firstName) + String.format(TAB, lastName) +"\n";
                result.append(line);
            }
            resultSet.close();
            return result.toString();
        }
    }
    
    public String deleteStudent(int studentId) throws SQLException {
        String insertQuery = "delete from school.t_students where student_id = ?";
        try(Connection connection = daoFactory.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)){
            preparedStatement.setInt(1, studentId);
            preparedStatement.executeQuery();
            return "student is deleted";
        }       
    }
    
    public String addStudentToCourse(int studentId, String courseName) throws SQLException {
        String insertQuery = "insert into school.t_courses_students (student_id, course_id)" 
                + "values(?,(select course_id from school.t_courses where course_name = ?))";
        try(Connection connection = daoFactory.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)){
            preparedStatement.setInt(1, studentId);
            preparedStatement.setString(2, courseName);
            preparedStatement.executeQuery();
            return "student added to a course";
        }
    }
    
    public String removeFromCourse(int studentId, String courseName) throws SQLException{
        String insertQuery = "delete from school.t_courses_students "
                + "where student_id = ? and course_id = (select course_id from school.t_courses where  course_name = ?)";
        try(Connection connection = daoFactory.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)){
            preparedStatement.setInt(1, studentId);
            preparedStatement.setString(2, courseName);
            preparedStatement.executeQuery();
            return "student remove from a course";
        }
    }
    
    public String printTableOfCourses() throws SQLException {
        String insertQuery = "select * from school.t_courses";
        StringBuilder result = new StringBuilder("");
        String line = "";
        try(Connection connection = daoFactory.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)){
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                int id = resultSet.getInt(1);
                String courseName = resultSet.getString(2);
                String courseDescription = resultSet.getString(3);
                line = String.format("%-5s", id) + String.format("%-15s", courseName) + String.format("%-10s", courseDescription) + "\n";
                result.append(line);
            }
            resultSet.close();
            return result.toString();            
        }
    }
    
   public String printTableOfStudents() throws SQLException {
       String insertQuery = "select student_id, first_name, last_name from school.t_students";
       StringBuilder result = new StringBuilder("");
       String line = "";
       try(Connection connection = daoFactory.getConnection();
               PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)){
           ResultSet resultSet = preparedStatement.executeQuery();
           while(resultSet.next()) {
               int id = resultSet.getInt(1);
               String firstName = resultSet.getString(2);
               String lastName = resultSet.getString(3);
               line = String.format("%-5s", id) + String.format("%-15s", firstName) + String.format("%-10s", lastName) + "\n";
               result.append(line);
           }
           resultSet.close();
           return result.toString();            
       }      
   }
    public void menu() {

    }
}
