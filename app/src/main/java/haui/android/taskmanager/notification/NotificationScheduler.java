package haui.android.taskmanager.notification;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import haui.android.taskmanager.R;
import haui.android.taskmanager.test.TestNotificationActivity;

public class NotificationScheduler {

    private Context context;

    public NotificationScheduler(Context context) {
        this.context = context;
    }

    public void createNotificationChannel(String channelId, String channelName, String channelDescription, int importance) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            channel.setDescription(channelDescription);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void cancelNotification(int notificationId) {
        Intent intent = new Intent(context, ReminderTwoActionBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }


    public void scheduleNotification(String channelId, int notificationId, String title, String contentText, long delayInMillis) {
        Intent intent = new Intent(context, ReminderBroadcast.class);
        intent.putExtra("channelId", channelId);
        intent.putExtra("title", title);
        intent.putExtra("contentText", contentText);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        long triggerAtMillis = System.currentTimeMillis() + delayInMillis;
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
    }

    public void scheduleNotificationWithTwoActions(String channelId, int notificationId, String title, String contentText, long delayInMillis) {
        Intent intent = new Intent(context, ReminderTwoActionBroadcast.class);
        intent.putExtra("channelId", channelId);
        intent.putExtra("title", title);
        intent.putExtra("contentText", contentText);
        intent.putExtra("notificationId", notificationId);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        long triggerAtMillis = System.currentTimeMillis() + delayInMillis;
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
    }


}
