package haui.android.taskmanager.views;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import haui.android.taskmanager.R;
import haui.android.taskmanager.controller.DBHelper;
import haui.android.taskmanager.models.Notification;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchTaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchNotificationFragment extends Fragment {

    AppCompatButton btnSearch;
    EditText textSearch;
    ListView listView;
    List<Notification> notificationArrayList = new ArrayList<Notification>();
    NotificationViewAdapter arrayAdapter = null;
    DBHelper dbHelper;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchNotificationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchWorkFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchNotificationFragment newInstance(String param1, String param2) {
        SearchNotificationFragment fragment = new SearchNotificationFragment();
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

    private void initView(View view){
        btnSearch = view.findViewById(R.id.search_notification_btn);
        textSearch = view.findViewById(R.id.search_notification_text);
        listView = view.findViewById(R.id.search_notification_listview);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_notification, container, false);

        initView(view);
        dbHelper = new DBHelper(getContext());

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(textSearch.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(), "Vui lòng nhập tên thông báo cần tìm", Toast.LENGTH_SHORT).show();
                }else {
                    notificationArrayList.clear();
                    notificationArrayList = dbHelper.searchNotifications(textSearch.getText().toString());
                    if(notificationArrayList == null){
                        Toast.makeText(getActivity(), "Không có thông báo cần tìm", Toast.LENGTH_SHORT).show();
                    }else {
                        arrayAdapter = new NotificationViewAdapter(getActivity(), notificationArrayList);
                        listView.setAdapter(arrayAdapter);
                    }
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String data = String.valueOf(notificationArrayList.get(position).getTaskID());

                Fragment editTaskFragment = EditTaskFragment.newInstance(data);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, editTaskFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showDeleteConfirmationDialog(position);
                return true;
            }
        });


        return view;
    }

    private void showDeleteConfirmationDialog(int position) {
        new AlertDialog.Builder(getActivity())
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn xóa thông báo này không?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    private DialogInterface dialog;
                    private int which;

                    public void onClick(DialogInterface dialog, int which) {
                        this.dialog = dialog;
                        this.which = which;
                        // Remove the item from the list
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