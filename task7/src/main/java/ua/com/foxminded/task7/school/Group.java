package ua.com.foxminded.task7.school;

import java.util.Random;

public class Group {
    private String groupName = "";
    private int groupId = 0;
    
    public Group() {
        this.groupName = generateName();
    }
    
    public String getGroupName() {
        return groupName;
    }
    
    public int getGroupId() {
        return groupId;
    }
    
    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }
    
    private String generateName() {
        int nameLength = 4;
        int hyphenPosition = 2;
        int numPosition = 3;
        
        Random random = new Random();
        StringBuilder name = new StringBuilder();
        
        for (int count = 0; count < nameLength; count++) {
            if (count == hyphenPosition) {
                name.append("-");
            }else if (count == numPosition) {
                int num = 10 + random.nextInt(99 - 10);
                name.append(num);
            } else {
                char tmp = (char) ('a' + random.nextInt('z' - 'a'));
                name.append(tmp);
            }
        }        
        return name.toString();
    }
}
