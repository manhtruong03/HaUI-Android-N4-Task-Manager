package haui.android.taskmanager.views;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import haui.android.taskmanager.MainActivity;
import haui.android.taskmanager.R;
import haui.android.taskmanager.controller.DBHelper;
import haui.android.taskmanager.models.HomeListTag;
import haui.android.taskmanager.models.Tag;
import haui.android.taskmanager.models.TaskDetail;

public class HomeTaskGroupAdapter  extends RecyclerView.Adapter<HomeTaskGroupAdapter.HomeTaskGroupAdapterViewHolder>{

    private List<TaskDetail> listTask;
    private int amountTag;
    private List<HomeListTag> allListTaskDetail;
    DBHelper dbHelper;
    private MainActivity activity;

    public interface ICickHomeListTag {
        void onItemClick(HomeListTag homeListTag);
    }

    private ICickHomeListTag listener;

    public HomeTaskGroupAdapter(List<TaskDetail> listTask, int amountTag, List<HomeListTag> allListTaskDetail, MainActivity activity, ICickHomeListTag listener) {
        this.listTask = listTask;
        this.amountTag = amountTag;
        this.listener = listener;
        this.activity = activity;
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
            case "#FF0033":
                holder.home_custom_tag_group.setImageResource(R.drawable.home_border_tag_red);// dung switchcase de chon mau
                break;
            case "#0033FF":
                holder.home_custom_tag_group.setImageResource(R.drawable.home_border_tag_blue);// dung switchcase de chon mau
                break;
            case "#33FF99":
                holder.home_custom_tag_group.setImageResource(R.drawable.home_border_tag_green);// dung switchcase de chon mau
                break;
            case "#FFCC33":
                holder.home_custom_tag_group.setImageResource(R.drawable.home_border_tag_yellow);// dung switchcase de chon mau
                break;
            case "#800080":
                holder.home_custom_tag_group.setImageResource(R.drawable.home_border_tag_purple);// dung switchcase de chon mau
                break;
            case "#FFD0D0":
                holder.home_custom_tag_group.setImageResource(R.drawable.home_border_tag_pink);// dung switchcase de chon mau
                break;
            case "#FF6600":
                holder.home_custom_tag_group.setImageResource(R.drawable.home_border_tag_orange);// dung switchcase de chon mau
                break;
            default:
                holder.home_custom_tag_group.setImageResource(R.drawable.home_border_tag_green_blue);// dung switchcase de chon mau
        }
        holder.home_ten_nhom_cv.setText(homeListTag1.getTaskDetailList().get(0).getTag().getTagName());
        int amountAll = allListTaskDetail.get(position).getTaskDetailList().size();
        holder.home_so_luong_cv.setText(amountAll+" công việc");
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
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(homeListTag1);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (homeListTag1 != null && position >= 0 && position < allListTaskDetail.size()) {
                    HomeListTag selectedHomeListTag = homeListTag1;
                    dbHelper = new DBHelper(v.getContext());

                    List<Integer> taskIDsToDelete = new ArrayList<>();
                    for (TaskDetail taskDetail : selectedHomeListTag.getTaskDetailList()) {
                        taskIDsToDelete.add(taskDetail.getTask().getTaskID());
                    }

                    // You can implement a custom dialog or logic for confirmation here
                    new AlertDialog.Builder(activity)
                            .setTitle("Delete Confirmation")
                            .setMessage("Bạn có chắc chắn xóa nhóm công việc này không?")
                            .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                                for(int i = 0; i < taskIDsToDelete.size(); i++){
                                    dbHelper.deleteTask(taskIDsToDelete.get(i));
                                }
                                // Update the adapter data and UI
                                allListTaskDetail.remove(position);
//                                homeAdapter.notifyDataSetChanged();
                                Fragment editTaskFragment = new HomeFragment();
                                if (editTaskFragment != null) {
                                    FragmentManager fragmentManager = activity.getSupportFragmentManager();
                                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                                    transaction.replace(R.id.fragment_container, editTaskFragment)
                                            .addToBackStack(null)
                                            .commit();
                                } else {
                                    Toast.makeText(v.getContext(), "Failed to create EditTaskFragment", Toast.LENGTH_SHORT).show();
                                }
//                                home_so_luong_nhom.setText(mListTaskInprogress.size());
                                Toast.makeText(activity, "Xóa thành công.", Toast.LENGTH_SHORT).show();
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_delete)
                            .show();
                } else {
                    Toast.makeText(v.getContext(), "Invalid task detail position", Toast.LENGTH_SHORT).show();
                }
                return true;
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
