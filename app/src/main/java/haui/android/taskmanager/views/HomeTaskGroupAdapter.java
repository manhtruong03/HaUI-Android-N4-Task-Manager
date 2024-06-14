package haui.android.taskmanager.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import haui.android.taskmanager.R;
import haui.android.taskmanager.controller.DBHelper;
import haui.android.taskmanager.models.HomeListTag;
import haui.android.taskmanager.models.Tag;
import haui.android.taskmanager.models.TaskDetail;

public class HomeTaskGroupAdapter  extends RecyclerView.Adapter<HomeTaskGroupAdapter.HomeTaskGroupAdapterViewHolder>{

    private List<TaskDetail> listTask;
    private List<TaskDetail> taskDetails;
    private List<Tag> tagList;
    private int amountTag;
    private List<HomeListTag> allListTaskDetail;
    DBHelper dbHelper1;
    SQLiteDatabase db;
    Context context;
    public interface ICickHomeListTag {
        void onItemClick(HomeListTag homeListTag);
    }

    private ICickHomeListTag listener;
//    public List<HomeListTag> Classify(List<TaskDetail> listTask){
//        db = this.dbHelper1.getWritableDatabase();
//        for(int i = 0; i < listTask.size(); i++) {
//            TaskDetail taskDetail = listTask.get(i);
//            for(int k = 0; k < tagList.size(); k++) {
//                if(tagList.get(k).getTagID() == taskDetail.getTask().getTagID()) {
//                    taskDetails = dbHelper1.getAllTaskDetailByTagId(tagList.get(k).getTagID());
//                    HomeListTag homeListTag = new HomeListTag(tagList.get(k).getTagID(), taskDetails);
////                    HomeListTag homeListTag = dbHelper1.getHomeListTag(tagList.get(k).getTagID());
//                    allListTaskDetail.add(homeListTag);
//                }
//            }
//        }
//        return allListTaskDetail;
//    }
    public HomeTaskGroupAdapter(List<TaskDetail> listTask, int amountTag, List<HomeListTag> allListTaskDetail, ICickHomeListTag listener) {
        this.listTask = listTask;
        this.amountTag = amountTag;
        this.listener = listener;
        this.allListTaskDetail = allListTaskDetail;
    }
    @NonNull
    @Override
    public HomeTaskGroupAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item_task_group, parent, false);
        return new HomeTaskGroupAdapterViewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull HomeTaskGroupAdapterViewHolder holder, int position) {
        HomeListTag homeListTag1 = allListTaskDetail.get(position); // Đây là một danh sánh con trong allListTaskDetail
        if(homeListTag1 == null) return;
        String color = allListTaskDetail.get(position).getTaskDetailList().get(0).getTag().getTagColor();
        switch (color) {
            case "Red":
                holder.home_custom_tag_group.setImageResource(R.drawable.home_border_tag_red);// dung switchcase de chon mau
                break;
            case "Blue":
                holder.home_custom_tag_group.setImageResource(R.drawable.home_border_tag_blue);// dung switchcase de chon mau
                break;
            case "Green":
                holder.home_custom_tag_group.setImageResource(R.drawable.home_border_tag_green);// dung switchcase de chon mau
                break;
            case "Yellow":
                holder.home_custom_tag_group.setImageResource(R.drawable.home_border_tag_yellow);// dung switchcase de chon mau
                break;
            case "Purple":
                holder.home_custom_tag_group.setImageResource(R.drawable.home_border_tag_purple);// dung switchcase de chon mau
                break;
            case "pink":
                holder.home_custom_tag_group.setImageResource(R.drawable.home_border_tag_pink);// dung switchcase de chon mau
                break;
            case "orange":
                holder.home_custom_tag_group.setImageResource(R.drawable.home_border_tag_orange);// dung switchcase de chon mau
                break;
            default:
                holder.home_custom_tag_group.setImageResource(R.drawable.home_border_tag_green_blue);// dung switchcase de chon mau
        }
        holder.home_ten_nhom_cv.setText(homeListTag1.getTaskDetailList().get(0).getTag().getTagName());
        int amountAll = allListTaskDetail.get(position).getTaskDetailList().size();
        holder.home_so_luong_cv.setText(amountAll+" nhiệm vụ");
        List<TaskDetail> doneHomeListTag = new ArrayList<>();

        for(int i = 0; i < amountAll; i++) {
            if(homeListTag1.getTaskDetailList().get(i).getStatus().getStatusID() == 3){
                doneHomeListTag.add(homeListTag1.getTaskDetailList().get(i));
            }
        }
        double tyLe = ((double)doneHomeListTag.size()/(double)amountAll) * 100;
        int x = (int) tyLe;
        holder.home_progress_circular_task_group.setMax(100);
        holder.home_progress_circular_task_group.setProgress(x);
        holder.home_txt_progress_circular_task_group.setText(x+"%");

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (listener != null) { // Check if listener is set (if using interface)
//                    listener.onItemClick(allListTaskDetail.get(position));
//                } else {
//                    // Handle item click directly here (without interface)
//                    // You can access the HomeListTag object using allListTaskDetail.get(position)
//                }
//            }
//        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(homeListTag1);
            }
        });
    }

    public void setOnItemClickListener(ICickHomeListTag listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        if(listTask != null) return amountTag;
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
