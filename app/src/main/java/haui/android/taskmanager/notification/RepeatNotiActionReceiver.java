package haui.android.taskmanager.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class RepeatNotiActionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Handle the button press
        String channelId = intent.getStringExtra("channelId");
        String title = "[Nhắc lại] " + intent.getStringExtra("title");
        String contentText = intent.getStringExtra("contentText");

        // Schedule a new notification for 5 seconds later
        NotificationScheduler scheduler = new NotificationScheduler(context);
        scheduler.scheduleNotification(channelId, intent.getIntExtra("notificationId", 0), title, contentText, (long)3000);
    }
}
