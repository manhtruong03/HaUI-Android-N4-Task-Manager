package haui.android.taskmanager.views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import haui.android.taskmanager.R;
import haui.android.taskmanager.models.TaskDetail;

public class HomeInprogressAdapter extends RecyclerView.Adapter<HomeInprogressAdapter.HomeViewHolder> {
    private List<TaskDetail> mListTask;

    private List<TaskDetail> mListTaskInprogress = new ArrayList<>();

    public void Classify(){
        for(int i = 0; i < mListTask.size(); i++) {
            TaskDetail taskDetail = mListTask.get(i);
            if(taskDetail.getStatus().getStatusID() == 2) mListTaskInprogress.add(taskDetail);
        }
    }

    public HomeInprogressAdapter(List<TaskDetail> mListTask) {
        this.mListTask = mListTask;
        Classify();
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item_inprogress,parent, false);
        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        TaskDetail home = mListTaskInprogress.get(position);
        if(home == null) return;
        String color = home.getTag().getTagColor();
        switch (color) {
            case "Red":
                holder.home_img_tag.setImageResource(R.drawable.home_border_tag_red);// dung switchcase de chon mau
                break;
            case "Blue":
                holder.home_img_tag.setImageResource(R.drawable.home_border_tag_blue);// dung switchcase de chon mau
                break;
            case "Green":
                holder.home_img_tag.setImageResource(R.drawable.home_border_tag_green);// dung switchcase de chon mau
                break;
            case "Yellow":
                holder.home_img_tag.setImageResource(R.drawable.home_border_tag_yellow);// dung switchcase de chon mau
                break;
            case "Purple":
                holder.home_img_tag.setImageResource(R.drawable.home_border_tag_purple);// dung switchcase de chon mau
                break;
            case "pink":
                holder.home_img_tag.setImageResource(R.drawable.home_border_tag_pink);// dung switchcase de chon mau
                break;
            case "orange":
                holder.home_img_tag.setImageResource(R.drawable.home_border_tag_orange);// dung switchcase de chon mau
                break;
            default:
                holder.home_img_tag.setImageResource(R.drawable.home_border_tag_green_blue);// dung switchcase de chon mau
        }
        holder.home_name.setText(home.getTask().getTaskName());
        holder.home_description.setText(home.getTask().getDescription());
    }

    @Override
    public int getItemCount() {
        if(mListTaskInprogress != null) return mListTaskInprogress.size();
        return 0;
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder {

        private ImageView home_img_tag;
        private TextView home_name;
        private TextView home_description;
        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);

            home_img_tag = itemView.findViewById(R.id.home_custom_shape);
            home_name = itemView.findViewById(R.id.home_txtTieuDeCV);
            home_description = itemView.findViewById(R.id.home_txtTenCV);
        }
    }
}
