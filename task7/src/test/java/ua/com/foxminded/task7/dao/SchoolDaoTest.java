package ua.com.foxminded.task7.dao;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import ua.com.foxminded.task7.school.TestData;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SchoolDaoTest {

    ResultSet resultSet = Mockito.mock(ResultSet.class);
    PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
    Connection jdbcConnection = Mockito.mock(Connection.class);
    SingleConnection connection = Mockito.mock(SingleConnection.class);
    TestData testData = Mockito.mock(TestData.class);
    SchoolDao dao = new SchoolDao(connection);

    String testCourseName = "testCourseName";
    String testGroupName = "testGroupName";
    String testCourseDisription = "testCourseDiscription";
    String testFirstName = "testFirstName";
    String testLastName = "testLastName";
    String someResult = "someResult";
    int testId = 1;
    int testNumber = 1;
    boolean expectedTrue = true;
    boolean expectedFalse = false;

    @Test
    public void findGroups_ShoudReturnFormatedString_WhenInputIsNumber() throws SQLException, IOException {

        Mockito.when(resultSet.next()).thenReturn(true).thenReturn(false);
        Mockito.when(resultSet.getString(1)).thenReturn(testGroupName);
        Mockito.when(resultSet.getInt(2)).thenReturn(testNumber);

        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(jdbcConnection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.when(connection.getConnection()).thenReturn(jdbcConnection);

        String expected = String.format("%-15s", testGroupName) + String.format("%-15s", testNumber) + "\n";

        Assert.assertEquals(expected, dao.findGroups("13").toString());
    }

    @Test
    public void findCourses_ShoudReturnFormatedString_WhenMethodCall() throws SQLException, IOException {

        Mockito.when(resultSet.next()).thenReturn(true).thenReturn(false);
        Mockito.when(resultSet.getInt(1)).thenReturn(testNumber);
        Mockito.when(resultSet.getString(2)).thenReturn(testCourseName);
        Mockito.when(resultSet.getString(3)).thenReturn(testCourseDisription);

        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(jdbcConnection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.when(connection.getConnection()).thenReturn(jdbcConnection);
        
        String expected = String.format("%-5s", testNumber) + String.format("%-15s", testCourseName) + String.format("%-10s", testCourseDisription) + "\n";
        
        Assert.assertEquals(expected, dao.findCourses().toString());
    }

    @Test
    public void findStudentsRelatedToCourse_ShoudReturnFormatedString_WhenInputCourseName() throws SQLException, IOException {

        Mockito.when(resultSet.next()).thenReturn(true).thenReturn(false);
        Mockito.when(resultSet.getString(1)).thenReturn(testFirstName);
        Mockito.when(resultSet.getString(2)).thenReturn(testLastName);

        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(jdbcConnection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.when(connection.getConnection()).thenReturn(jdbcConnection);

        String expected = String.format("%-15s", testFirstName) + String.format("%-15s", testLastName) + "\n";

        Assert.assertEquals(expected, dao.findStudentsRelatedToCourse(testCourseName).toString());
    }

    @Test
    public void insertStudent_verifyMockBehavior() throws SQLException, IOException {

        Mockito.when(jdbcConnection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);

        Mockito.when(connection.getConnection()).thenReturn(jdbcConnection);

        dao.insertStudent(testFirstName, testLastName);

        Mockito.verify(connection).getConnection();
        Mockito.verify(jdbcConnection).prepareStatement(Mockito.anyString());
        Mockito.verify(preparedStatement).setInt(Mockito.anyInt(), Mockito.anyInt());
        Mockito.verify(preparedStatement, Mockito.times(2)).setString(Mockito.anyInt(), Mockito.anyString());
        Mockito.verify(preparedStatement).executeUpdate();
    }
    
    @Test
    public void findAllStudents_ShoudReturnFormatedString_WhenMethodCall() throws SQLException, IOException {

        Mockito.when(resultSet.next()).thenReturn(true).thenReturn(false);
        Mockito.when(resultSet.getInt(1)).thenReturn(testId);
        Mockito.when(resultSet.getString(2)).thenReturn(testFirstName);
        Mockito.when(resultSet.getString(3)).thenReturn(testLastName);

        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(jdbcConnection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.when(connection.getConnection()).thenReturn(jdbcConnection);
        
        String expected = String.format("%-5s", testId) + String.format("%-15s", testFirstName) + String.format("%-10s", testLastName) + "\n";

        Assert.assertEquals(expected, dao.findAllStudents().toString());     
    }
    
    @Test
    public void deleteStudent_verifyMockBehavior() throws SQLException, IOException {

        Mockito.when(jdbcConnection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);

        Mockito.when(connection.getConnection()).thenReturn(jdbcConnection);

        dao.deleteStudent(testId);

        Mockito.verify(connection).getConnection();
        Mockito.verify(jdbcConnection).prepareStatement(Mockito.anyString());
        Mockito.verify(preparedStatement).setInt(Mockito.anyInt(), Mockito.anyInt());
        Mockito.verify(preparedStatement).executeUpdate();
    }

    @Test
    public void addStudentToCourse_verifyMockBehavior() throws SQLException, IOException {

        Mockito.when(jdbcConnection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);

        Mockito.when(connection.getConnection()).thenReturn(jdbcConnection);

        dao.addStudentToCourse(testId, testCourseName);

        Mockito.verify(connection).getConnection();
        Mockito.verify(jdbcConnection).prepareStatement(Mockito.anyString());
        Mockito.verify(preparedStatement).setInt(Mockito.anyInt(), Mockito.anyInt());
        Mockito.verify(preparedStatement).setString(Mockito.anyInt(), Mockito.anyString());
        Mockito.verify(preparedStatement).executeUpdate();
    }

    @Test
    public void removeFromCourse_verifyMockBehavior() throws SQLException, IOException {

        Mockito.when(jdbcConnection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);

        Mockito.when(connection.getConnection()).thenReturn(jdbcConnection);

        dao.removeFromCourse(testId, testCourseName);

        Mockito.verify(connection).getConnection();
        Mockito.verify(jdbcConnection).prepareStatement(Mockito.anyString());
        Mockito.verify(preparedStatement).setInt(Mockito.anyInt(), Mockito.anyInt());
        Mockito.verify(preparedStatement).setString(Mockito.anyInt(), Mockito.anyString());
        Mockito.verify(preparedStatement).executeUpdate();
    }

    @Test
    public void checkStudentInDB_ShoudReturnTrue_WhenPrStatementReturnTrue() throws SQLException, IOException {

        Mockito.when(resultSet.next()).thenReturn(true).thenReturn(false);
        Mockito.when(resultSet.getString(1)).thenReturn(someResult);

        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(jdbcConnection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.when(connection.getConnection()).thenReturn(jdbcConnection);

        Assert.assertEquals(expectedTrue, dao.checkStudentInDB(testId));
    }

    @Test
    public void checkStudentInDB_ShoudReturnFalse_WhenPrStatementReturnFalse() throws SQLException, IOException {

        Mockito.when(resultSet.next()).thenReturn(false);

        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(jdbcConnection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.when(connection.getConnection()).thenReturn(jdbcConnection);

        Assert.assertEquals(expectedFalse, dao.checkStudentInDB(testId));
    }

    @Test
    public void checkCourseInDB_ShoudReturnTrue_WhenResultsetReturnTrue() throws SQLException, IOException {

        Mockito.when(resultSet.next()).thenReturn(true).thenReturn(false);
        Mockito.when(resultSet.getString(1)).thenReturn(someResult);

        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(jdbcConnection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.when(connection.getConnection()).thenReturn(jdbcConnection);

        Assert.assertEquals(expectedTrue, dao.checkCourseInDB(testCourseName));
    }

    @Test
    public void checkCourseInDB_ShoudReturnFalse_WhenResultsetReturnFalse() throws SQLException, IOException {

        Mockito.when(resultSet.next()).thenReturn(false);

        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(jdbcConnection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.when(connection.getConnection()).thenReturn(jdbcConnection);

        Assert.assertEquals(expectedFalse, dao.checkCourseInDB(testCourseName));
    }

    @Test
    public void checkStudentCourseRelation_ShoudReturnTrue_WhenResultsetReturnTrue() throws SQLException, IOException {

        Mockito.when(resultSet.next()).thenReturn(true).thenReturn(false);

        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(jdbcConnection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.when(connection.getConnection()).thenReturn(jdbcConnection);

        Assert.assertEquals(expectedTrue, dao.checkStudentCourseRelation(testId, testCourseName));
    }

    @Test
    public void checkStudentCourseRelation_ShoudReturnFalse_WhenResultsetReturnFalse() throws SQLException, IOException {

        Mockito.when(resultSet.next()).thenReturn(false);

        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(jdbcConnection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.when(connection.getConnection()).thenReturn(jdbcConnection);

        Assert.assertEquals(expectedFalse, dao.checkStudentCourseRelation(testId, testCourseName));
    }

    @Test
    public void generateTestData_verifyMockBehavior() throws SQLException, IOException {
        dao.generateTestData(testData);
        Mockito.verify(testData).generateNewDataTables();
        Mockito.verify(testData).generateTestData();
    }
}
