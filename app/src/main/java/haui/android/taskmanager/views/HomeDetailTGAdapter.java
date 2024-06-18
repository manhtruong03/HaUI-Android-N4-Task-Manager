package haui.android.taskmanager.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import haui.android.taskmanager.R;
import haui.android.taskmanager.models.TaskDetail;

public class HomeDetailTGAdapter extends ArrayAdapter<TaskDetail> {

    private final List<TaskDetail> taskDetails;

    public HomeDetailTGAdapter(Context context, List<TaskDetail> taskDetails) {
        super(context, R.layout.home_item_hometaglist, taskDetails); // Use custom layout for ListView items
        this.taskDetails = taskDetails;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TaskDetail taskDetail = taskDetails.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.home_item_hometaglist, parent, false);
        }

        ImageView homeCustomTagGroupHometaglist = convertView.findViewById(R.id.home_custom_tag_group_hometaglist);
        TextView homeThoiGianCongViecHometaglist = convertView.findViewById(R.id.home_thoi_gian_congviec_hometaglist);
        TextView homeTenCongViecHometaglist = convertView.findViewById(R.id.home_ten_congviec_hometaglist);
        TextView homeTxtProgressTaskGroupHometaglist = convertView.findViewById(R.id.home_txt_progress_task_group_hometaglist);
        TextView homeTxtDecription = convertView.findViewById(R.id.home_txt_decription);

        if (taskDetail != null) {
            String color = taskDetail.getTag().getTagColor();
            switch (color) {
                case "#FF0033":
                    homeCustomTagGroupHometaglist.setImageResource(R.drawable.home_border_tag_red);
                    break;
                case "#0033FF":
                    homeCustomTagGroupHometaglist.setImageResource(R.drawable.home_border_tag_blue);
                    break;
                case "#33FF99":
                    homeCustomTagGroupHometaglist.setImageResource(R.drawable.home_border_tag_green);
                    break;
                case "#FFCC33":
                    homeCustomTagGroupHometaglist.setImageResource(R.drawable.home_border_tag_yellow);
                    break;
                case "#800080":
                    homeCustomTagGroupHometaglist.setImageResource(R.drawable.home_border_tag_purple);
                    break;
                case "#FFD0D0":
                    homeCustomTagGroupHometaglist.setImageResource(R.drawable.home_border_tag_pink);
                    break;
                case "#FF6600":
                    homeCustomTagGroupHometaglist.setImageResource(R.drawable.home_border_tag_orange);
                    break;
                default:
                    homeCustomTagGroupHometaglist.setImageResource(R.drawable.home_border_tag_green_blue);
            }
            homeThoiGianCongViecHometaglist.setText(taskDetail.getTask().getStartTime() + " - " + taskDetail.getTask().getStartDate() + " đến " + taskDetail.getTask().getEndTime() + " - " + taskDetail.getTask().getEndDate());
            homeTenCongViecHometaglist.setText(taskDetail.getTask().getTaskName());
            homeTxtDecription.setText(taskDetail.getTask().getDescription());

            String status = "";
            int statusId = taskDetail.getStatus().getStatusID();
            switch (statusId) {
                case 1:
                    status = "Cần làm";
                    break;
                case 2:
                    status = "Đang làm";
                    break;
                case 3:
                    status = "Hoàn thành";
                    break;
                case 4:
                    status = "Muộn";
                    break;
                default:
                    status = "";
            }
            homeTxtProgressTaskGroupHometaglist.setText(status);
        }

        return convertView;
    }
}
