package ua.com.foxminded.task6.school;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class School {
    List<String> courseNames = Arrays.asList("biology", "chemistry", "physics", "algebra", "geometry", "calculus",
            "humanities", "literature", "composition", "history");

   
    private final String url = "jdbc:postgresql://localhost:5432/postgres";
    private final String user = "postgres";
    private final String password = "1234";

    public void generateTestData() {
        List<Course> courses = makeCoursesList();
        List<Student> students = makeStudentList();
        List<Group> groups = makeGroupsList();
        List<Student> studentsWithId = studentsWithGroupId(students, groups);        
        insertGroupsData(groups);
        insertStudentsData(studentsWithId);
        insertCoursesData(courses);
    }
    
    private List<Course> makeCoursesList(){
        List<Course> courses = new ArrayList<Course>();
        
        for (String course : courseNames) {
            courses.add(new Course(course));
        }
        return courses;
    }
    
    private List<Student> makeStudentList() {
        int countOfStudents = 200;
        
        List<Student> students = new ArrayList<Student>();
        for (int count = 0; count < countOfStudents; count++) {
            students.add(new Student());
        }
        
        return students;
    }
    
    private List<Group> makeGroupsList(){
        int countOfGroups = 10;
        List<Group> groups = new ArrayList<Group>();
        for (int count = 0; count < countOfGroups; count++) {
            groups.add(new Group());
        }
        return groups;
    }
    
    private List<Student> studentsWithGroupId(List<Student> students, List<Group> groups){
        List <Student> studentsWithGroups = new ArrayList<Student>();
        int count = 0;
        for (Group group : groups) {
            int numOfStudents = generateValue() + count;            
            for (; count <= numOfStudents; count++) {
                if (count >= students.size()) {
                    break;
                }
                students.get(count).setGroupId(group.getGroupId());
            }
            count = numOfStudents;            
        }        
        return students;        
    }
    
    private void insertGroupsData(List<Group> groups) {
        String insertQuery = "insert into school.t_groups (group_id, group_name) values(?,?)";
        try(Connection connection = DriverManager.getConnection(url, user, password);
                Statement statement = connection.createStatement();){
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            for (Group group : groups) {
                preparedStatement.setInt(1, group.getGroupId());
                preparedStatement.setString(2,group.getGroupName());
                preparedStatement.executeUpdate();
            }            
            
        }catch (SQLException e) {
            e.printStackTrace();
        }
    } 
    
    private void insertStudentsData(List<Student> students) {
        String insertQuery = "insert into school.t_students (student_id, group_id, first_name, last_name) values(?,?,?,?)";;
        try(Connection connection = DriverManager.getConnection(url, user, password);
                Statement statement = connection.createStatement();){
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            for (Student student : students) {
                preparedStatement.setInt(1, student.getStudentId());
                preparedStatement.setInt(2,student.getGroupId());
                preparedStatement.setString(3,student.getFirstName());
                preparedStatement.setString(4,student.getLastName());
                preparedStatement.executeUpdate();
            }
            
            
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void insertCoursesData(List<Course> courses) {
        String insertQuery = "insert into school.t_courses (course_id, course_name, course_description) values(?,?,?)";
        try(Connection connection = DriverManager.getConnection(url, user, password);
                Statement statement = connection.createStatement();){
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            for (Course course : courses) {
                preparedStatement.setInt(1, course.getCourceId());
                preparedStatement.setString(2,course.getCourceName());
                preparedStatement.setString(3,course.getCourceDescription());
                preparedStatement.executeUpdate();
            }                    
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void refreshDatabase() throws IOException {
        try (Connection connection = DriverManager.getConnection(url, user, password);
                Statement statement = connection.createStatement();) {
            PreparedStatement preparedStatement = connection.prepareStatement(readQueryFile("script.sql"));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private String readQueryFile(String file) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File resource = new File(classLoader.getResource(file).getFile());
        StringBuilder query = new StringBuilder();
        try(Stream<String> fileStream = Files.lines(Paths.get(resource.getAbsolutePath()))){
            fileStream.forEach(query::append);
        }
        return query.toString();        
    }
    
    private int generateValue() {
        int min = 10;
        int max = 30;
        int randomValue;
        return randomValue = min + (int)(Math.random() * ((max - min) + 1));
    }
}
