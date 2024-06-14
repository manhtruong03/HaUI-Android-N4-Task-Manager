package haui.android.taskmanager.models;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return Objects.equals(tagName, tag.tagName) &&
                Objects.equals(tagColor, tag.tagColor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tagName, tagColor);
    }

}
