package ua.com.foxminded.task7.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class SingleConnection {
    private static SingleConnection instance;

    public static synchronized SingleConnection getInstance() {
        if (instance == null) {
            instance = new SingleConnection();
        }
        
        return instance;
    }

    public Connection getConnection() throws IOException, SQLException {
        Properties properties = new Properties();
        
        if(SingleConnection.class.getClassLoader().getResource("application.properties") == null) {
            throw new FileNotFoundException("File \"application.properties\" not found");
        }
        
        properties.load(SingleConnection.class.getClassLoader().getResourceAsStream("application.properties"));
        
        String url = properties.getProperty("url");
        String user = properties.getProperty("user");
        String password = properties.getProperty("password");
        
        Connection connection = DriverManager.getConnection(url, user, password);
        
        return connection;
    }
}
