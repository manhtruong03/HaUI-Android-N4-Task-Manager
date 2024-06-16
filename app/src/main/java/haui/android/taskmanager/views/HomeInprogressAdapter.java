package haui.android.taskmanager.views;

import static haui.android.taskmanager.views.HomeFragment.homeAdapter;
import static haui.android.taskmanager.views.HomeFragment.home_so_luong_nhom;

import android.app.Activity;
import android.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import haui.android.taskmanager.models.TaskDetail;

public class HomeInprogressAdapter extends RecyclerView.Adapter<HomeInprogressAdapter.HomeViewHolder> {
    private List<TaskDetail> mListTask;

    private List<TaskDetail> mListTaskInprogress = new ArrayList<>();
    private MainActivity activity;
    DBHelper dbHelper;

    public void Classify(){
        for(int i = 0; i < mListTask.size(); i++) {
            TaskDetail taskDetail = mListTask.get(i);
            if(taskDetail.getStatus().getStatusID() == 2) mListTaskInprogress.add(taskDetail);
        }
    }

    public HomeInprogressAdapter(List<TaskDetail> mListTask, MainActivity activity) {
        this.mListTask = mListTask;
        this.activity = activity;
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
            case "#FF0033":
                holder.home_img_tag.setImageResource(R.drawable.home_border_tag_red);// dung switchcase de chon mau
                break;
            case "#0033FF":
                holder.home_img_tag.setImageResource(R.drawable.home_border_tag_blue);// dung switchcase de chon mau
                break;
            case "#33FF99":
                holder.home_img_tag.setImageResource(R.drawable.home_border_tag_green);// dung switchcase de chon mau
                break;
            case "#FFCC33":
                holder.home_img_tag.setImageResource(R.drawable.home_border_tag_yellow);// dung switchcase de chon mau
                break;
            case "#800080":
                holder.home_img_tag.setImageResource(R.drawable.home_border_tag_purple);// dung switchcase de chon mau
                break;
            case "#FFD0D0":
                holder.home_img_tag.setImageResource(R.drawable.home_border_tag_pink);// dung switchcase de chon mau
                break;
            case "#FF6600":
                holder.home_img_tag.setImageResource(R.drawable.home_border_tag_orange);// dung switchcase de chon mau
                break;
            default:
                holder.home_img_tag.setImageResource(R.drawable.home_border_tag_green_blue);// dung switchcase de chon mau
        }
        holder.home_name.setText(home.getTask().getTaskName());
        holder.home_description.setText(home.getTask().getDescription());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListTaskInprogress != null && position >= 0 && position < mListTaskInprogress.size()) {
                    TaskDetail selectedTaskDetail = mListTaskInprogress.get(position);
                    String taskID = String.valueOf(selectedTaskDetail.getTask().getTaskID());
                    Log.d("ID task", "onItemClick: " + taskID);

                    // Assuming you have an EditTaskFragment class
                    Fragment editTaskFragment = EditTaskFragment.newInstance(taskID);
                    if (editTaskFragment != null) {
                        FragmentManager fragmentManager = activity.getSupportFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.fragment_container, editTaskFragment)
                                .addToBackStack(null)
                                .commit();
                    } else {
                        Toast.makeText(v.getContext(), "Failed to create EditTaskFragment", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(v.getContext(), "Invalid task detail position", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (home != null && position >= 0 && position < mListTaskInprogress.size()) {
                    TaskDetail selectedTaskDetail = home;
                    int taskID = selectedTaskDetail.getTask().getTaskID();
                    dbHelper = new DBHelper(v.getContext());

                    // You can implement a custom dialog or logic for confirmation here
                    new AlertDialog.Builder(activity)
                            .setTitle("Delete Confirmation")
                            .setMessage("Bạn có chắc chắn xóa công việc này không?")
                            .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                                dbHelper.deleteTask(taskID);

                                // Update the adapter data and UI
                                mListTaskInprogress.removeIf(taskDetail -> taskDetail.getTask().getTaskID() == taskID);
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
                return false;
            }
        });
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
