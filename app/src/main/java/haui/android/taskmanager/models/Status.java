package haui.android.taskmanager.models;

public class Status {
    private int statusID;
    private String stautusName;

    public Status() {
    }

    public Status(int statusID, String stautusName) {
        this.statusID = statusID;
        this.stautusName = stautusName;
    }

    public int getStatusID() {
        return statusID;
    }

    public void setStatusID(int statusID) {
        this.statusID = statusID;
    }

    public String getStautusName() {
        return stautusName;
    }

    public void setStautusName(String stautusName) {
        this.stautusName = stautusName;
    }
}
