package haui.android.taskmanager.views;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;

import haui.android.taskmanager.R;
import haui.android.taskmanager.controller.DBHelper;
import haui.android.taskmanager.models.HomeListTag;
import haui.android.taskmanager.models.TaskDetail;
import haui.android.taskmanager.notification.NotificationScheduler;

public class HomeDetailTGFragment extends Fragment {

    private ListView rvcHomeListTag;
    HomeListTag homeListTag;

    HomeDetailTGAdapter homeDetailTGAdapter;
    TextView home_text_ten_nhom_cv;
    DBHelper dbHelper;

    public HomeDetailTGFragment(){
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_home_detail_t_g, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvcHomeListTag = view.findViewById(R.id.home_list_view_hometaglist);
        home_text_ten_nhom_cv = view.findViewById(R.id.home_ten_nhom_cv_hometaglist);

        Bundle bundleReceive = getArguments();
        if(bundleReceive != null){
            homeListTag = (HomeListTag) bundleReceive.get("object_hometaglist");
            if(homeListTag != null){
                home_text_ten_nhom_cv.setText(homeListTag.getTaskDetailList().get(0).getTag().getTagName());
                List<TaskDetail> taskDetails = homeListTag.getTaskDetailList();
                homeDetailTGAdapter = new HomeDetailTGAdapter(getContext(), taskDetails); // Use Context for ListView adapter
                rvcHomeListTag.setAdapter(homeDetailTGAdapter);
                rvcHomeListTag.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (taskDetails != null && position >= 0 && position < taskDetails.size()) {
                            TaskDetail selectedTaskDetail = taskDetails.get(position);
                            String taskID = String.valueOf(selectedTaskDetail.getTask().getTaskID());
                            Log.d("ID task", "onItemClick: " + taskID);

                            // Assuming you have an EditTaskFragment class
                            Fragment editTaskFragment = EditTaskFragment.newInstance(taskID);
                            if (editTaskFragment != null) {
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                FragmentTransaction transaction = fragmentManager.beginTransaction();
                                transaction.replace(R.id.fragment_container, editTaskFragment)
                                        .addToBackStack(null)
                                        .commit();
                            } else {
                                Toast.makeText(getContext(), "Failed to create EditTaskFragment", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "Invalid task detail position", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                rvcHomeListTag.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        if (taskDetails != null && position >= 0 && position < taskDetails.size()) {
                            TaskDetail selectedTaskDetail = taskDetails.get(position);
                            int taskID = selectedTaskDetail.getTask().getTaskID();
                            dbHelper = new DBHelper(getContext());

                            // You can implement a custom dialog or logic for confirmation here
                            new AlertDialog.Builder(getActivity())
                                    .setTitle("Xác nhận xóa")
                                    .setMessage("Bạn có chắc chắn xóa công việc này không?")
                                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                                        dbHelper.deleteTask(taskID);

                                        // Update the adapter data and UI
                                        taskDetails.removeIf(taskDetail -> taskDetail.getTask().getTaskID() == taskID);
                                        homeDetailTGAdapter.notifyDataSetChanged();

                                        Toast.makeText(getActivity(), "Xóa thành công.", Toast.LENGTH_SHORT).show();
                                        // Xóa thông báo cũ
                                        NotificationScheduler notificationScheduler = new NotificationScheduler(requireContext());
                                        notificationScheduler.cancelNotification(taskID + 1000);
                                        notificationScheduler.cancelNotification(taskID + 1999);
                                    })
                                    .setNegativeButton(android.R.string.no, null)
                                    .setIcon(android.R.drawable.ic_delete)
                                    .show();
                        } else {
                            Toast.makeText(getContext(), "Invalid task detail position", Toast.LENGTH_SHORT).show();
                        }
                        return true; // Consume the long click event
                    }
                });
            }

        }

    }

}
