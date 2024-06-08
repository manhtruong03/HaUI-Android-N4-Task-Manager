package haui.android.taskmanager.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import haui.android.taskmanager.R;
import haui.android.taskmanager.models.Task;

public class TaskViewAdapter extends BaseAdapter {
    private Context context;
    private List<Task> arrayListTask;

    public TaskViewAdapter(Context context, List<Task> data){
        this.context = context;
        this.arrayListTask = data;
    }

    @Override
    public int getCount() {
        return arrayListTask.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayListTask.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.item_list_task, parent, false);
        }

        Task currentTask = (Task) getItem(position);

        TextView task_txt_title = (TextView) convertView.findViewById(R.id.task_txt_title);
        TextView task_txt_decription = (TextView) convertView.findViewById(R.id.task_txt_decription);
        TextView task_txt_time = (TextView) convertView.findViewById(R.id.task_txt_time);

        task_txt_title.setText(currentTask.getTaskName());
        task_txt_decription.setText(currentTask.getDescription());
        task_txt_time.setText(currentTask.getStartTime() + " - " + currentTask.getStartDate() + " đến "+ currentTask.getEndTime() + " - " + currentTask.getEndDate());

        return convertView;
    }
}
