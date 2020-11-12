package ua.com.foxminded.task7.school;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

public class Student {
    private static final List<String> FIRST_NAMES = new ArrayList<>();
    private static final List<String> LAST_NAMES = new ArrayList<>();
    private String firstName;
    private String lastName;
    private int groupId;
    private int studentId;

    public Student() {
        this.firstName = FIRST_NAMES.get(generateNameIndex());
        this.lastName = LAST_NAMES.get(generateNameIndex());
    }

    public Student(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
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

    public void setStudentId(int studentId) {
        this.studentId = studentId;
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

        randomNameIndex = firstIndex + (int) (Math.random() * lastIndex);

        return randomNameIndex;
    }

    static {
        FIRST_NAMES.add("Tyler");
        FIRST_NAMES.add("Grayson");
        FIRST_NAMES.add("Baldwin");
        FIRST_NAMES.add("Hadley");
        FIRST_NAMES.add("Lesley");
        FIRST_NAMES.add("Mallory");
        FIRST_NAMES.add("Magnus");
        FIRST_NAMES.add("Lane");
        FIRST_NAMES.add("Maurice");
        FIRST_NAMES.add("Russell");
        FIRST_NAMES.add("Pamela");
        FIRST_NAMES.add("Laurence");
        FIRST_NAMES.add("Fabian");
        FIRST_NAMES.add("Egbert");
        FIRST_NAMES.add("Tony");
        FIRST_NAMES.add("Brigham");
        FIRST_NAMES.add("Herb");
        FIRST_NAMES.add("Kenelm");
        FIRST_NAMES.add("Nicholas");
        FIRST_NAMES.add("Hadley");
    }

    static {
        LAST_NAMES.add("Mitchell");
        LAST_NAMES.add("Bates");
        LAST_NAMES.add("Kennedy");
        LAST_NAMES.add("Gardner");
        LAST_NAMES.add("Shortle");
        LAST_NAMES.add("Manwaring");
        LAST_NAMES.add("Rogers");
        LAST_NAMES.add("Chambers");
        LAST_NAMES.add("Gilbert");
        LAST_NAMES.add("Motley");
        LAST_NAMES.add("Rose");
        LAST_NAMES.add("Hargraves");
        LAST_NAMES.add("Robbins");
        LAST_NAMES.add("Fletcher");
        LAST_NAMES.add("Jackson");
        LAST_NAMES.add("Perkins");
        LAST_NAMES.add("Santiago");
        LAST_NAMES.add("Nichols");
        LAST_NAMES.add("Haynes");
        LAST_NAMES.add("Meskill");
    }
}
