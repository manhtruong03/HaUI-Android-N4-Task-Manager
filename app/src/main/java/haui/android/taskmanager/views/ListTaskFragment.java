package haui.android.taskmanager.views;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import haui.android.taskmanager.R;
import haui.android.taskmanager.controller.DBHelper;
import haui.android.taskmanager.models.Tag;
import haui.android.taskmanager.models.Task;
import haui.android.taskmanager.notification.NotificationScheduler;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListTaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListTaskFragment extends Fragment {

    AppCompatButton btnAll, btnWorking, btnLate, btnComplete;
    AppCompatImageView btnSearch;
    LinearLayout linearLayoutList;
    ListView listViewTask;
    DBHelper dbHelper;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ListTaskFragment() {
        // Required empty public constructor
    }

    private void initView(View view){
        btnAll = view.findViewById(R.id.list_btn_all);
        btnWorking = view.findViewById(R.id.list_btn_working);
        btnLate = view.findViewById(R.id.list_btn_late);
        btnComplete = view.findViewById(R.id.list_btn_complete);
        btnSearch = view.findViewById(R.id.list_btn_search);
        linearLayoutList = view.findViewById(R.id.linearLayoutList);
        listViewTask = view.findViewById(R.id.list_view_task);
    }

    // TODO: Rename and change types and number of parameters
    public static ListTaskFragment newInstance(String param1, String param2) {
        ListTaskFragment fragment = new ListTaskFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    // Phương thức để đặt trạng thái chọn cho nút và thay đổi màu
    private void setButtonSelected(AppCompatButton button) {
        button.setTextColor(Color.WHITE);
        button.setBackgroundColor(Color.parseColor("#5F33E1"));
        button.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.border_search_button));
    }

    // Phương thức để đặt trạng thái không chọn cho nút và thay đổi màu
    private void setButtonUnselected(AppCompatButton button) {
        button.setTextColor(Color.parseColor("#5F33E1"));
        button.setBackgroundColor(Color.parseColor("#EDE8FF"));
        button.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.border_phanloai_listtask));
    }

    public void onResume() {
        super.onResume();
    }

    List<Task> taskArrayList = new ArrayList<Task>();
    TaskViewAdapter arrayAdapter = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_task, container, false);
        initView(view);
        dbHelper = new DBHelper(getContext());

        try {
            taskArrayList.clear();
            taskArrayList = dbHelper.getAllTasksByStatus(1);

            arrayAdapter = new TaskViewAdapter(getActivity(), taskArrayList);

            listViewTask.setAdapter(arrayAdapter);

            btnAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setButtonSelected(btnAll);
                    setButtonUnselected(btnWorking);
                    setButtonUnselected(btnComplete);
                    setButtonUnselected(btnLate);

                    taskArrayList.clear();
                    taskArrayList = dbHelper.getAllTasksByStatus(1);
                    arrayAdapter = new TaskViewAdapter(getActivity(), taskArrayList);

                    listViewTask.setAdapter(arrayAdapter);
                }
            });

            btnWorking.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setButtonSelected(btnWorking);
                    setButtonUnselected(btnAll);
                    setButtonUnselected(btnComplete);
                    setButtonUnselected(btnLate);

                    taskArrayList.clear();
                    taskArrayList = dbHelper.getAllTasksByStatus(2);
                    arrayAdapter = new TaskViewAdapter(getActivity(), taskArrayList);

                    listViewTask.setAdapter(arrayAdapter);
                }
            });

            btnComplete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setButtonSelected(btnComplete);
                    setButtonUnselected(btnWorking);
                    setButtonUnselected(btnAll);
                    setButtonUnselected(btnLate);

                    taskArrayList.clear();
                    taskArrayList = dbHelper.getAllTasksByStatus(3);
                    arrayAdapter = new TaskViewAdapter(getActivity(), taskArrayList);

                    listViewTask.setAdapter(arrayAdapter);
                }
            });

            btnLate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setButtonSelected(btnLate);
                    setButtonUnselected(btnWorking);
                    setButtonUnselected(btnComplete);
                    setButtonUnselected(btnAll);

                    taskArrayList.clear();
                    taskArrayList = dbHelper.getAllTasksByStatus(4);

                    arrayAdapter = new TaskViewAdapter(getActivity(), taskArrayList);

                    listViewTask.setAdapter(arrayAdapter);
                }
            });

            btnSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment searchTaskFragment = new SearchTaskFragment();
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, searchTaskFragment)
                            .addToBackStack(null)
                            .commit();

                }
            });

            listViewTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String data = String.valueOf(taskArrayList.get(position).getTaskID());
                    Log.d("ID task", "onItemClick: " + data);
                    Fragment editTaskFragment = EditTaskFragment.newInstance(data);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, editTaskFragment)
                            .addToBackStack(null)
                            .commit();

                }
            });

            listViewTask.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    showDeleteConfirmationDialog(position);
                    return true;
                }
            });
        }catch (Exception ex){
            Log.d("TAG", "onCreateView: " + ex);
        }
        return view;

    }


    private void showDeleteConfirmationDialog(int position) {
        new AlertDialog.Builder(getActivity())
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn xóa công việc này không?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    private DialogInterface dialog;
                    private int which;

                    public void onClick(DialogInterface dialog, int which) {
                        this.dialog = dialog;
                        this.which = which;
                        // Remove the item from the list
                        int taskID = taskArrayList.get(position).getTaskID();
                        dbHelper.deleteTask(taskID);
                        taskArrayList.remove(position);
                        arrayAdapter.notifyDataSetChanged();
                        Toast.makeText(getActivity(), "Xóa thành công.", Toast.LENGTH_SHORT).show();
                        // Xóa thông báo cũ đã hẹn giờ
                        NotificationScheduler notificationScheduler = new NotificationScheduler(requireContext());
                        notificationScheduler.cancelNotification(taskID + 1000);
                        notificationScheduler.cancelNotification(taskID + 1999);
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_delete)
                .show();
    }
}