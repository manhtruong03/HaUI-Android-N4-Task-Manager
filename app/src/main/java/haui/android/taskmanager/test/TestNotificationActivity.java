package haui.android.taskmanager.test;

import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import haui.android.taskmanager.notification.NotificationScheduler;
import haui.android.taskmanager.R;
import haui.android.taskmanager.views.EditTaskFragment;

public class TestNotificationActivity extends AppCompatActivity {

    private NotificationScheduler notificationScheduler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        notificationScheduler = new NotificationScheduler(this);
        notificationScheduler.createNotificationChannel("notifyTCM", "Kênh thông báo của Mạnh", "Tác giả: Trương Công Mạnh-2021601910", NotificationManager.IMPORTANCE_DEFAULT);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificationScheduler.scheduleNotificationWithTwoActions("notifyTCM", 101,"APP CUA MANH", "Xin chào, tôi là Mạnh", 5 * 1000);
            }
        });
        Toast.makeText(this, "hello 0", Toast.LENGTH_SHORT).show();
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Toast.makeText(this, "hello 1", Toast.LENGTH_SHORT).show();
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        Toast.makeText(this, "hello 2", Toast.LENGTH_SHORT).show();
        if (intent != null && intent.hasExtra("messageId")) {
            String messageId = intent.getStringExtra("messageId");
            Bundle data = new Bundle();
            data.putString("messageId", messageId);
            Fragment editTaskFragment = EditTaskFragment.newInstance("2");     // String.valueOf(data)
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, editTaskFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }
}
