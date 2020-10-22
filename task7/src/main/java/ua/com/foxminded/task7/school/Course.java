package ua.com.foxminded.task7.school;

import java.util.Random;

public class Course{
    private String courceName = "";
    private String courceDescription = "Some description";
    private int courceId = 0;

    public Course(String course) {
        this.courceName = course;
    }
    
    public String getCourceName() {
        return courceName;
    }
    
    public int getCourceId() {
        return courceId;
    }
    
    public void setCourseId(int courceId) {
        this.courceId = courceId;
    }
    
    public String getCourceDescription() {
        return courceDescription;
    }    
}
