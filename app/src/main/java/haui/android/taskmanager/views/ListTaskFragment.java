package haui.android.taskmanager.views;

import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

import haui.android.taskmanager.MainActivity;
import haui.android.taskmanager.R;
import haui.android.taskmanager.controller.DBHelper;
import haui.android.taskmanager.databinding.ActivityMainBinding;
import haui.android.taskmanager.models.Task;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListTaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListTaskFragment extends Fragment {

    AppCompatButton btnAll, btnWorking, btnLate, btnComplete;
    AppCompatImageView btnSearch;
    LinearLayout linearLayoutList;
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
        button.setBackgroundColor(Color.parseColor("#FEB36D"));
        button.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.border_search_button));
    }

    // Phương thức để đặt trạng thái không chọn cho nút và thay đổi màu
    private void setButtonUnselected(AppCompatButton button) {
        button.setTextColor(Color.parseColor("#8B8888"));
        button.setBackgroundColor(Color.parseColor("#EAE9E9"));
        button.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.border_phanloai_listtask));
    }

    public void onResume() {
        super.onResume();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_task, container, false);
        initView(view);
        dbHelper = new DBHelper(getContext());

        List<Task> taskList = dbHelper.getAllTasks();

        // Gán sự kiện click cho các nút
        // Nút all task
        btnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonSelected(btnAll);
                setButtonUnselected(btnWorking);
                setButtonUnselected(btnComplete);
                setButtonUnselected(btnLate);

                Boolean check = false;

                for (int i = 0; i < taskList.size(); i++) {
                    if (taskList.get(i).getTaskID() == 0) {
                        check = true;
                        break;
                    }
                }

                dbHelper.close();

                if (check) {

                    FragmentTransaction transactionMain = getActivity().getSupportFragmentManager().beginTransaction();

                    Bundle bundle = new Bundle();
                    bundle.putInt("type_to_show", 0);


                    RecycleViewFragment recycleViewFragment = new RecycleViewFragment();
                    recycleViewFragment.setArguments(bundle);

                    transactionMain.replace(R.id.linearLayoutList, recycleViewFragment);
                    // Commit transaction
                    transactionMain.commit();
                }
            }
        });

        // Nút task đang làm
        btnWorking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonSelected(btnWorking);
                setButtonUnselected(btnAll);
                setButtonUnselected(btnComplete);
                setButtonUnselected(btnLate);

                Boolean check = false;

                for (int i = 0; i < taskList.size(); i++) {
                    if (taskList.get(i).getTaskID() == 1) {
                        check = true;
                        break;
                    }
                }

                dbHelper.close();

                if (check) {

                    FragmentTransaction transactionMain = getActivity().getSupportFragmentManager().beginTransaction();

                    Bundle bundle = new Bundle();
                    bundle.putInt("type_to_show", 1);


                    RecycleViewFragment recycleViewFragment = new RecycleViewFragment();
                    recycleViewFragment.setArguments(bundle);

                    transactionMain.replace(R.id.linearLayoutList, recycleViewFragment);
                    // Commit transaction
                    transactionMain.commit();
                }
            }
        });

        // Nút task hoàn thành
        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonSelected(btnComplete);
                setButtonUnselected(btnWorking);
                setButtonUnselected(btnAll);
                setButtonUnselected(btnLate);

                Boolean check = false;

                for (int i = 0; i < taskList.size(); i++) {
                    if (taskList.get(i).getTaskID() == 2) {
                        check = true;
                        break;
                    }
                }

                dbHelper.close();

                if (check) {

                    FragmentTransaction transactionMain = getActivity().getSupportFragmentManager().beginTransaction();

                    Bundle bundle = new Bundle();
                    bundle.putInt("type_to_show", 2);


                    RecycleViewFragment recycleViewFragment = new RecycleViewFragment();
                    recycleViewFragment.setArguments(bundle);

                    transactionMain.replace(R.id.linearLayoutList, recycleViewFragment);
                    // Commit transaction
                    transactionMain.commit();
                }
            }
        });


        // Nút task muộn
        btnLate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonSelected(btnLate);
                setButtonUnselected(btnWorking);
                setButtonUnselected(btnComplete);
                setButtonUnselected(btnAll);

                Boolean check = false;

                for (int i = 0; i < taskList.size(); i++) {
                    if (taskList.get(i).getTaskID() == 3) {
                        check = true;
                        break;
                    }
                }

                dbHelper.close();

                if (check) {

                    FragmentTransaction transactionMain = getActivity().getSupportFragmentManager().beginTransaction();

                    Bundle bundle = new Bundle();
                    bundle.putInt("type_to_show", 3);


                    RecycleViewFragment recycleViewFragment = new RecycleViewFragment();
                    recycleViewFragment.setArguments(bundle);

                    transactionMain.replace(R.id.linearLayoutList, recycleViewFragment);
                    // Commit transaction
                    transactionMain.commit();
                }
            }
        });

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {


            }
        });

        // Inflate the layout for this fragment
        return view;
    }
}