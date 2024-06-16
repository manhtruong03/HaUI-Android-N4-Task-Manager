package haui.android.taskmanager.views;



import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import haui.android.taskmanager.R;
import haui.android.taskmanager.controller.DBHelper;
import haui.android.taskmanager.models.Task;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarFragment extends Fragment {
    private CalendarView calendarView;
    private ListView calender_ListView;
    private DBHelper dbHelper;
    List<Task> tasks = null;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CalendarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CalendarFragment.
     */
    // TODO: Rename and change types and number of parameters

    public static CalendarFragment newInstance(String param1, String param2) {
        CalendarFragment fragment = new CalendarFragment();
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
    TaskViewAdapter arrayAdapter = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        // widgets
        calendarView = view.findViewById(R.id.calendar_view_calendar);
        calender_ListView = view.findViewById(R.id.calendar_listview);
        dbHelper = new DBHelper(getActivity());
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String date = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);
                //lấy tasks theo ngày
                tasks = dbHelper.getTasksByDay(date);
                //Sửa format ngày
                // Hiển thị tasks lên ListView
                displayTasks(tasks);
            }
        });
        calender_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String data = String.valueOf(tasks.get(position).getTaskID());

                Fragment editTaskFragment = EditTaskFragment.newInstance(data);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, editTaskFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        calender_ListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showDeleteConfirmationDialog(position);
                return true;
            }
        });

        return view;
        // return inflater.inflate(R.layout.fragment_calendar, container, false);
    }
    private void showDeleteConfirmationDialog(int position) {
        new AlertDialog.Builder(getActivity())
                .setTitle("Delete Confirmation")
                .setMessage("Bạn có chắc chắn xóa công việc này không?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    private DialogInterface dialog;
                    private int which;

                    public void onClick(DialogInterface dialog, int which) {
                        this.dialog = dialog;
                        this.which = which;
                        // Remove the item from the list
                        dbHelper.deleteTask(tasks.get(position).getTaskID());
                        tasks.remove(position);
                        arrayAdapter.notifyDataSetChanged();
                        Toast.makeText(getActivity(), "Xóa thành công.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_delete)
                .show();
    }
    private void displayTasks(List<Task> tasks) {
        // If no tasks, show a toast message
        if (tasks.isEmpty()) {
            Toast.makeText(getActivity(), "Không có công việc trong ngày này!", Toast.LENGTH_SHORT).show();
        }
        arrayAdapter = new TaskViewAdapter(getActivity(), tasks);
        calender_ListView.setAdapter(arrayAdapter);

    }

}