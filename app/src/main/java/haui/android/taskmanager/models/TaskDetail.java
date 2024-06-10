package haui.android.taskmanager.models;

public class TaskDetail {
    private Task task;
    private Status status;
    private Tag tag;

    public TaskDetail(Task task, Status status, Tag tag) {
        this.task = task;
        this.status = status;
        this.tag = tag;
    }

    public TaskDetail() {
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }
}
