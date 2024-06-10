package haui.android.taskmanager.models;

import java.io.Serializable;
import java.util.List;

public class HomeListTag implements Serializable {

    private int id;
    private List<TaskDetail> taskDetailList;

    public HomeListTag() {
    }

    public HomeListTag(int id, List<TaskDetail> taskDetailList) {
        this.id = id;
        this.taskDetailList = taskDetailList;
    }

    public List<TaskDetail> addList(TaskDetail taskDetail){
        taskDetailList.add(taskDetail);
        return taskDetailList;
    }

    public List<TaskDetail> getTaskDetailList() {
        return taskDetailList;
    }

    public void setTaskDetailList(List<TaskDetail> taskDetailList) {
        this.taskDetailList = taskDetailList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
