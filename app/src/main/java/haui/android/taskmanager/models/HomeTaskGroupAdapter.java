package haui.android.taskmanager.models;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import haui.android.taskmanager.R;

public class HomeTaskGroupAdapter  extends RecyclerView.Adapter<HomeTaskGroupAdapter.HomeTaskGroupAdapterViewHolder>{

    private List<Task> listTask;

    public HomeTaskGroupAdapter(List<Task> listTask) {
        this.listTask = listTask;
    }
    @NonNull
    @Override
    public HomeTaskGroupAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item_task_group, parent, false);
        return new HomeTaskGroupAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeTaskGroupAdapterViewHolder holder, int position) {
        Task task = listTask.get(position);
        if(task == null) return;
//        holder.home_custom_tag_group.setImageResource(task.getTagColor());
//        holder.home_ten_nhom_cv.setText(task.getTagName());
        holder.home_so_luong_cv.setText(listTask.size()+"");
        holder.home_progress_circular_task_group.setMax(100);
        holder.home_progress_circular_task_group.setProgress(80);
        holder.home_txt_progress_circular_task_group.setText("80%");
    }

    @Override
    public int getItemCount() {
        if(listTask != null) return listTask.size();
        return 0;
    }

    public class HomeTaskGroupAdapterViewHolder extends RecyclerView.ViewHolder{

        private ImageView home_custom_tag_group;
        private TextView home_ten_nhom_cv,home_so_luong_cv, home_txt_progress_circular_task_group;
        private ProgressBar home_progress_circular_task_group;

        public HomeTaskGroupAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            home_custom_tag_group = itemView.findViewById(R.id.home_custom_tag_group);
            home_ten_nhom_cv = itemView.findViewById(R.id.home_ten_nhom_cv);
            home_so_luong_cv = itemView.findViewById(R.id.home_so_luong_cv);
            home_txt_progress_circular_task_group = itemView.findViewById(R.id.home_txt_progress_circular_task_group);
            home_progress_circular_task_group = itemView.findViewById(R.id.home_progress_circular_task_group);

        }
    }
}
