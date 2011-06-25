package models;

import douyu.mvc.Model;

@Model
public class GroupAndUser2 {
    private int id;
    private int groupId;
    private String groupName;
    private String userName;
    private int groupType;

    public void set(int id, int groupId, String groupName, String userName, int groupType) {
        this.id = id;
        this.groupId = groupId;
        this.groupName = groupName;
        this.userName = userName;
        this.groupType = groupType;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGroupId() {
        return this.groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getGroupType() {
        return this.groupType;
    }

    public void setGroupType(int groupType) {
        this.groupType = groupType;
    }
}