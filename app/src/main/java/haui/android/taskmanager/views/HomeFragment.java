package haui.android.taskmanager.views;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.activity.EdgeToEdge;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

import java.util.List;

import haui.android.taskmanager.MainActivity;
import haui.android.taskmanager.R;
import haui.android.taskmanager.controller.DBHelper;
import haui.android.taskmanager.models.HomeInprogressAdapter;
import haui.android.taskmanager.models.HomeTaskGroupAdapter;
import haui.android.taskmanager.models.Task;

public class HomeFragment extends Fragment {
    DBHelper dbHelper;
    RecyclerView rcv_ingrogres, rcv_task_group;
    ProgressBar home_progress_circular1;
    HomeInprogressAdapter homeAdapter;

    HomeTaskGroupAdapter homeTaskGroupAdapter;
    TextView home_txtInProgress, home_so_luong_nhom;

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
        rcv_ingrogres = view.findViewById(R.id.rcv_inprogress);
        rcv_task_group = view.findViewById(R.id.rcv_task_group);
        home_txtInProgress = view.findViewById(R.id.home_txtInProgress);
        home_progress_circular1 = view.findViewById(R.id.home_progress_circular1);
        home_so_luong_nhom = view.findViewById(R.id.home_task_group);
        home_btnViewTask = view.findViewById(R.id.home_btnViewTask);
        dbHelper = new DBHelper(getContext());

//        dbHelper.insertTask("Thiết kế giao diện","Màn hình: thống kê, thông báo","11/5/2024 00:00:00","31/12/1899 09:00:00","14/5/2024 00:00:00","31/12/1899 16:00:00",0,0,0);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        rcv_ingrogres.setLayoutManager(linearLayoutManager);
        List<Task> allListTask = dbHelper.getAllTasks();
        home_txtInProgress.setText(allListTask.size() + "");
        homeAdapter = new HomeInprogressAdapter(allListTask);
        rcv_ingrogres.setAdapter(homeAdapter);

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rcv_task_group.setLayoutManager(linearLayoutManager1);
        homeTaskGroupAdapter = new HomeTaskGroupAdapter(allListTask);
        rcv_task_group.setAdapter(homeTaskGroupAdapter);
        home_so_luong_nhom.setText(allListTask.size()+"");

        home_progress_circular1.setProgress(75);
        home_progress_circular1.setMax(100);

        home_btnViewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ListTaskFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment).commit();
            }
        });



    }
//    public List<Task> getListHomeInProgress(List<Task> allListHome){
//        List<Task> list = new ArrayList<>();
//        for(int i = 0; i < allListHome.size(); i++) {
//            if(allListHome.get(i).getStatusName() == 0) {
//                list.add(allListHome.get(i));
//            }
//        }
//        return list;
//    }

//    private void openFragment(Fragment fragment, int enterAnimation, int exitAnimation) {
//        if (enterAnimation == -1 || exitAnimation == -1) {
//            enterAnimation = R.anim.exit_to_left;
//            exitAnimation = R.anim.enter_from_right;
//        }
//        getParentFragment().
//    }

}
