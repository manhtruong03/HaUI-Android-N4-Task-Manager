package haui.android.taskmanager.views;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import android.app.AlertDialog;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import haui.android.taskmanager.R;
import haui.android.taskmanager.controller.DBHelper;
import haui.android.taskmanager.models.Tag;

public class CreateTaskFragment extends Fragment {

    String[] nhans = {"Công việc", "Học tập", "Gia đình", "Sức Khỏe"};
    AutoCompleteTextView spinner;
    TextInputEditText taskName, datestart, timestart, dateend, timeend,taskDescription;

    //    ArrayList<Tag> arrTag;
//    TagAdapter adapterTag;
    ArrayAdapter<String>  adapterNhans;
    Button btnAddTask;
    ImageView addTag;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_task, container, false);
        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.add_task_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        spinner = view.findViewById(R.id.spinner);
        datestart = view.findViewById(R.id.datestart);
        timestart = view.findViewById(R.id.timestart);
        dateend = view.findViewById(R.id.dateend);
        timeend = view.findViewById(R.id.timeend);
        taskName = view.findViewById(R.id.taskName);
        taskDescription = view.findViewById(R.id.task_description);
        btnAddTask = view.findViewById(R.id.btnAddTask);
        addTag = view.findViewById(R.id.addTag);

        // add tag
        addTag.setOnClickListener(v -> showAddTagDialog());

        // Thiết lập adapter cho spinner
//        arrTag = new ArrayList<>();
//        arrTag.add(new Tag(1, "Công Việc", "#FFCC33"));
//        arrTag.add(new Tag(2, "Học Tập", "#FFCC33"));

//        adapterTag = new TagAdapter(this,R.layout.create_customlistview, arrTag );
//        spinner.setAdapter(adapterTag);

        adapterNhans = new ArrayAdapter<String>(getActivity(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, nhans);

        spinner.setAdapter(adapterNhans);


        spinner.setOnItemClickListener((parent, view1, position, id) -> {
            String item = parent.getItemAtPosition(position).toString();
        });

        // Thiết lập sự kiện chọn ngày và giờ
        datestart.setOnClickListener(v -> chooseDate(datestart));
        timestart.setOnClickListener(v -> chooseTime(timestart));
        dateend.setOnClickListener(v -> chooseDate(dateend));
        timeend.setOnClickListener(v -> chooseTime(timeend));

        // Gắn sự kiện cho nút "Thêm"
        btnAddTask.setOnClickListener(v -> addTask());
        return view;
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

    private void addTask() {
        // Lấy dữ liệu từ các trường nhập liệu
        String name = taskName.getText().toString().trim();
        String description = taskDescription.getText().toString().trim();
        String startDate = datestart.getText().toString().trim();
        String startTime = timestart.getText().toString().trim();
        String endDate = dateend.getText().toString().trim();
        String endTime = timeend.getText().toString().trim();
        String selectedTag = spinner.getText().toString().trim(); // Lấy giá trị từ spinner

        // Kiểm tra nếu các trường bắt buộc không được điền đầy đủ
        if (name.isEmpty() || description.isEmpty() || startDate.isEmpty() || startTime.isEmpty() || endDate.isEmpty() || endTime.isEmpty() || selectedTag.isEmpty()) {
            showAlert("Vui lòng điền đầy đủ thông tin!");
            return;
        }

        // Kiểm tra nếu ngày kết thúc sau ngày bắt đầu
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        try {
            // Chuyển đổi chuỗi ngày giờ thành đối tượng Date
            Date startDateTime = dateFormat.parse(startDate + " " + startTime);
            Date endDateTime = dateFormat.parse(endDate + " " + endTime);

            if (endDateTime.before(startDateTime)) {
                showAlert("Ngày kết thúc phải sau ngày bắt đầu!");
                return;
            } else if (endDateTime.equals(startDateTime)) {
                showAlert("Thời gian kết thúc phải sau thời gian bắt đầu!");
                return;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            showAlert("Định dạng ngày giờ không hợp lệ!");
            return;
        }

        // Tạo task mới với dữ liệu đã nhập và giá trị từ spinner
        int priority = 1;
        int statusId = 1;
        int tagId = getTagIdByName(selectedTag); // Lấy tagId từ giá trị được chọn trong spinner

        DBHelper dbHelper = new DBHelper(getContext());
        long taskId = dbHelper.insertTask(name, description, startDate, startTime, endDate, endTime, priority, statusId, tagId);
        if (taskId != -1) {
            // Nếu thêm thành công, hiển thị thông báo thành công
            Toast.makeText(getContext(), "Thêm task thành công.", Toast.LENGTH_SHORT).show();
        } else {
            // Nếu thêm không thành công, hiển thị thông báo lỗi
            showAlert("Có lỗi xảy ra khi thêm task.");
        }
    }

    private int getTagIdByName(String tagName) {

        String[] nhans = {"Công việc", "Học tập", "Gia đình", "Sức Khỏe"};

        for (int i = 0; i < nhans.length; i++) {
            if (nhans[i].equals(tagName)) {

                return i + 1;
            }
        }

        // Trường hợp không tìm thấy, trả về giá trị mặc định.
        return 0;
    }
    private void showAlert(String message) {
        new AlertDialog.Builder(getContext())
                .setTitle("Thông báo")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }
    private void showAddTagDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.createtask_dialog_add_tag, null);
        builder.setView(dialogView);

        EditText editTextNewTag = dialogView.findViewById(R.id.editTextNewTag);
        Button buttonAddTag = dialogView.findViewById(R.id.buttonAddTag);

        AlertDialog dialog = builder.create();

        buttonAddTag.setOnClickListener(view -> {
            String newTag = editTextNewTag.getText().toString().trim();
            // Thêm logic để lưu tag mới vào cơ sở dữ liệu ở đây
            // Sau khi lưu thành công, có thể cập nhật spinner hoặc thông báo thành công

            // Sau khi thực hiện xong các logic thêm tag, đóng dialog
            dialog.dismiss();
        });

        dialog.show();
    }
}
