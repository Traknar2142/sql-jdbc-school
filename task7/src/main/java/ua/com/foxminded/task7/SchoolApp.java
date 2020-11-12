package ua.com.foxminded.task7;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import ua.com.foxminded.task7.dao.SchoolDao;
import ua.com.foxminded.task7.dao.SingleConnection;
import ua.com.foxminded.task7.school.TestData;
import ua.com.foxminded.task7.ui.SchoolUserDialog;

public class SchoolApp {

    public static void main(String[] args) {
        SingleConnection singleConnection = SingleConnection.getInstance();

        SchoolDao school = new SchoolDao(singleConnection);
        SchoolUserDialog schoolUserDialog = new SchoolUserDialog();
        TestData testData = new TestData();

        try {
            school.generateTestData(testData);
            schoolUserDialog.getAppMenu();
        } catch (SQLException e) {
            System.err.println("Can't read the query \n" + e.getMessage());
            e.printStackTrace();

        } catch (FileNotFoundException e) {
            System.err.println("File " + e.getMessage() + " not found");
            e.printStackTrace();

        } catch (IOException e) {
            System.err.println("Can't read the file");
            e.printStackTrace();
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
    }
}
