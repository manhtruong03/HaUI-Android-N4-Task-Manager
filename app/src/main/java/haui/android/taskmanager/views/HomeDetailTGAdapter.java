package haui.android.taskmanager.views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import haui.android.taskmanager.R;
import haui.android.taskmanager.models.TaskDetail;

public class HomeDetailTGAdapter extends RecyclerView.Adapter<HomeDetailTGAdapter.HomeDetailHolder> {
    private List<TaskDetail> taskDetails;

    public HomeDetailTGAdapter(List<TaskDetail> taskDetails) {
        this.taskDetails = taskDetails;
    }

    @NonNull
    @Override
    public HomeDetailHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item_hometaglist, parent, false);
        return new HomeDetailHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeDetailHolder holder, int position) {
        TaskDetail taskDetail = taskDetails.get(position);
        if(taskDetail == null) return;
        String color = taskDetail.getTag().getTagColor();
        switch (color) {
            case "#FF0033":
                holder.home_custom_tag_group_hometaglist.setImageResource(R.drawable.home_border_tag_red);// dung switchcase de chon mau
                break;
            case "#0033FF":
                holder.home_custom_tag_group_hometaglist.setImageResource(R.drawable.home_border_tag_blue);// dung switchcase de chon mau
                break;
            case "#33FF99":
                holder.home_custom_tag_group_hometaglist.setImageResource(R.drawable.home_border_tag_green);// dung switchcase de chon mau
                break;
            case "#FFCC33":
                holder.home_custom_tag_group_hometaglist.setImageResource(R.drawable.home_border_tag_yellow);// dung switchcase de chon mau
                break;
            case "#800080":
                holder.home_custom_tag_group_hometaglist.setImageResource(R.drawable.home_border_tag_purple);// dung switchcase de chon mau
                break;
            case "#FFD0D0":
                holder.home_custom_tag_group_hometaglist.setImageResource(R.drawable.home_border_tag_pink);// dung switchcase de chon mau
                break;
            case "#FF6600":
                holder.home_custom_tag_group_hometaglist.setImageResource(R.drawable.home_border_tag_orange);// dung switchcase de chon mau
                break;
            default:
                holder.home_custom_tag_group_hometaglist.setImageResource(R.drawable.home_border_tag_green_blue);// dung switchcase de chon mau
        }
        holder.home_thoi_gian_congviec_hometaglist.setText(taskDetail.getTask().getStartTime() + " - " + taskDetail.getTask().getStartDate());
        holder.home_ten_congviec_hometaglist.setText(taskDetail.getTask().getTaskName());
        String status = "";
        int statusId = taskDetail.getStatus().getStatusID();
        switch (statusId){
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
        holder.home_txt_progress_task_group_hometaglist.setText(status);
    }

    @Override
    public int getItemCount() {
        if(taskDetails != null) return taskDetails.size();
        return 0;
    }

    public class HomeDetailHolder extends RecyclerView.ViewHolder{

        private ImageView home_custom_tag_group_hometaglist;
        private TextView home_thoi_gian_congviec_hometaglist, home_ten_congviec_hometaglist, home_txt_progress_task_group_hometaglist;

        public HomeDetailHolder(@NonNull View itemView) {
            super(itemView);

            home_custom_tag_group_hometaglist = itemView.findViewById(R.id.home_custom_tag_group_hometaglist);
            home_thoi_gian_congviec_hometaglist = itemView.findViewById(R.id.home_thoi_gian_congviec_hometaglist);
            home_ten_congviec_hometaglist = itemView.findViewById(R.id.home_ten_congviec_hometaglist);
            home_txt_progress_task_group_hometaglist = itemView.findViewById(R.id.home_txt_progress_task_group_hometaglist);
        }
    }
}
