package haui.android.taskmanager.test;


import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

import haui.android.taskmanager.R;
import haui.android.taskmanager.controller.DBHelper;
import haui.android.taskmanager.models.Status;
import haui.android.taskmanager.models.Tag;
import haui.android.taskmanager.models.Task;
import haui.android.taskmanager.models.TaskDetail;

public class TestDBActivity extends AppCompatActivity {
    private DBHelper dbHelper;
    private static final String TAG = "TestDBActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_db);

        dbHelper = new DBHelper(this);

        dbHelper.insertTask("Hoàn thành bài tâp", "...", "12/02/2024", "08:00", "14/02/2024", "08:00", 1, 1, 1);
        dbHelper.insertTask("Sửa lỗi bài tập", "...", "29/05/2024", "08:00", "18/06/2024", "08:00", 2, 2, 2);
        dbHelper.insertTask("Hoàn thành bài tâp", "...", "12/02/2024", "08:00", "14/02/2024", "08:00", 3, 3, 3);
        dbHelper.insertTask("Hoàn thành bài tâp", "...", "12/02/2024", "08:00", "14/02/2024", "08:00", 1, 4, 4);
        dbHelper.insertTask("Hoàn thành bài tâp", "...", "12/02/2024", "08:00", "14/02/2024", "08:00", 2, 1, 5);
        dbHelper.insertTask("Hoàn thành bài tâp", "...", "12/02/2024", "08:00", "14/02/2024", "08:00", 3, 2, 1);
        dbHelper.insertTask("Hoàn thành bài tâp", "...", "12/02/2024", "08:00", "14/02/2024", "08:00", 1, 3, 2);
        dbHelper.insertTask("Hoàn thành bài tâp", "...", "12/02/2024", "08:00", "14/02/2024", "08:00", 1, 4, 3);
        dbHelper.insertTask("Hoàn thành bài tâp", "...", "12/02/2024", "08:00", "14/02/2024", "08:00", 1, 1, 4);
        dbHelper.insertTask("Hoàn thành bài tâp", "...", "12/02/2024", "08:00", "14/02/2024", "08:00", 1, 2, 5);
//        testDBOperations();
    }

    private void testDBOperations() {
        Log.d(TAG, "======START TEST=======");

        // Test adding statuses
        long newStatusId = dbHelper.addStatus("Hello form Test Status");
        Log.d(TAG, "Hàm addStatus() => Đã thêm StatusID: " + newStatusId);

        // Test retrieving a status
        Status status = dbHelper.getStatus((int) newStatusId);
        Log.d(TAG, "Hàm getStatus() => Lấy StatusName: " + status.getStatusName());

        // Test updating a status
        status.setStatusName("Cập nhật Test Status");
        int rowsAffected = dbHelper.updateStatus(status);
        Log.d(TAG, "Hàm updateStatus() => Updated Status Rows Affected: " + rowsAffected);

        // Test retrieving all statuses
        Log.d(TAG, "Hàm getAllStatus():");
        List<Status> statuses = dbHelper.getAllStatus();
        for (Status s : statuses) {
            Log.d(TAG, "- Status: " + s.getStatusName());
        }

        // Test deleting a status
        dbHelper.deleteStatus((int) newStatusId);
        Log.d(TAG, "Hàm deleteStatus() => Đã xóa StatusID: " + newStatusId);

        // Test deleting all statuses
//        dbHelper.deleteAllStatusData();
        Log.d(TAG, "Hàm deleteAllStatusData() => Đã xóa tất cả dữ liệu Status");

        // Test adding a tag
        Tag newTag = new Tag(9, "Test Tag", "Blue");
        long newTagId = dbHelper.addTag(newTag);
        Log.d(TAG, "Hàm addTag() => Đã thêm TagID: " + newTagId);

        // Test retrieving a tag
        Tag tag = dbHelper.getTag((int) newTagId);
        Log.d(TAG, "Hàm getTag() => Truy vấn Tag: " + tag.getTagName() + ", Color: " + tag.getTagColor());

        // Test updating a tag
        tag.setTagName("Cập nhật Test Tag");
        tag.setTagColor("Green");
        rowsAffected = dbHelper.updateTag(tag);
        Log.d(TAG, "Hàm updateTag() => Cập nhật Tag Rows Affected: " + rowsAffected);

        // Test retrieving all tags
        Log.d(TAG, "Hàm getAllTags():");
        List<Tag> tags = dbHelper.getAllTags();
        for (Tag t : tags) {
            Log.d(TAG, "- Tag: " + t.getTagName() + ", Color: " + t.getTagColor());
        }

        // Test adding a task
        long newTaskId = dbHelper.insertTask("Test Task", "Task Description", "01/06/2024", "09:00", "02/06/2024", "17:00", 1, 1, 1);
        Log.d(TAG, "Hàm insertTask() => Đã thêm TaskID: " + newTaskId);

        // Test retrieving all tasks
        Log.d(TAG, "Hàm getAllTasks():");
        List<Task> tasks = dbHelper.getAllTasks();
        for (Task t : tasks) {
            Log.d(TAG, "- Task: " + t.getTaskName() + ", Description: " + t.getDescription());
        }

        // Test retrieving all task details
        Log.d(TAG, "Hàm getAllTasksDetail():");
        List<TaskDetail> listTaskDetail = dbHelper.getAllTasksDetail();
        for (TaskDetail t : listTaskDetail) {
            Log.d(TAG, "- Task Detail: " + t.getTask().getTaskName() + ", Status: " + t.getStatus().getStatusName() + ", Tag: " + t.getTag().getTagName());
        }

        // Test fetching tasks by day
        String testDate = "2024-06-01";
        List<Task> tasksByDay = dbHelper.getTasksByDay(testDate);
        for (Task t : tasksByDay) {
            Log.d(TAG, "Hàm getTasksByDay() => Task vào ngày " + testDate + ": " + t.getTaskName() + ", Status: " + t.getStatusID() + ", Tag: " + t.getTagID());
        }

        // Test retrieving task by ID
        Task taskById = dbHelper.getTaskById((int) newTaskId);
        Log.d(TAG, "Hàm getTaskById() => Task ID " + newTaskId + ": " + taskById.getTaskName());

        // Test retrieving tasks by status
        int statusId = 1;
        List<Task> tasksByStatus = dbHelper.getAllTasksByStatus(statusId);
        Log.d(TAG, "Hàm getAllTasksByStatus() => Task với StatusID " + statusId + ":");
        for (Task t : tasksByStatus) {
            Log.d(TAG, "- Task: " + t.getTaskName() + ", Status: " + t.getStatusID());
        }

        // Test updating a task
        boolean isUpdated = dbHelper.updateTask((int) newTaskId, "Updated Task Description", "2024-06-01", "10:00", "2024-06-02", "18:00", 1);
        Log.d(TAG, "Hàm updateTask() => Task ID " + newTaskId + " updated: " + isUpdated);

        // Test updating task status
        isUpdated = dbHelper.updateStatusTask((int) newTaskId, 2);
        Log.d(TAG, "Hàm updateStatusTask() => Task ID " + newTaskId + " updated status to: " + isUpdated);

        // Test deleting a task
        boolean isDeleted = dbHelper.deleteTask((int) newTaskId);
        Log.d(TAG, "Hàm deleteTask() => Task ID " + newTaskId + " deleted: " + isDeleted);

        // Test searching tasks
        String query = "Task";
        List<Task> searchResults = dbHelper.searchTasks(query);
        Log.d(TAG, "Hàm searchTasks() => Search results for query '" + query + "':");
        for (Task t : searchResults) {
            Log.d(TAG, "- Task: " + t.getTaskName() + ", Description: " + t.getDescription());
        }

        Log.d(TAG, "======END TEST=======");
    }

}
