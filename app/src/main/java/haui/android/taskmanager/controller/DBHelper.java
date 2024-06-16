package haui.android.taskmanager.controller;

import static android.content.ContentValues.TAG;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import haui.android.taskmanager.models.Notification;
import haui.android.taskmanager.models.Status;
import haui.android.taskmanager.models.Tag;
import haui.android.taskmanager.models.Task;
import haui.android.taskmanager.models.TaskDetail;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    // DATABASE
    private static final String DATABASE_NAME = "TaskManager.db";
    private static final int DATABASE_VERSION = 9;

    // table STATUS
    private static final String TABLE_STATUS_NAME = "STATUS";
    private static final String COLUMN_STATUS_ID = "StatusID";
    private static final String COLUMN_STATUS_NAME = "StatusName";


    // table CATEGORY
    private static final String TABLE_TAG_NAME = "TAG";
    private static final String COLUMN_TAG_ID = "TagID";
    private static final String COLUMN_TAG_NAME = "TagName";
    private static final String COLUMN_TAG_COLOR = "TagColor";


    // table TASK
    private static final String TABLE_TASK_NAME = "TASK";
    private static final String COLUMN_TASK_ID = "ID";
    private static final String COLUMN_TASK_NAME = "TaskName";
    private static final String COLUMN_TASK_DESCRIPTION = "TaskDescription";
    private static final String COLUMN_TASK_START_DATE = "StartDate";
    private static final String COLUMN_TASK_START_TIME = "StartTime";
    private static final String COLUMN_TASK_END_DATE = "EndDate";
    private static final String COLUMN_TASK_END_TIME = "EndTime";
    private static final String COLUMN_TASK_PRIORITY = "Priority";
    // Foreign key
    private static final String COLUMN_TASK_STATUS_ID = "StatusID";
    private static final String COLUMN_TASK_TAG_ID = "TagID";


    // table NOTIFICATION
    private static final String TABLE_NOTIFICATION_NAME = "NOTIFICATION";
    private static final String COLUMN_NOTIFICATION_ID = "NotificationID";
    private static final String COLUMN_NOTIFICATION_TASKID = "TaskID";
    private static final String COLUMN_NOTIFICATION_TITLE = "NotificationTitle";
    private static final String COLUMN_NOTIFICATION_CONTENT = "NotificationContent";
    private static final String COLUMN_NOTIFICATION_TIME = "NotificationTime";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private boolean isTableExists(SQLiteDatabase db, String tableName) {
        Cursor cursor = db.rawQuery("SELECT DISTINCT tbl_name FROM sqlite_master WHERE tbl_name = '" + tableName + "'", null);
        boolean result = cursor.getCount() > 0;
        cursor.close();
        return result;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        if (!isTableExists(db, TABLE_STATUS_NAME)) {
            String createCategoryTableStatement = String.format(
                    "CREATE TABLE %s (" +
                            "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "%s TEXT)",
                    TABLE_STATUS_NAME, COLUMN_STATUS_ID, COLUMN_STATUS_NAME
            );
            db.execSQL(createCategoryTableStatement);
        }

        if (!isTableExists(db, TABLE_TAG_NAME)) {
            String createTagTableStatement = String.format(
                    "CREATE TABLE %s (" +
                            "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "%s TEXT, " +
                            "%s TEXT)",
                    TABLE_TAG_NAME, COLUMN_TAG_ID, COLUMN_TAG_NAME, COLUMN_TAG_COLOR
            );
            db.execSQL(createTagTableStatement);
        }

        if (!isTableExists(db, TABLE_TASK_NAME)) {
            String createTaskTableStatement = String.format(
                    "CREATE TABLE %s (" +
                        "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s INTEGER, " +
                        "%s INTEGER, " +
                        "%s INTEGER, " +
                        "FOREIGN KEY (%s) REFERENCES %s (%s), " +
                        "FOREIGN KEY (%s) REFERENCES %s (%s))",
                    TABLE_TASK_NAME, COLUMN_TASK_ID, COLUMN_TASK_NAME, COLUMN_TASK_DESCRIPTION,
                    COLUMN_TASK_START_DATE, COLUMN_TASK_START_TIME, COLUMN_TASK_END_DATE, COLUMN_TASK_END_TIME,
                    COLUMN_TASK_PRIORITY, COLUMN_TASK_STATUS_ID, COLUMN_TASK_TAG_ID,
                    COLUMN_TASK_STATUS_ID, TABLE_STATUS_NAME, COLUMN_STATUS_ID,
                    COLUMN_TASK_TAG_ID, TABLE_TAG_NAME, COLUMN_TAG_ID
            );
            db.execSQL(createTaskTableStatement);
        }

        if (!isTableExists(db, TABLE_NOTIFICATION_NAME)) {
            String createNotificationTableStatement = String.format(
                    "CREATE TABLE %s (" +
                            "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "%s INTEGER, " +
                            "%s TEXT, " +
                            "%s TEXT, " +
                            "%s TEXT)",
                    TABLE_NOTIFICATION_NAME, COLUMN_NOTIFICATION_ID, COLUMN_NOTIFICATION_TASKID,
                    COLUMN_NOTIFICATION_TITLE,
                    COLUMN_NOTIFICATION_CONTENT, COLUMN_NOTIFICATION_TIME
            );
            db.execSQL(createNotificationTableStatement);
        }


        initialData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Xử lý nâng cấp cơ sở dữ liệu khi có thay đổi version
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK_NAME);
        onCreate(db);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STATUS_NAME);
        onCreate(db);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAG_NAME);
        onCreate(db);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATION_NAME);
        onCreate(db);
    }

    private void initialData(SQLiteDatabase db) {
        insertStatus(db, "Cần làm");
        insertStatus(db, "Đang làm");
        insertStatus(db, "Hoàn thành");
        insertStatus(db, "Muộn");

        // Insert tags
//        insertTag(db, "Công việc", "Red");
//        insertTag(db, "Cá nhân", "Blue");
//        insertTag(db, "Khẩn cấp", "Green");
//        insertTag(db, "Nhà", "Yellow");
//        insertTag(db, "Linh tinh", "Purple");
//        insertTag(db, "Khác", "Pink");
        // Insert tags
        insertTag(db, "Công việc", "#FF0033");
        insertTag(db, "Cá nhân", "#0033FF");
        insertTag(db, "Khẩn cấp", "#33FF99");
        insertTag(db, "Nhà", "#FFCC33");
        insertTag(db, "Linh tinh", "#800080");

        // Insert tasks
        addTask(db, "Hoàn thành báo cáo", "Hoàn thành báo cáo tài chính hàng tháng.", "01/06/2024", "09:00", "02/06/2024", "17:00", 1, 1, 1);
        addTask(db, "Họp nhóm", "Tham dự cuộc họp nhóm hàng tuần.", "03/06/2024", "10:00", "05/06/2024", "11:00", 2, 2, 2);
        addTask(db, "Deadline dự án", "Gửi sản phẩm cuối cùng của dự án.", "01/06/2024", "14:00", "06/06/2024", "16:00", 3, 3, 3);
        addTask(db, "Gọi cho khách hàng", "Gọi với khách hàng để thảo luận về các yêu cầu của dự án.", "05/06/2024", "13:00", "10/06/2024", "14:00", 1, 4, 4);
        addTask(db, "Đánh giá code", "Xem lại mã được gửi bởi các thành viên trong nhóm.", "09/06/2024", "15:00", "11/06/2024", "17:00", 2, 1, 5);
        addTask(db, "Cập nhật trang web", "Cập nhật tin tức mới nhất vào trang web công ty.", "11/06/2024", "16:00", "11/06/2024", "18:00", 3, 2, 1);
        addTask(db, "Sửa lỗi", "Sửa lỗi do nhóm QA báo cáo..", "12/06/2024", "09:00", "14/06/2024", "12:00", 1, 3, 2);
        addTask(db, "Tính năng mới", "Phát triển tính năng mới cho ứng dụng di động.", "14/06/2024", "10:00", "15/06/2024", "17:00", 2, 4, 3);
        addTask(db, "Viết tài liệu", "Viết tài liệu cho API mới.", "14/06/2024", "11:00", "16/06/2024", "13:00", 3, 1, 4);
        addTask(db, "Lên kế hoạch chạy nước rút", "Lập kế hoạch nhiệm vụ cho lần chạy nước rút tiếp theo.", "19/06/2024", "09:00", "20/06/2024", "11:00", 1, 2, 5);

        // Insert notification
        insertNotification(db, 3, "Bắt đầu: Deadline dự án", "Gửi sản phẩm cuối cùng của dự án.", "01/06/2024 14:00");
        insertNotification(db, 2,"Bắt đầu: Họp nhóm", "Tham dự cuộc họp nhóm hàng tuần.", "03/06/2024 10:00");

        insertNotification(db, 4, "Bắt đầu: Gọi cho khách hàng", "Gọi với khách hàng để thảo luận về các yêu cầu của dự án.", "05/06/2024 13:00");
        insertNotification(db, 4,"[Nhắc lại] Bắt đầu: Gọi cho khách hàng", "Gọi với khách hàng để thảo luận về các yêu cầu của dự án.", "05/06/2024 13:05");

        insertNotification(db, 4,"Đến hạn: Gọi cho khách hàng", "Gọi với khách hàng để thảo luận về các yêu cầu của dự án.", "10/06/2024 14:00");
        insertNotification(db, 4, "[Nhắc lại] Đến hạn: Gọi cho khách hàng", "Gọi với khách hàng để thảo luận về các yêu cầu của dự án.", "10/06/2024 14:05");

        insertNotification(db, 6,"Bắt đầu: Cập nhật trang web", "Cập nhật tin tức mới nhất vào trang web công ty.", "11/06/2024 16:00");
        insertNotification(db, 6,"[Nhắc lại] Bắt đầu: Cập nhật trang web", "Cập nhật tin tức mới nhất vào trang web công ty.", "11/06/2024 16:05");

        insertNotification(db, 8, "Bắt đầu: Tính năng mới", "Phát triển tính năng mới cho ứng dụng di động.", "14/06/2024 10:00");
        insertNotification(db, 8, "[Nhắc lại] Bắt đầu: Tính năng mới", "Phát triển tính năng mới cho ứng dụng di động.", "14/06/2024 10:05");

        insertNotification(db, 8, "Đến hạn: Tính năng mới", "Phát triển tính năng mới cho ứng dụng di động.", "15/06/2024 17:00");
        insertNotification(db, 8, "[Nhắc lại] Đến hạn: Tính năng mới", "Phát triển tính năng mới cho ứng dụng di động.", "15/06/2024 17:05");
    }

    private void addTask(SQLiteDatabase db, String taskName, String description, String startDate, String startTime, String endDate, String endTime, int priority, int statusID, int tagID) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK_NAME, taskName);
        values.put(COLUMN_TASK_DESCRIPTION, description);
        values.put(COLUMN_TASK_START_DATE, startDate);
        values.put(COLUMN_TASK_START_TIME, startTime);
        values.put(COLUMN_TASK_END_DATE, endDate);
        values.put(COLUMN_TASK_END_TIME, endTime);
        values.put(COLUMN_TASK_PRIORITY, priority);
        values.put(COLUMN_TASK_STATUS_ID, statusID);
        values.put(COLUMN_TASK_TAG_ID, tagID);
        db.insert(TABLE_TASK_NAME, null, values);
    }

    private void insertNotification(SQLiteDatabase db, int taskID, String title, String content, String time) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTIFICATION_TASKID, taskID);
        values.put(COLUMN_NOTIFICATION_TITLE, title);
        values.put(COLUMN_NOTIFICATION_CONTENT, content);
        values.put(COLUMN_NOTIFICATION_TIME, time);
        db.insert(TABLE_NOTIFICATION_NAME, null, values);
    }

    private void insertTag(SQLiteDatabase db, String tagName, String tagColor) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TAG_NAME, tagName);
        values.put(COLUMN_TAG_COLOR, tagColor);
        db.insert(TABLE_TAG_NAME, null, values);
    }

    private long insertStatus(SQLiteDatabase db, String statusName) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_STATUS_NAME, statusName);
        return db.insert(TABLE_STATUS_NAME, null, values);
    }

    public long addStatus(String statusName) {
        SQLiteDatabase db = this.getWritableDatabase();
        long newRowId = insertStatus(db, statusName);
        db.close();
        return newRowId;
    }

    public Status getStatus(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_STATUS_NAME, new String[]{COLUMN_STATUS_ID, COLUMN_STATUS_NAME}, COLUMN_STATUS_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            Status status = new Status(cursor.getInt(0), cursor.getString(1));
            cursor.close();
            db.close();
            return status;
        } else {
            db.close();
            return null;
        }
    }

    public Status getStatusByName(String statusName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_STATUS_NAME,
                new String[]{COLUMN_STATUS_ID, COLUMN_STATUS_NAME},
                COLUMN_STATUS_NAME + " = ? COLLATE NOCASE",
                new String[]{statusName},
                null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            Status status = new Status(cursor.getInt(0), cursor.getString(1));
            cursor.close();
            db.close();
            return status;
        } else {
            db.close();
            return null;
        }
    }

    public List<Status> getAllStatus() {
        List<Status> statuses = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_STATUS_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Status status = new Status(cursor.getInt(0), cursor.getString(1));
                statuses.add(status);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return statuses;
    }

    public int updateStatus(Status status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STATUS_NAME, status.getStatusName());

        return db.update(TABLE_STATUS_NAME, values, COLUMN_STATUS_ID + " = ?", new String[]{String.valueOf(status.getStatusID())});
    }

    public void deleteStatus(int statusID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_STATUS_NAME, COLUMN_STATUS_ID + " = ?", new String[]{String.valueOf(statusID)});
        db.close();
    }

    public void deleteAllStatusData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_STATUS_NAME, null, null);
        db.close();
    }

    // CRUD for TAG table
    public long addTag(Tag tag) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TAG_NAME, tag.getTagName());
        values.put(COLUMN_TAG_COLOR, tag.getTagColor());

        long newRowId = db.insert(TABLE_TAG_NAME, null, values);
        db.close();
        return newRowId;
    }

    public Tag getTagById(int tagID){
        SQLiteDatabase db = this.getReadableDatabase();
        Tag tag = null;
        Cursor cursor = db.query(TABLE_TAG_NAME, new String[]{
                        COLUMN_TAG_ID, COLUMN_TAG_NAME, COLUMN_TAG_COLOR},
                COLUMN_TAG_ID + "=?", new String[]{String.valueOf(tagID)},
                null, null, null);

        if (cursor.moveToFirst()) {
            tag = createTagFromCursor(cursor);
        }
        cursor.close();
        db.close();
        return tag;
    }


    public Tag getTagByTagName(String tagName){
        SQLiteDatabase db = this.getReadableDatabase();
        Tag tag = null;
        Cursor cursor = db.query(TABLE_TAG_NAME,
                new String[]{COLUMN_TAG_ID, COLUMN_TAG_NAME, COLUMN_TAG_COLOR},
                COLUMN_TAG_NAME + " = ? COLLATE NOCASE",
                new String[]{tagName},
                null, null, null);

        if (cursor.moveToFirst()) {
            tag = createTagFromCursor(cursor);
        }
        cursor.close();
        db.close();
        return tag;
    }

    public Tag getTag(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TAG_NAME, new String[]{COLUMN_TAG_ID, COLUMN_TAG_NAME, COLUMN_TAG_COLOR}, COLUMN_TAG_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            Tag tag = new Tag(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
            cursor.close();
            db.close();
            return tag;
        } else {
            db.close();
            return null;
        }
    }

    public List<Tag> getAllTags() {
        List<Tag> tags = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_TAG_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Tag tag = new Tag(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
                tags.add(tag);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return tags;
    }

    public int updateTag(Tag tag) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TAG_NAME, tag.getTagName());
        values.put(COLUMN_TAG_COLOR, tag.getTagColor());

        return db.update(TABLE_TAG_NAME, values, COLUMN_TAG_ID + " = ?", new String[]{String.valueOf(tag.getTagID())});
    }

    public int deleteTag(int tagID) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_TAG_NAME, COLUMN_TAG_ID + " = ?", new String[]{String.valueOf(tagID)});
    }

    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TASK_NAME, new String[]{
                        COLUMN_TASK_ID, COLUMN_TASK_NAME, COLUMN_TASK_DESCRIPTION,
                        COLUMN_TASK_START_DATE, COLUMN_TASK_START_TIME,
                        COLUMN_TASK_END_DATE, COLUMN_TASK_END_TIME,
                        COLUMN_TASK_PRIORITY,
                        COLUMN_TASK_STATUS_ID, COLUMN_TASK_TAG_ID},
                null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                tasks.add(createTaskFromCursor(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return tasks;
    }

    public List<TaskDetail> getAllTasksDetail() {
        List<TaskDetail> listTaskDetail = new ArrayList<>();
        String selectQuery = "SELECT t.*, s.*, g.* " + " FROM "
                + TABLE_TASK_NAME + " t LEFT JOIN " + TABLE_STATUS_NAME + " s ON t." + COLUMN_TASK_STATUS_ID + " = s." + COLUMN_STATUS_ID
                + " LEFT JOIN " + TABLE_TAG_NAME + " g ON t." + COLUMN_TASK_TAG_ID + " = g." + COLUMN_TAG_ID;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                Status status = new Status();
                Tag tag = new Tag();

                task = createTaskFromCursor(cursor);
                status = createStatusFromCursor(cursor);
                tag = createTagFromCursor(cursor);

                TaskDetail taskDetail = new TaskDetail(task, status, tag);

                listTaskDetail.add(taskDetail);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return listTaskDetail;
    }


    public TaskDetail getTasksDetailByID(int taskId) {
        String selectQuery = "SELECT t.*, s.*, g.* "
                + "FROM " + TABLE_TASK_NAME + " t "
                + "LEFT JOIN " + TABLE_STATUS_NAME + " s ON t." + COLUMN_TASK_STATUS_ID + " = s." + COLUMN_STATUS_ID + " "
                + "LEFT JOIN " + TABLE_TAG_NAME + " g ON t." + COLUMN_TASK_TAG_ID + " = g." + COLUMN_TAG_ID + " "
                + "WHERE t." + COLUMN_TASK_ID + " = ?";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(taskId)});


        TaskDetail taskDetail = null;
        if (cursor.moveToFirst()) {
            Task task = createTaskFromCursor(cursor);
            Status status = createStatusFromCursor(cursor);
            Tag tag = createTagFromCursor(cursor);

            taskDetail = new TaskDetail(task, status, tag);
        }

        cursor.close();
        db.close();
        return taskDetail;
    }

    public List<TaskDetail> getAllTaskDetailByTagId(int tagId) {
        List<TaskDetail> listTaskDetail = new ArrayList<>();
        String selectQuery = "SELECT t.*, s.*, g.* " +
                " FROM " + TABLE_TASK_NAME + " t " +
                " LEFT JOIN " + TABLE_STATUS_NAME + " s ON t." + COLUMN_TASK_STATUS_ID + " = s." + COLUMN_STATUS_ID +
                " LEFT JOIN " + TABLE_TAG_NAME + " g ON t." + COLUMN_TASK_TAG_ID + " = g." + COLUMN_TAG_ID +
                " WHERE t." + COLUMN_TAG_ID + " = ?";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(tagId)}); // Truyền tagId vào

        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                Status status = new Status();
                Tag tag = new Tag();

                task = createTaskFromCursor(cursor);
                status = createStatusFromCursor(cursor);
                tag = createTagFromCursor(cursor);

                TaskDetail taskDetail = new TaskDetail(task, status, tag);

                listTaskDetail.add(taskDetail);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return listTaskDetail;
    }

    public List<Task> getTasksByDay(String date) {
        List<Task> tasks = new ArrayList<>();

        String selectQuery = "SELECT t.*, s." + COLUMN_STATUS_NAME + ", g." + COLUMN_TAG_NAME + ", g." + COLUMN_TAG_COLOR
                + " FROM " + TABLE_TASK_NAME + " t LEFT JOIN " + TABLE_STATUS_NAME + " s ON t." + COLUMN_TASK_STATUS_ID + " = s." + COLUMN_STATUS_ID
                + " LEFT JOIN " + TABLE_TAG_NAME + " g ON t." + COLUMN_TASK_TAG_ID + " = g." + COLUMN_TAG_ID
                + " WHERE date(substr(t." + COLUMN_TASK_START_DATE + ", 7, 4) || '-' || substr(t." + COLUMN_TASK_START_DATE + ", 4, 2) || '-' || substr(t." + COLUMN_TASK_START_DATE + ", 1, 2)) <= date(?)"
                + " AND date(substr(t." + COLUMN_TASK_END_DATE + ", 7, 4) || '-' || substr(t." + COLUMN_TASK_END_DATE + ", 4, 2) || '-' || substr(t." + COLUMN_TASK_END_DATE + ", 1, 2)) >= date(?)";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{date, date});

        if (cursor.moveToFirst()) {
            do {
                Task task = createTaskFromCursor(cursor);
                tasks.add(task);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return tasks;
    }

    public Task getTaskById(int taskId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Task task = null;
        Cursor cursor = db.query(TABLE_TASK_NAME, new String[]{
                        COLUMN_TASK_ID, COLUMN_TASK_NAME, COLUMN_TASK_DESCRIPTION,
                        COLUMN_TASK_START_DATE, COLUMN_TASK_START_TIME,
                        COLUMN_TASK_END_DATE, COLUMN_TASK_END_TIME,
                        COLUMN_TASK_PRIORITY,
                        COLUMN_TASK_STATUS_ID, COLUMN_TASK_TAG_ID},
                COLUMN_TASK_ID + "=?", new String[]{String.valueOf(taskId)},
                null, null, null);

        if (cursor.moveToFirst()) {
            task = createTaskFromCursor(cursor);
        }
        cursor.close();
        db.close();
        return task;
    }

    public List<Task> getAllTasksByStatus(int statusId) {
        List<Task> tasks = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TASK_NAME, new String[]{
                        COLUMN_TASK_ID, COLUMN_TASK_NAME, COLUMN_TASK_DESCRIPTION,
                        COLUMN_TASK_START_DATE, COLUMN_TASK_START_TIME,
                        COLUMN_TASK_END_DATE, COLUMN_TASK_END_TIME,
                        COLUMN_TASK_PRIORITY,
                        COLUMN_TASK_STATUS_ID, COLUMN_TASK_TAG_ID},
                COLUMN_TASK_STATUS_ID +"=?", new String[]{String.valueOf(statusId)},
                null, null, null);

        // Xử lý kết quả truy vấn
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int idIndex = cursor.getColumnIndex(COLUMN_TASK_ID);
                int taskNameIndex = cursor.getColumnIndex(COLUMN_TASK_NAME);
                int taskPriorityIndex = cursor.getColumnIndex(COLUMN_TASK_PRIORITY);
                int taskDescriptionIndex = cursor.getColumnIndex(COLUMN_TASK_DESCRIPTION);
                int taskStartDayIndex = cursor.getColumnIndex(COLUMN_TASK_START_DATE);
                int taskStartTimeIndex = cursor.getColumnIndex(COLUMN_TASK_START_TIME);
                int taskEndDayIndex = cursor.getColumnIndex(COLUMN_TASK_END_DATE);
                int taskEndTimeIndex = cursor.getColumnIndex(COLUMN_TASK_END_TIME);
                int taskTagIdIndex = cursor.getColumnIndex(COLUMN_TASK_TAG_ID);
                int taskStatusIdIndex = cursor.getColumnIndex(COLUMN_TASK_STATUS_ID);

                if (idIndex != -1 && taskNameIndex != -1 && taskPriorityIndex != -1 && taskDescriptionIndex != -1 && taskStartDayIndex != -1 && taskStartTimeIndex != -1 && taskEndDayIndex != -1 && taskEndTimeIndex != -1 && taskTagIdIndex != -1 && taskStatusIdIndex != -1) {

                    int id = cursor.getInt(idIndex);
                    String taskName = cursor.getString(taskNameIndex);
                    int taskPriority = cursor.getInt(taskPriorityIndex);
                    String taskDescription = cursor.getString(taskDescriptionIndex);
                    String taskStartDate = cursor.getString(taskStartDayIndex);
                    String taskStartTime = cursor.getString(taskStartTimeIndex);
                    String taskEndDate = cursor.getString(taskEndDayIndex);
                    String taskEndTime = cursor.getString(taskEndTimeIndex);
                    int taskTagId = cursor.getInt(taskTagIdIndex);
                    int taskStatusId = cursor.getInt(taskStatusIdIndex);

                    Task task = new Task(id, taskName, taskDescription, taskStartDate, taskStartTime, taskEndDate, taskEndTime, taskPriority, taskStatusId, taskTagId);

                    tasks.add(task);
                }
            } while (cursor.moveToNext());

            // Đóng Cursor sau khi sử dụng
            cursor.close();
        }
        db.close();
        return tasks;
    }

    public List<Task> searchTasks(String query) {
        List<Task> tasks = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_TASK_NAME + " WHERE " + COLUMN_TASK_NAME + " LIKE '%" + query + "%'";
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Xử lý kết quả truy vấn
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int idIndex = cursor.getColumnIndex(COLUMN_TASK_ID);
                int taskNameIndex = cursor.getColumnIndex(COLUMN_TASK_NAME);
                int taskPriorityIndex = cursor.getColumnIndex(COLUMN_TASK_PRIORITY);
                int taskDescriptionIndex = cursor.getColumnIndex(COLUMN_TASK_DESCRIPTION);
                int taskStartDayIndex = cursor.getColumnIndex(COLUMN_TASK_START_DATE);
                int taskStartTimeIndex = cursor.getColumnIndex(COLUMN_TASK_START_TIME);
                int taskEndDayIndex = cursor.getColumnIndex(COLUMN_TASK_END_DATE);
                int taskEndTimeIndex = cursor.getColumnIndex(COLUMN_TASK_END_TIME);
                int taskTagIdIndex = cursor.getColumnIndex(COLUMN_TASK_TAG_ID);
                int taskStatusIdIndex = cursor.getColumnIndex(COLUMN_TASK_STATUS_ID);

                if (idIndex != -1 && taskNameIndex != -1 && taskPriorityIndex != -1 && taskDescriptionIndex != -1 && taskStartDayIndex != -1 && taskStartTimeIndex != -1 && taskEndDayIndex != -1 && taskEndTimeIndex != -1 && taskTagIdIndex != -1 && taskStatusIdIndex != -1) {

                    int id = cursor.getInt(idIndex);
                    String taskName = cursor.getString(taskNameIndex);
                    int taskPriority = cursor.getInt(taskPriorityIndex);
                    String taskDescription = cursor.getString(taskDescriptionIndex);
                    String taskStartDate = cursor.getString(taskStartDayIndex);
                    String taskStartTime = cursor.getString(taskStartTimeIndex);
                    String taskEndDate = cursor.getString(taskEndDayIndex);
                    String taskEndTime = cursor.getString(taskEndTimeIndex);
                    int taskTagId = cursor.getInt(taskTagIdIndex);
                    int taskStatusId = cursor.getInt(taskStatusIdIndex);

                    Task task = new Task(id, taskName, taskDescription, taskStartDate, taskStartTime, taskEndDate, taskEndTime, taskPriority, taskStatusId, taskTagId);

                    tasks.add(task);
                }
            } while (cursor.moveToNext());

            // Đóng Cursor sau khi sử dụng
            cursor.close();
        }

        db.close();
        return tasks;
    }

    private Integer safeGetInteger(Cursor cursor, String columnName) {
        int columnIndex = cursor.getColumnIndex(columnName);
        if (columnIndex != -1 && !cursor.isNull(columnIndex)) {
            return cursor.getInt(columnIndex);
        }
        return null;
    }

    private String safeGetString(Cursor cursor, String columnName) {
        int columnIndex = cursor.getColumnIndex(columnName);
        if (columnIndex != -1 && !cursor.isNull(columnIndex)) {
            return cursor.getString(columnIndex);
        }
        return null;
    }

    private Task createTaskFromCursor(Cursor cursor) {
        Integer id = safeGetInteger(cursor, COLUMN_TASK_ID);
        String taskName = safeGetString(cursor, COLUMN_TASK_NAME);
        String taskDescription = safeGetString(cursor, COLUMN_TASK_DESCRIPTION);
        String taskStartDay = safeGetString(cursor, COLUMN_TASK_START_DATE);
        String taskStartTime = safeGetString(cursor, COLUMN_TASK_START_TIME);
        String taskEndDay = safeGetString(cursor, COLUMN_TASK_END_DATE);
        String taskEndTime = safeGetString(cursor, COLUMN_TASK_END_TIME);
        Integer taskPriority = safeGetInteger(cursor, COLUMN_TASK_PRIORITY);
        Integer taskStatusId = safeGetInteger(cursor, COLUMN_TASK_STATUS_ID);
        Integer taskTagId = safeGetInteger(cursor, COLUMN_TASK_TAG_ID);

        if (id != null && taskName != null && taskDescription != null &&
                taskStartDay != null && taskStartTime != null &&
                taskEndDay != null && taskEndTime != null &&
                taskPriority != null && taskStatusId != null && taskTagId != null) {
            return new Task(id, taskName, taskDescription,
                    taskStartDay, taskStartTime, taskEndDay, taskEndTime,
                    taskPriority, taskStatusId, taskTagId);
        } else {

            throw new IllegalArgumentException("createTaskFromCursor(): One or more essential fields are missing in the cursor." + "|id: " + id +
                    "|taskName: " + taskName +
                    "|taskDescription: " + taskDescription +
                    "|taskStartDay: " + taskStartDay +
                    "|taskStartTime: " + taskStartTime +
                    "|taskEndDay: " + taskEndDay +
                    "|taskEndTime: " + taskEndTime +
                    "|taskPriority: " + taskPriority +
                    "|taskStatusId: " + taskStatusId +
                    "|taskTagId: " + taskTagId);
        }
    }

    private Status createStatusFromCursor(Cursor cursor) {
        Integer id = safeGetInteger(cursor, COLUMN_STATUS_ID);
        String statusName = safeGetString(cursor, COLUMN_STATUS_NAME);

        if (id != null && statusName != null) {
            return new Status(id, statusName);
        } else {
            throw new IllegalArgumentException("createStatusFromCursor(): One or more essential fields are missing in the cursor.");
        }
    }

    private Tag createTagFromCursor(Cursor cursor) {
        Integer id = safeGetInteger(cursor, COLUMN_TAG_ID);
        String tagName = safeGetString(cursor, COLUMN_TAG_NAME);
        String tagColor = safeGetString(cursor, COLUMN_TAG_COLOR);

        if (id != null && tagName != null && tagColor != null) {
            return new Tag(id, tagName, tagColor);
        } else {
            throw new IllegalArgumentException("createTagFromCursor(): One or more essential fields are missing in the cursor.");
        }
    }

    public long insertTask(String taskName, String taskDescription,
                           String taskStartDay, String taskStartTime,
                           String taskEndDay, String taskEndTime,
                           int taskPriority, int taskStatusId, int taskTagId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK_NAME, taskName);
        values.put(COLUMN_TASK_DESCRIPTION, taskDescription);
        values.put(COLUMN_TASK_START_DATE, taskStartDay);
        values.put(COLUMN_TASK_START_TIME, taskStartTime);
        values.put(COLUMN_TASK_END_DATE, taskEndDay);
        values.put(COLUMN_TASK_END_TIME, taskEndTime);
        values.put(COLUMN_TASK_PRIORITY, taskPriority);
        values.put(COLUMN_TASK_STATUS_ID, taskStatusId);
        values.put(COLUMN_TASK_TAG_ID, taskTagId);

        long taskId = db.insert(TABLE_TASK_NAME, null, values);
        db.close();
        return taskId;
    }



    public boolean updateTask(int taskId, String taskDescription,
                              String taskStartDay, String taskStartTime,
                              String taskEndDay, String taskEndTime, int taskTagId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK_DESCRIPTION, taskDescription);
        values.put(COLUMN_TASK_START_DATE, taskStartDay);
        values.put(COLUMN_TASK_START_TIME, taskStartTime);
        values.put(COLUMN_TASK_END_DATE, taskEndDay);
        values.put(COLUMN_TASK_END_TIME, taskEndTime);
        values.put(COLUMN_TASK_TAG_ID, taskTagId);

        int rowsAffected = db.update(TABLE_TASK_NAME, values,
                COLUMN_TASK_ID + "=?", new String[]{String.valueOf(taskId)});
        db.close();
        return rowsAffected > 0;
    }

    public boolean updateStatusTask(int taskId, int statusId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK_STATUS_ID, statusId);

        int rowsAffected = db.update(TABLE_TASK_NAME, values,
                COLUMN_TASK_ID + "=?", new String[]{String.valueOf(taskId)});
        db.close();
        return rowsAffected > 0;
    }

    public boolean deleteTask(int taskId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_TASK_NAME,
                COLUMN_TASK_ID + "=?", new String[]{String.valueOf(taskId)});
        db.close();
        return rowsAffected > 0;
    }










    // Add a new notification
    public long addNotification(int taskID, String title, String content, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTIFICATION_TASKID, taskID);
        values.put(COLUMN_NOTIFICATION_TITLE, title);
        values.put(COLUMN_NOTIFICATION_CONTENT, content);
        values.put(COLUMN_NOTIFICATION_TIME, time);

        long newRowId = db.insert(TABLE_NOTIFICATION_NAME, null, values);
        db.close();
        return newRowId;
    }

    // Get a single notification by ID
    public Notification getNotificationByNotiID(int notiID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NOTIFICATION_NAME, new String[]{
                        COLUMN_NOTIFICATION_ID, COLUMN_NOTIFICATION_TASKID, COLUMN_NOTIFICATION_TITLE,
                        COLUMN_NOTIFICATION_CONTENT, COLUMN_NOTIFICATION_TIME},
                COLUMN_NOTIFICATION_ID + "=?", new String[]{String.valueOf(notiID)},
                null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            Notification notification = createNotificationFromCursor(cursor);
            cursor.close();
            db.close();
            return notification;
        } else {
            db.close();
            return null;
        }
    }

    // Get all notifications
    public List<Notification> getAllNotifications() {
        List<Notification> notifications = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NOTIFICATION_NAME +
                " ORDER BY " + COLUMN_NOTIFICATION_ID + " DESC;";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Notification notification = createNotificationFromCursor(cursor);
                notifications.add(notification);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return notifications;
    }

    // Update a notification
    public int updateNotification(Notification notification) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTIFICATION_TASKID, notification.getTaskID());
        values.put(COLUMN_NOTIFICATION_TITLE, notification.getTitle());
        values.put(COLUMN_NOTIFICATION_CONTENT, notification.getContent());
        values.put(COLUMN_NOTIFICATION_TIME, notification.getTime());

        int rowsAffected = db.update(TABLE_NOTIFICATION_NAME, values,
                COLUMN_NOTIFICATION_ID + " = ?", new String[]{String.valueOf(notification.getNotiID())});
        db.close();
        return rowsAffected;
    }

    // Delete a notification
    public int deleteNotification(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_NOTIFICATION_NAME, COLUMN_NOTIFICATION_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected;
    }

    // Helper function to create Notification object from Cursor
    private Notification createNotificationFromCursor(Cursor cursor) {
        Integer notiID = safeGetInteger(cursor, COLUMN_NOTIFICATION_ID);
        Integer taskID = safeGetInteger(cursor, COLUMN_NOTIFICATION_TASKID);
        String title = safeGetString(cursor, COLUMN_NOTIFICATION_TITLE);
        String content = safeGetString(cursor, COLUMN_NOTIFICATION_CONTENT);
        String time = safeGetString(cursor, COLUMN_NOTIFICATION_TIME);

        if (notiID != null && taskID != null && title != null && content != null && time != null) {
            return new Notification(notiID, taskID, title, content, time);
        } else {
            throw new IllegalArgumentException("createNotificationFromCursor(): Thiếu một hoặc nhiều trường thiết yếu trong con trỏ");
        }
    }

    // Get notifications by title keyword
    public List<Notification> searchNotifications(String keyword) {
        List<Notification> notifications = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NOTIFICATION_NAME + " WHERE " + COLUMN_NOTIFICATION_TITLE + " LIKE '%" + keyword + "%' OR " + COLUMN_NOTIFICATION_CONTENT + " LIKE '%" + keyword + "%'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Notification notification = createNotificationFromCursor(cursor);
                notifications.add(notification);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return notifications;
    }

    // Delete all notifications
    public void deleteAllNotifications() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTIFICATION_NAME, null, null);
        db.close();
    }



    // Get notifications based on the time criteria
    public List<Notification> getNotificationsByTime(String timeCriteria) {
        List<Notification> notifications = new ArrayList<>();
        String selectQuery = "";

        String baseQuery = "SELECT * FROM " + TABLE_NOTIFICATION_NAME + " WHERE ";
        String dateCondition = "date(substr(" + COLUMN_NOTIFICATION_TIME + ", 7, 4) || '-' || " +
                "substr(" + COLUMN_NOTIFICATION_TIME + ", 4, 2) || '-' || " +
                "substr(" + COLUMN_NOTIFICATION_TIME + ", 1, 2))";
        String orderByClause = " ORDER BY " + dateCondition + " DESC, " +
                "time(substr(" + COLUMN_NOTIFICATION_TIME + ", 12, 2) || ':' || " +
                "substr(" + COLUMN_NOTIFICATION_TIME + ", 15, 2)) DESC, " +
                COLUMN_NOTIFICATION_ID + " DESC;";

        switch (timeCriteria) {
            case "today":
                selectQuery = baseQuery + dateCondition + " = date('now', '+0 days')" + orderByClause;
                break;
            case "past7days":
                selectQuery = baseQuery + dateCondition + " >= date('now', '-7 days')" + orderByClause;
                break;
            case "past30days":
                selectQuery = baseQuery + dateCondition + " >= date('now', '-30 days')" + orderByClause;
                break;
            default:
                throw new IllegalArgumentException("Invalid time criteria: " + timeCriteria);
        }
        Log.d(TAG, "selectQuery: " + selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Notification notification = createNotificationFromCursor(cursor);
                notifications.add(notification);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return notifications;
    }




}
