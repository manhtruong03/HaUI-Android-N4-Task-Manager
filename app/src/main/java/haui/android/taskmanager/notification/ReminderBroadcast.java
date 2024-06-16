package haui.android.taskmanager.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.text.SimpleDateFormat;
import java.util.Date;

import haui.android.taskmanager.R;
import haui.android.taskmanager.controller.DBHelper;

public class ReminderBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String channelId = intent.getStringExtra("channelId");
        String title = intent.getStringExtra("title");
        String contentText = intent.getStringExtra("contentText");

        int taskID = intent.getIntExtra("notificationId", 0) - 1000;
        taskID = (taskID > 500) ? taskID - 999 : taskID;

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String timeNow = sdf.format(new Date());

        DBHelper dbHelper = new DBHelper(context);
        dbHelper.addNotification(taskID, title, contentText, timeNow);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.notification_ic_notification)
                .setContentTitle(title)
                .setContentText(contentText)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notificationManager.notify(intent.getIntExtra("notificationId", 0), builder.build());
    }
}
