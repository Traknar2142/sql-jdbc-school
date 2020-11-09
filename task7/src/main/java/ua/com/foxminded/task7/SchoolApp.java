package ua.com.foxminded.task7;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import ua.com.foxminded.task7.dao.SchoolDao;
import ua.com.foxminded.task7.dao.SingleConnection;
import ua.com.foxminded.task7.ui.SchoolUserDialog;

public class SchoolApp {

    public static void main(String[] args) {
        SingleConnection singleConnection = SingleConnection.getInstance();

        SchoolDao school = new SchoolDao(singleConnection);
        SchoolUserDialog schoolUserDialog = new SchoolUserDialog();

        try {
            school.generateTestData();
            schoolUserDialog.getAppMenu();
        } catch (Exception e) {
            if (e instanceof SQLException) {
                SQLException sqle = (SQLException) e;
                System.err.println("Can't read the query \n" + sqle.getMessage());
                sqle.printStackTrace();

            } else if (e instanceof FileNotFoundException) {
                FileNotFoundException fnfe = (FileNotFoundException) e;
                System.err.println("File " + fnfe.getMessage() + " not found");
                fnfe.printStackTrace();

            } else if (e instanceof IOException) {
                IOException ioe = (IOException) e;
                System.err.println("Can't read the file");
                ioe.printStackTrace();
            }
        }
    }
}
