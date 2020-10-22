package ua.com.foxminded.task7.school;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import ua.com.foxminded.task7.dao.SingleConnection;

public class TestData {
    static List<String> courseNames = new ArrayList<>();
    
    static {
        courseNames.add("biology");
        courseNames.add("chemistry");
        courseNames.add("physics");
        courseNames.add("algebra");
        courseNames.add("geometry");
        courseNames.add("humanities");
        courseNames.add("literature");
        courseNames.add("composition");
        courseNames.add("history");             
    }
    
    SingleConnection singleConnection = SingleConnection.getInstance();

    public void refreshDataBase() throws SQLException, IOException{
        try (Connection connection = singleConnection.getConnection();
                Statement statement = connection.createStatement();
                PreparedStatement preparedStatement = connection.prepareStatement(readQueryFile("generateTable.sql"));) {
            preparedStatement.executeUpdate();
        }
    }

    public void generateTestData() throws IOException, SQLException {
        List<Course> courses = makeCoursesList();
        List<Student> students = makeStudentList();
        List<Group> groups = makeGroupsList();

        insertGroupsData(groups);
        setGroupIdIntoGroups(groups);

        setGroupIdIntoStudents(students, groups);
        insertStudentsData(students);
        setStudentIdIntoStudents(students);

        insertCoursesData(courses);
        setCourseIdIntoCourses(courses);

        insertIdData(students, courses);
    }

    private List<Course> makeCoursesList() {
        List<Course> courses = new ArrayList<>();

        for (String course : courseNames) {
            courses.add(new Course(course));
        }
        return courses;
    }

    private List<Student> makeStudentList() {
        int countOfStudents = 200;

        List<Student> students = new ArrayList<>();
        for (int count = 0; count < countOfStudents; count++) {
            students.add(new Student());
        }
        return students;
    }

    private List<Group> makeGroupsList() {
        int countOfGroups = 10;
        List<Group> groups = new ArrayList<>();

        for (int count = 0; count < countOfGroups; count++) {
            groups.add(new Group());
        }
        return groups;
    }

    private void insertGroupsData(List<Group> groups) throws IOException, SQLException {
        String insertQuery = "INSERT INTO t_groups (group_name) "
                           + "VALUES(?)";
        try (Connection connection = singleConnection.getConnection();
                Statement statement = connection.createStatement();
                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);) {
            for (Group group : groups) {
                preparedStatement.setString(1, group.getGroupName());
                preparedStatement.executeUpdate();
            }
        }
    }

    private void setGroupIdIntoGroups(List<Group> groups) throws SQLException, IOException {
        int id;

        for (Group group : groups) {
            id = getGroupIdFromDB(group.getGroupName());
            group.setGroupId(id);
        }
    }

    private int getGroupIdFromDB(String groupName) throws SQLException, IOException {
        String query = "SELECT group_id "
                     + "FROM school.t_groups "
                     + "WHERE group_name = ?";
        String id = "";
        
        try (Connection connection = singleConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, groupName);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                id = resultSet.getString(1);
            }

            return Integer.parseInt(id);
        }
    }

    private void setGroupIdIntoStudents(List<Student> students, List<Group> groups) {
        int count = 0;

        for (Group group : groups) {
            int numOfStudents = generateValue(10, 30) + count;
            for (; count <= numOfStudents; count++) {
                if (count >= students.size()) {
                    break;
                }
                students.get(count).setGroupId(group.getGroupId());
            }
            count = numOfStudents;
        }
    }

    private void insertStudentsData(List<Student> students) throws IOException, SQLException {
        String insertQuery = "INSERT INTO school.t_students (group_id, first_name, last_name) "
                           + "VALUES(?,?,?)";
        
        try (Connection connection = singleConnection.getConnection();
                Statement statement = connection.createStatement();
                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);) {

            for (Student student : students) {
                preparedStatement.setInt(1, student.getGroupId());
                preparedStatement.setString(2, student.getFirstName());
                preparedStatement.setString(3, student.getLastName());
                preparedStatement.executeUpdate();
            }
        }
    }

    private void setStudentIdIntoStudents(List<Student> students) throws SQLException, IOException {
        String query = "SELECT student_id "
                     + "FROM school.t_students";
        int id = 0;
        int count = 0;
        
        try (Connection connection = singleConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                id = resultSet.getInt(1);
                students.get(count).setStudentId(id);
                count++;
            }
        }
    }

    private void insertCoursesData(List<Course> courses) throws IOException, SQLException {
        String insertQuery = "INSERT INTO school.t_courses (course_name, course_description) "
                           + "VALUES(?,?)";
        try (Connection connection = singleConnection.getConnection();
                Statement statement = connection.createStatement();
                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);) {
            for (Course course : courses) {
                preparedStatement.setString(1, course.getCourceName());
                preparedStatement.setString(2, course.getCourceDescription());
                preparedStatement.executeUpdate();
            }
        }
    }

    private void setCourseIdIntoCourses(List<Course> courses) throws SQLException, IOException {
        int id;
        for (Course course : courses) {
            id = getCourseIdFromDB(course.getCourceName());
            course.setCourseId(id);
        }
    }

    private int getCourseIdFromDB(String courseName) throws SQLException, IOException {
        String query = "SELECT course_id "
                     + "FROM school.t_courses "
                     + "WHERE course_name = ?";
        String id = null;
        try (Connection connection = singleConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, courseName);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                id = resultSet.getString(1);
            }
            return Integer.parseInt(id);
        }
    }

    private void insertIdData(List<Student> students, List<Course> courses) throws IOException, SQLException {
        String insertQuery = "INSERT INTO school.t_courses_students (student_id, course_id) "
                           + "VALUES(?,?)";
        try (Connection connection = singleConnection.getConnection();
                Statement statement = connection.createStatement();
                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);) {
            for (Student student : students) {
                if (student.getGroupId() == 0) {
                    continue;
                }
                List<Integer> sampleCourseId = getRandomCoursesId(courses, generateValue(1, 3));
                for (Integer id : sampleCourseId) {
                    preparedStatement.setInt(1, student.getStudentId());
                    preparedStatement.setInt(2, id);
                    preparedStatement.executeUpdate();
                }
            }
        }
    }

    private List<Integer> getRandomCoursesId(List<Course> courses, int selection) {
        Random rand = new Random();
        List<Course> modified—ourses = new ArrayList<Course>(courses);
        List<Integer> idOfCourses = new ArrayList<Integer>();
        for (int i = 0; i < selection; i++) {
            int randomIndex = rand.nextInt(modified—ourses.size());
            idOfCourses.add(modified—ourses.get(randomIndex).getCourceId());
            modified—ourses.remove(randomIndex);
        }
        return idOfCourses;
    }

    private String readQueryFile(String file) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        if(classLoader.getResource(file) == null) {
            throw new FileNotFoundException("File "+ file + " not found");
        }
        File resource = new File(classLoader.getResource(file).getFile());
        StringBuilder query = new StringBuilder();
        try (Stream<String> fileStream = Files.lines(Paths.get(resource.getAbsolutePath()))) {
            fileStream.forEach(query::append);
        }
        return query.toString();
    }

    private int generateValue(int minValue, int maxValue) {
        int randomValue = 0;
        return randomValue = minValue + (int) (Math.random() * ((maxValue - minValue) + 1));
    }
}
