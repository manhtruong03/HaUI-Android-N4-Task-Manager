package haui.android.taskmanager.models;

public class Tag {
    private int tagID;
    private String tagName;
    private String tagColor;

    public Tag() {
    }

    public Tag(int tagID, String tagName, String tagColor) {
        this.tagID = tagID;
        this.tagName = tagName;
        this.tagColor = tagColor;
    }

    public int getTagID() {
        return tagID;
    }

    public void setTagID(int tagID) {
        this.tagID = tagID;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getTagColor() {
        return tagColor;
    }

    public void setTagColor(String tagColor) {
        this.tagColor = tagColor;
    }
}
