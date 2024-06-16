package haui.android.taskmanager.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import android.app.PendingIntent;

import java.text.SimpleDateFormat;
import java.util.Date;

import haui.android.taskmanager.R;
import haui.android.taskmanager.controller.DBHelper;

public class ReminderTwoActionBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String channelId = intent.getStringExtra("channelId");
        String title = intent.getStringExtra("title");
        String contentText = intent.getStringExtra("contentText");
        int notificationId = intent.getIntExtra("notificationId", 0);


        int taskID = notificationId - 1000;
        taskID = (taskID > 500) ? taskID - 999 : taskID;

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String timeNow = sdf.format(new Date());

        DBHelper dbHelper = new DBHelper(context);
        dbHelper.addNotification(taskID, title, contentText, timeNow);


        // Create pending intents for actions
        Intent repeatIntent = new Intent(context, RepeatNotiActionReceiver.class);
        repeatIntent.putExtra("channelId", channelId);
        repeatIntent.putExtra("title", title);
        repeatIntent.putExtra("contentText", contentText);
        repeatIntent.putExtra("notificationId", notificationId);

        PendingIntent repeatPendingIntent = PendingIntent.getBroadcast(context, notificationId, repeatIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Intent doneTaskIntent = new Intent(context, DoneTaskActionReceiver.class);
        doneTaskIntent.putExtra("channelId", channelId);
        doneTaskIntent.putExtra("title", title);
        doneTaskIntent.putExtra("contentText", contentText);
        doneTaskIntent.putExtra("notificationId", notificationId);

        PendingIntent doneTaskPendingIntent = PendingIntent.getBroadcast(context, notificationId, doneTaskIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);


        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.notification_ic_notification)
                .setContentTitle(title)
                .setContentText(contentText)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .addAction(R.drawable.edit_task_ic_time, "Hoàn thành", doneTaskPendingIntent)
                .addAction(R.drawable.edit_task_ic_time, "Nhắc lại (5s)", repeatPendingIntent);

        // Show the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notificationManager.notify(notificationId, builder.build());
    }
}
