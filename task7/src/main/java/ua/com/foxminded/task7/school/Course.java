package ua.com.foxminded.task7.school;

import java.util.Random;

public class Course{
    private static int id = 1;
    private String courceName = "";
    private int courceId = 0;
    private String courceDescription = "Some description";

    public Course(String course) {
        this.courceName = course;
        this.courceId = id++;
    }
    
    public String getCourceName() {
        return courceName;
    }
    
    public int getCourceId() {
        return courceId;
    }
    
    public String getCourceDescription() {
        return courceDescription;
    }    
}
