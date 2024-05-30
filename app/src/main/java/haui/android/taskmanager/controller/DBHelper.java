package haui.android.taskmanager.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import haui.android.taskmanager.models.Status;
import haui.android.taskmanager.models.Task;

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
        initialData(db);

        if (!isTableExists(db, TABLE_TAG_NAME)) {
            String createCategoryTableStatement = String.format(
                    "CREATE TABLE %s (" +
                            "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "%s TEXT, " +
                            "%s TEXT)",
                    TABLE_TAG_NAME, COLUMN_TAG_ID, COLUMN_TAG_NAME, COLUMN_TAG_COLOR
            );
            db.execSQL(createCategoryTableStatement);
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
    }

    private void initialData(SQLiteDatabase db) {
        insertStatus(db, "Cần làm");
        insertStatus(db, "Đang làm");
        insertStatus(db, "Hoàn thành");
        insertStatus(db, "Muộn");
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

    public void deleteAllStatusData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_STATUS_NAME, null, null);
        db.close();
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
            throw new IllegalArgumentException("One or more essential fields are missing in the cursor.");
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
}
