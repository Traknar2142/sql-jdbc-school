package ua.com.foxminded.task6.school;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

public class Student {
    private static final List<String> FIRST_NAMES = Arrays.asList("Tyler", "Grayson", "Baldwin", "Hadley" , "Lesley", "Mallory", "Magnus", "Lane", "Maurice" , "Russell", "Pamela", "Laurence", "Fabian", "Egbert" , "Tony", "Brigham", "Herb", "Kenelm", "Nicholas" , "Echo" );
    private static final List<String> LAST_NAMES = Arrays.asList("Mitchell", "Bates", "Kennedy", "Gardner" , "Shortle", "Manwaring", "Rogers", "Chambers", "Gilbert" , "Motley", "Rose", "Hargraves", "Robbins", "Fletcher" , "Jackson", "Perkins", "Santiago", "Nichols", "Haynes" , "Meskill" );
    private static int id = 1;
    private String firstName ="";
    private String lastName = "";
    private int groupId = 0;
    private int studentId = 0;
    
    public Student() {
        this.firstName = FIRST_NAMES.get(generateNameIndex());
        this.lastName = LAST_NAMES.get(generateNameIndex());
        this.studentId = id++;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public int getStudentId() {
        return studentId;
    }
    
    public int getGroupId() {
        return groupId;
    }
    
    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }
    
    private int generateNameIndex() {
        int randomNameIndex = 0;
        int firstIndex = 0;
        int lastIndex = 19;
        
        randomNameIndex = firstIndex + (int)(Math.random() * lastIndex);
        
        return randomNameIndex;
    }
}
