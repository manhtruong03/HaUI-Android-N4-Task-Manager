package haui.android.taskmanager.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import haui.android.taskmanager.R;
import haui.android.taskmanager.models.Notification;

public class NotificationViewAdapter extends ArrayAdapter<Notification> {
    private Context context;
    private List<Notification> notifications;

    public NotificationViewAdapter(Context context, List<Notification> notifications) {
        super(context, 0, notifications);
        this.context = context;
        this.notifications = notifications;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Notification notification = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false);
        }

        TextView titleView = convertView.findViewById(R.id.notification_title);
        TextView contentView = convertView.findViewById(R.id.notification_content);
        TextView timeView = convertView.findViewById(R.id.notification_time);

        titleView.setText(notification.getTitle());
        contentView.setText(notification.getContent());
        timeView.setText(notification.getTime());

        return convertView;
    }
}

