package ua.com.foxminded.task7;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import ua.com.foxminded.task7.dao.SchoolDao;
import ua.com.foxminded.task7.ui.SchoolUserDialog;

public class SchoolApp {

    public static void main(String[] args) throws Exception {
        SchoolDao school = new SchoolDao();
        SchoolUserDialog schoolUserDialog = new SchoolUserDialog();
        try {
            school.generateTestData();
            schoolUserDialog.getAppMenu();

        } catch (SQLException e) {
            e.printStackTrace();

        } catch (FileNotFoundException e) {
            System.err.println("File " + e.getMessage() + " not found");
            e.printStackTrace();

        } catch (IOException e) {
            System.err.println("Can't read file");
        }
    }
}
