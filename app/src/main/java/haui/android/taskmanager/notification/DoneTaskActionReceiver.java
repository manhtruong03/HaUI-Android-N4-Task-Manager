package haui.android.taskmanager.notification;

import static android.content.ContentValues.TAG;
import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import haui.android.taskmanager.controller.DBHelper;
import haui.android.taskmanager.models.Task;
import haui.android.taskmanager.views.EditTaskFragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import android.app.PendingIntent;

import haui.android.taskmanager.R;

public class DoneTaskActionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        int taskID = intent.getIntExtra("notificationId", 0) - 1000;
        if (taskID > 500) { taskID -= 999; }

        DBHelper dbHelper = new DBHelper(context);
        Task task = dbHelper.getTaskById(taskID);

        if(task.getStatusID() == 3) {
            return;
        }

        String stringDate = task.getEndDate() + " " + task.getEndTime();
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        try {
            Date dateEnd = dateTimeFormat.parse(stringDate);
            Date dateNow = new Date();

            if(dateEnd.before(dateNow)){
                dbHelper.updateStatusTask(task.getTaskID(), 4);
            }else{
                dbHelper.updateStatusTask(task.getTaskID(), 3);
            }

            // Xóa thông báo cũ trước khi cập nhật tác vụ
            NotificationScheduler notificationScheduler = new NotificationScheduler(context);
            notificationScheduler.cancelNotification(taskID + 1000);
            notificationScheduler.cancelNotification(taskID + 1999);

            // Xử lý việc nhấn nút
            String channelId = intent.getStringExtra("channelId");
            String title = "[Hoàn thành] " + task.getTaskName();
            String contentText = task.getDescription();

            // Lên lịch thông báo hoàn thành trong 100 mili giây
            NotificationScheduler scheduler = new NotificationScheduler(context);
            scheduler.scheduleNotification(channelId, taskID + 1000, title, contentText, (long)100);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }
}
