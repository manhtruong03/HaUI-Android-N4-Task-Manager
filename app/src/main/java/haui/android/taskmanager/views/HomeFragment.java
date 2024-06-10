package haui.android.taskmanager.views;

import static haui.android.taskmanager.MainActivity.bottomNavigation;

import android.annotation.SuppressLint;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

import java.util.List;

import haui.android.taskmanager.MainActivity;
import haui.android.taskmanager.R;
import haui.android.taskmanager.controller.DBHelper;
import haui.android.taskmanager.models.HomeListTag;
import haui.android.taskmanager.models.Tag;
import haui.android.taskmanager.models.Task;
import haui.android.taskmanager.models.TaskDetail;

public class HomeFragment extends Fragment{
    DBHelper dbHelper;
    RecyclerView rcv_ingrogres, rcv_task_group;
    ProgressBar home_progress_circular1;
    HomeInprogressAdapter homeAdapter;

    TextView test;
    HomeTaskGroupAdapter homeTaskGroupAdapter;
    TextView home_txtInProgress, home_so_luong_nhom, home_txtProgressCircle1;
    SQLiteDatabase db;
    Button home_btnViewTask;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Enabling Edge-to-Edge
        if (getActivity() != null) {
            EdgeToEdge.enable(getActivity());
        }

        findId(view);
        dbHelper = new DBHelper(getContext());
        db = this.dbHelper.getWritableDatabase();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false); // set for inprogress
        rcv_ingrogres.setLayoutManager(linearLayoutManager);
        List<TaskDetail> allListTask = dbHelper.getAllTasksDetail();
        List<Task> TaskInprogress = dbHelper.getAllTasksByStatus(2);
        homeAdapter = new HomeInprogressAdapter(allListTask);
        rcv_ingrogres.setAdapter(homeAdapter);
        home_txtInProgress.setText(TaskInprogress.size() + "");

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false); // set for taskgroups
        rcv_task_group.setLayoutManager(linearLayoutManager1);
        List<Tag> tagList = dbHelper.getAllTags(); // Lấy số lượng nhóm nhiệm vụ
        homeTaskGroupAdapter = new HomeTaskGroupAdapter(allListTask, tagList, dbHelper, new HomeTaskGroupAdapter.ICickHomeListTag() {
            @Override
            public void onItemClick(HomeListTag homeListTag) {
                goToHomeDeDetailTGFragment(homeListTag);
            }
        });
        rcv_task_group.setAdapter(homeTaskGroupAdapter);
        home_so_luong_nhom.setText(tagList.size() +"");

        List<Task> tasksDone =dbHelper.getAllTasksByStatus(3);
        List<TaskDetail> allTasks = dbHelper.getAllTasksDetail();
        double tyLeCvHoanThanh = ((double) tasksDone.size() / allTasks.size()) * 100;
        int x = (int) tyLeCvHoanThanh;
        home_txtProgressCircle1.setText( x+ "%");
        home_progress_circular1.setProgress(x);
        home_progress_circular1.setMax(100);

        home_btnViewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ListTaskFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment).commit();
                bottomNavigation.show(4, true);
            }
        });
    }

    private void findId(View view){
        rcv_ingrogres = view.findViewById(R.id.rcv_inprogress);
        rcv_task_group = view.findViewById(R.id.rcv_task_group);
        home_txtInProgress = view.findViewById(R.id.home_txtInProgress);
        home_progress_circular1 = view.findViewById(R.id.home_progress_circular1);
        home_so_luong_nhom = view.findViewById(R.id.home_task_group);
        home_btnViewTask = view.findViewById(R.id.home_btnViewTask);
        home_txtProgressCircle1 = view.findViewById(R.id.home_txtProgressCircle1);
    }

    private void goToHomeDeDetailTGFragment(HomeListTag homeListTag){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

        HomeDetailTGFragment homeDetailTGFragment = new HomeDetailTGFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_hometaglist",homeListTag);
        homeDetailTGFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.fragment_container, homeDetailTGFragment).commit();
    }

}
