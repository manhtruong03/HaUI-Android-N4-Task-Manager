package haui.android.taskmanager.views;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import haui.android.taskmanager.R;
import haui.android.taskmanager.controller.DBHelper;
import haui.android.taskmanager.models.Notification;

public class ListNotificationFragment extends Fragment {

    AppCompatButton btnAll, btnToday, btnPast7days, btnPast30days;
    AppCompatImageView btnSearch;
    LinearLayout linearLayoutList;
    ListView listViewNotification;
    DBHelper dbHelper;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public ListNotificationFragment() {
        // Required empty public constructor
    }

    private void initView(View view) {
        btnAll = view.findViewById(R.id.list_btn_all);
        btnToday = view.findViewById(R.id.list_btn_today);
        btnPast7days = view.findViewById(R.id.list_btn_past7days);
        btnPast30days = view.findViewById(R.id.list_btn_past30days);
        btnSearch = view.findViewById(R.id.list_btn_search);
        linearLayoutList = view.findViewById(R.id.linearLayoutList);
        listViewNotification = view.findViewById(R.id.list_view_notification);
    }

    public static ListNotificationFragment newInstance(String param1, String param2) {
        ListNotificationFragment fragment = new ListNotificationFragment();
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

    private void setButtonSelected(AppCompatButton button) {
        button.setTextColor(Color.WHITE);
        button.setBackgroundColor(Color.parseColor("#5F33E1"));
        button.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.border_search_button));
    }

    private void setButtonUnselected(AppCompatButton button) {
        button.setTextColor(Color.parseColor("#5F33E1"));
        button.setBackgroundColor(Color.parseColor("#EDE8FF"));
        button.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.border_phanloai_listtask));
    }

    public void onResume() {
        super.onResume();
    }

    List<Notification> notificationArrayList = new ArrayList<>();
    NotificationViewAdapter arrayAdapter = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_notification, container, false);
        initView(view);
        dbHelper = new DBHelper(getContext());

        try {
            notificationArrayList.clear();
            notificationArrayList = dbHelper.getAllNotifications();

            arrayAdapter = new NotificationViewAdapter(getActivity(), notificationArrayList);

            listViewNotification.setAdapter(arrayAdapter);

            btnAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setButtonSelected(btnAll);
                    setButtonUnselected(btnToday);
                    setButtonUnselected(btnPast30days);
                    setButtonUnselected(btnPast7days);

                    notificationArrayList.clear();
                    notificationArrayList = dbHelper.getAllNotifications();
                    arrayAdapter = new NotificationViewAdapter(getActivity(), notificationArrayList);

                    listViewNotification.setAdapter(arrayAdapter);
                }
            });

            btnToday.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setButtonSelected(btnToday);
                    setButtonUnselected(btnAll);
                    setButtonUnselected(btnPast30days);
                    setButtonUnselected(btnPast7days);

                    notificationArrayList.clear();
                    notificationArrayList = dbHelper.getNotificationsByTime("today");
                    arrayAdapter = new NotificationViewAdapter(getActivity(), notificationArrayList);

                    listViewNotification.setAdapter(arrayAdapter);
                }
            });

            btnPast7days.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setButtonSelected(btnPast7days);
                    setButtonUnselected(btnToday);
                    setButtonUnselected(btnPast30days);
                    setButtonUnselected(btnAll);

                    notificationArrayList.clear();
                    notificationArrayList = dbHelper.getNotificationsByTime("past7days");

                    arrayAdapter = new NotificationViewAdapter(getActivity(), notificationArrayList);

                    listViewNotification.setAdapter(arrayAdapter);
                }
            });

            btnPast30days.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setButtonSelected(btnPast30days);
                    setButtonUnselected(btnToday);
                    setButtonUnselected(btnAll);
                    setButtonUnselected(btnPast7days);

                    notificationArrayList.clear();
                    notificationArrayList = dbHelper.getNotificationsByTime("past30days");
                    arrayAdapter = new NotificationViewAdapter(getActivity(), notificationArrayList);

                    listViewNotification.setAdapter(arrayAdapter);
                }
            });

            btnSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment searchNotificationFragment = new SearchNotificationFragment();
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, searchNotificationFragment)
                            .addToBackStack(null)
                            .commit();

                }
            });

            listViewNotification.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String data = String.valueOf(notificationArrayList.get(position).getTaskID());
                    Log.d("ID task", "onItemClick: " + data);
                    Fragment editNotificationFragment = EditTaskFragment.newInstance(data);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, editNotificationFragment)
                            .addToBackStack(null)
                            .commit();

                }
            });

            listViewNotification.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    showDeleteConfirmationDialog(position);
                    return true;
                }
            });
        } catch (Exception ex) {
            Log.d("TAG", "onCreateView: " + ex);
        }
        return view;
    }

    private void showDeleteConfirmationDialog(int position) {
        new AlertDialog.Builder(getActivity())
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn xóa thông báo này không?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper.deleteNotification(notificationArrayList.get(position).getNotiID());
                        notificationArrayList.remove(position);
                        arrayAdapter.notifyDataSetChanged();
                        Toast.makeText(getActivity(), "Xóa thành công.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_delete)
                .show();
    }
}

