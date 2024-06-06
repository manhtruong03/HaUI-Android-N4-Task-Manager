package haui.android.taskmanager.models;

import java.io.Serializable;

public class Task implements Serializable {
    private int taskID;
    private String taskName;
    private String description;
    private String startDate;
    private String startTime;
    private String endDate;
    private String endTime;
    private int priority;
    private int statusID;
    private int tagID;

    // tagId, taskId, tagColor, tagName,taskName, stautusName, description

    public Task() {

    }
    // tagColor, stautusName

    public Task(int taskID, String taskName, String description, String startDate, String startTime, String endDate, String endTime, int priority, int statusID, int tagID) {
        this.taskID = taskID;
        this.taskName = taskName;
        this.description = description;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
        this.priority = priority;
        this.statusID = statusID;
        this.tagID = tagID;
    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getTagID() {
        return tagID;
    }

    public void setTagID(int tagID) {
        this.tagID = tagID;
    }

    public int getStatusID() {
        return statusID;
    }

    public void setStatusID(int statusID) {
        this.statusID = statusID;
    }

    public String getStringWorking(){
        return "Working | Deadline: " + endTime + " at " + endDate;
    }

    public String getStringNewWork(){
        return "Start at: " + startDate + "\nEnd at: " + endDate;
    }

    public String getStringComplete(){
        return "Complete: \nStart at: " + startDate + "\nEnd at: " + endDate;
    }

    public String getStringLate(){
        return "Late: \nStart at: " + startDate + "\nEnd at: " + endDate;
    }

}
