package haui.android.taskmanager.views;

import static android.content.ContentValues.TAG;

import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;

import haui.android.taskmanager.R;
import haui.android.taskmanager.controller.DBHelper;
import haui.android.taskmanager.models.Tag;
import haui.android.taskmanager.models.Task;
import haui.android.taskmanager.notification.NotificationScheduler;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditTaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditTaskFragment extends Fragment {

    private static final String ARG_DATA = "data";

    List<String> nhans;
    Spinner spinner;
    TextInputEditText datestart, timestart, dateend, timeend, decriptionTask, titleTask;
    ArrayAdapter<String> adapterNhans;
    Button btnUpdate, btnComplete;
    CheckBox checkBoxWorking;
    Task currentTask;
    DBHelper dbHelper;
    NotificationScheduler notificationScheduler;

    // TODO: Rename and change types and number of parameters
    public static EditTaskFragment newInstance(String param1) {
        EditTaskFragment fragment = new EditTaskFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DATA, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void initView(View view){
        dbHelper = new DBHelper(getContext());
        notificationScheduler = new NotificationScheduler(requireContext());

        spinner = view.findViewById(R.id.edit_spinner);
        datestart = view.findViewById(R.id.edit_date_start);
        timestart = view.findViewById(R.id.edit_time_start);
        dateend = view.findViewById(R.id.edit_date_end);
        timeend = view.findViewById(R.id.edit_btn_time_end);
        titleTask = view.findViewById(R.id.edit_title_task);
        decriptionTask = view.findViewById(R.id.edit_decription_task);
        btnUpdate = view.findViewById(R.id.edit_btn_update);
        btnComplete = view.findViewById(R.id.edit_btn_complete);
        checkBoxWorking = view.findViewById(R.id.edit_check_working);
        nhans = new ArrayList<String>();

        List<Tag> listTag = dbHelper.getAllTags();
        for(int i=0; i<listTag.size(); i++){
            nhans.add(listTag.get(i).getTagName());
        }

        adapterNhans = new ArrayAdapter<>(getActivity(), R.layout.item_list_nhan, nhans);
        spinner.setAdapter(adapterNhans);
    }

    private boolean checkValue(){
        if(datestart.getText().toString().equals("") || timestart.getText().toString().equals("") || dateend.getText().toString().equals("")
        || timeend.getText().toString().equals("") || decriptionTask.getText().toString().equals(""))
            return true;
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_task, container, false);
        initView(view);

        try {
            if (getArguments() != null) {
                Integer position = Integer.valueOf(getArguments().getString(ARG_DATA));

                currentTask = dbHelper.getTaskById(position);


                    Tag tag = dbHelper.getTagById(currentTask.getTagID());

                    int indexNhans = findPosition(nhans, tag.getTagName());


                if(currentTask.getStatusID() == 3 || currentTask.getStatusID() == 4){
                    btnComplete.setVisibility(View.GONE);
                    checkBoxWorking.setVisibility(View.GONE);
                }

                if(currentTask.getStatusID() == 2 ){
                    checkBoxWorking.setVisibility(View.GONE);
                }

                spinner.setSelection(indexNhans);
                titleTask.setText(currentTask.getTaskName());
                decriptionTask.setText(currentTask.getDescription());
                datestart.setText(currentTask.getStartDate());
                timestart.setText(currentTask.getStartTime());
                dateend.setText(currentTask.getEndDate());
                timeend.setText(currentTask.getEndTime());
            }

            datestart.setOnClickListener(v -> chooseDate(datestart));
            timestart.setOnClickListener(v -> chooseTime(timestart));
            dateend.setOnClickListener(v -> chooseDate(dateend));
            timeend.setOnClickListener(v -> chooseTime(timeend));

            // Ngăn không cho bàn phím xuất hiện
            disableKeyboardForEditText(datestart);
            disableKeyboardForEditText(timestart);
            disableKeyboardForEditText(dateend);
            disableKeyboardForEditText(timeend);

            btnUpdate.setOnClickListener(v -> updateTask());
            btnComplete.setOnClickListener(v -> completeTask(currentTask));
            checkBoxWorking.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        dbHelper.updateStatusTask(currentTask.getTaskID(), 2);
                        Toast.makeText(getActivity(), "Cập nhật trạng thái thành công.", Toast.LENGTH_SHORT).show();
                        checkBoxWorking.setEnabled(false);
                    }

                }
            });

        }catch (Exception ex){
            Log.e("Error Edit Task:", "onCreateView: ", ex );
        }

        return view;
    }


    private void disableKeyboardForEditText(TextInputEditText editText) {
        editText.setShowSoftInputOnFocus(false);
    }

    public void completeTask(Task task){
        if(task.getStatusID() == 3) {
            Toast.makeText(getActivity(), "Công việc đã hoàn thành.", Toast.LENGTH_SHORT).show();
        }else{

            String stringDate = task.getEndDate() + " " + task.getEndTime();

            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            try {
                Date dateEnd = dateTimeFormat.parse(stringDate);
                Date dateNow = new Date();

                if(dateEnd.before(dateNow)){
                    dbHelper.updateStatusTask(task.getTaskID(), 4);
                }else{
                    dbHelper.updateStatusTask(task.getTaskID(), 3);
                }

                // Xóa thông báo cũ vì đã hoàn thành
                removeOldNotifications(currentTask.getTaskID());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            btnComplete.setVisibility(View.GONE);
            Toast.makeText(getActivity(), "Cập nhật trạng thái thành công.", Toast.LENGTH_SHORT).show();
        }
    }


    private void updateTask(){
        if(checkValue())
            Toast.makeText(getActivity(), "Vui lòng nhập đủ nôi dung !!!", Toast.LENGTH_SHORT).show();

        else {
            // Xóa thông báo cũ trước khi cập nhật tác vụ
            removeOldNotifications(currentTask.getTaskID());

            dbHelper.updateTask(currentTask.getTaskID(), decriptionTask.getText().toString(),
                    datestart.getText().toString(), timestart.getText().toString(),
                    dateend.getText().toString(), timeend.getText().toString(), currentTask.getTagID());

            // Lên lịch thông báo mới với các chi tiết cập nhật
            scheduleTaskNotifications();

            Toast.makeText(getActivity(), "Sửa thành công.", Toast.LENGTH_SHORT).show();
        }
    }

    public void removeOldNotifications(int taskID) {
        int startNotificationId = taskID + 1000;
        int endNotificationId = startNotificationId + 999;

        notificationScheduler.cancelNotification(startNotificationId);
        notificationScheduler.cancelNotification(endNotificationId);
    }

    private void scheduleTaskNotifications() {
        String startDate = datestart.getText().toString();
        String startTime = timestart.getText().toString();
        String endDate = dateend.getText().toString();
        String endTime = timeend.getText().toString();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

        try {
            Date startDateTime = dateFormat.parse(startDate + " " + startTime);
            Date endDateTime = dateFormat.parse(endDate + " " + endTime);

            String channelId = "taskNotifications";
            String channelName = "Thông báo nhiệm vụ";
            String channelDescription = "Thông báo cho các sự kiện nhiệm vụ\n";

            notificationScheduler.createNotificationChannel(channelId, channelName, channelDescription, NotificationManager.IMPORTANCE_HIGH);

            // Tính thời gian bằng mili giây
            long startTimeMillis = startDateTime.getTime() - System.currentTimeMillis();
            long endTimeMillis = endDateTime.getTime() - System.currentTimeMillis();

            int startNotificationId = currentTask.getTaskID() + 1000;
            int endNotificationId = startNotificationId + 999;

            notificationScheduler.scheduleNotificationWithTwoActions(channelId,
                    startNotificationId,
                    "Bắt đầu: " + titleTask.getText().toString(),
                    "Mô tả: " + decriptionTask.getText().toString(),
                    startTimeMillis);
            notificationScheduler.scheduleNotificationWithTwoActions(channelId,
                    endNotificationId,
                    "Hết hạn: " + titleTask.getText().toString(),
                    "Mô tả: " + decriptionTask.getText().toString(),
                    endTimeMillis);

        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(TAG, "Định dạng ngày giờ không hợp lệ!");
        }
    }

    public int findPosition(List<String> array, String value) {
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i).equals(value)) {
                return i;
            }
        }
        return 0; // Trả về -1 nếu không tìm thấy
    }

    private void chooseDate(TextInputEditText date) {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, year1, monthOfYear, dayOfMonth) -> {
            calendar.set(year1, monthOfYear, dayOfMonth);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            date.setText(simpleDateFormat.format(calendar.getTime()));
        }, year, month, day);
        datePickerDialog.show();
    }

    private void chooseTime(TextInputEditText time) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), (view, hourOfDay, minute1) -> {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            calendar.set(0, 0, 0, hourOfDay, minute1);
            time.setText(simpleDateFormat.format(calendar.getTime()));
        }, hour, minute, true);
        timePickerDialog.show();
    }
}