package haui.android.taskmanager.models;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import haui.android.taskmanager.R;

public class HomeInprogressAdapter extends RecyclerView.Adapter<HomeInprogressAdapter.HomeViewHolder> {
    private List<Task> mListTask;

    public HomeInprogressAdapter(List<Task> mListTask) {
        this.mListTask = mListTask;
    }


    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item_inprogress,parent, false);
        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        Task home = mListTask.get(position);
        if(home == null) return;
//        holder.home_img_tag.setImageResource(home.getTagColor());
        holder.home_name.setText(home.getTaskName());
        holder.home_description.setText(home.getDescription());
    }

    @Override
    public int getItemCount() {
        if(mListTask != null) return mListTask.size();
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
