package haui.android.taskmanager.models;

public class Notification {
    private int notiID;
    private int taskID;
    private String title;
    private String content;
    private String time;

    public Notification() {
    }

    public Notification(int notiID, int taskID, String title, String content, String time) {
        this.notiID = notiID;
        this.taskID = taskID;
        this.title = title;
        this.content = content;
        this.time = time;
    }

    public int getNotiID() {
        return notiID;
    }

    public void setNotiID(int notiID) {
        this.notiID = notiID;
    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
