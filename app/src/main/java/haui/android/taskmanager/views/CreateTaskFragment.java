package haui.android.taskmanager.views;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import android.app.AlertDialog;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.Toast;

import haui.android.taskmanager.R;
import haui.android.taskmanager.controller.DBHelper;
import haui.android.taskmanager.models.Tag;

public class CreateTaskFragment extends Fragment {
    AutoCompleteTextView spinner;
    TextInputEditText taskName, datestart, timestart, dateend, timeend, taskDescription;
    TagAdapter adapterTag;
    Button btnAddTask;
    ImageView editTag;
    DBHelper db;

    int vitri;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_task, container, false);
        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.add_task_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        vitri = -1;

        spinner = view.findViewById(R.id.spinner);
        datestart = view.findViewById(R.id.datestart);
        timestart = view.findViewById(R.id.timestart);
        dateend = view.findViewById(R.id.dateend);
        timeend = view.findViewById(R.id.timeend);
        taskName = view.findViewById(R.id.taskName);
        taskDescription = view.findViewById(R.id.task_description);
        btnAddTask = view.findViewById(R.id.btnAddTask);
        editTag = view.findViewById(R.id.editTag);

        db = new DBHelper(requireContext());

        List<Tag> arrTag = db.getAllTags();
        // Sử dụng Set để loại bỏ các phần tử trùng lặp
        Set<Tag> uniqueTags = new HashSet<>(arrTag);

        // Tạo danh sách mới từ Set
        List<Tag> tagList = new ArrayList<>(uniqueTags);

        // Tạo Adapter với danh sách không có phần tử trùng lặp
        adapterTag = new TagAdapter(getContext(), R.layout.create_customlistview, tagList);
        spinner.setAdapter(adapterTag);
        adapterTag.notifyDataSetChanged();

        spinner.setOnItemClickListener((parent, view1, position, id) -> {
            Tag selectedTag = adapterTag.getItem(position);
            if (selectedTag != null) {
                String selectedTagName = selectedTag.getTagName();
                spinner.setText(selectedTagName, false); // false to prevent showing dropdown again
            }
            vitri = position;
        });


        // Thiết lập sự kiện chọn ngày và giờ
        datestart.setOnClickListener(v -> chooseDate(datestart));
        timestart.setOnClickListener(v -> chooseTime(timestart));
        dateend.setOnClickListener(v -> chooseDate(dateend));
        timeend.setOnClickListener(v -> chooseTime(timeend));

        // Ngăn không cho bàn phím xuất hiện
        disableKeyboardForEditText(datestart);
        disableKeyboardForEditText(timestart);
        disableKeyboardForEditText(dateend);
        disableKeyboardForEditText(timeend);
        spinner.setShowSoftInputOnFocus(false);

        // Gắn sự kiện cho nút "Thêm"
        btnAddTask.setOnClickListener(v -> addTask());

        // edit Tag
        editTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowMenu();
            }
        });
        return view;

    }

    private void disableKeyboardForEditText(TextInputEditText editText) {
        editText.setShowSoftInputOnFocus(false);
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
        Integer selectedTagId = adapterTag.getItem(vitri).getTagID();// Lấy tagId từ giá trị được chọn trong spinner

        //Integer selectedTagId = 0;

        // Kiểm tra nếu các trường bắt buộc không được điền đầy đủ
        if (name.isEmpty() || description.isEmpty() || startDate.isEmpty() || startTime.isEmpty() || endDate.isEmpty() || endTime.isEmpty() || selectedTagId.toString().isEmpty()) {
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
        int tagId = selectedTagId;

        long taskId = db.insertTask(name, description, startDate, startTime, endDate, endTime, priority, statusId, tagId);

        if (taskId != -1) {
            // Nếu thêm thành công, hiển thị thông báo thành công
            Toast.makeText(getContext(), "Thêm task thành công.", Toast.LENGTH_SHORT).show();
        } else {
            // Nếu thêm không thành công, hiển thị thông báo lỗi
            showAlert("Có lỗi xảy ra khi thêm task.");
        }
    }

    private void clearInput() {
        taskName.setText("");
        taskDescription.setText("");
        datestart.setText("");
        timestart.setText("");
        dateend.setText("");
        timeend.setText("");
        spinner.setText("");
        vitri = -1;
    }

    private void showAlert(String message) {
        new AlertDialog.Builder(getContext())
                .setTitle("Thông báo")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    // Method to refresh spinner data
    private void refreshSpinner() {
        List<Tag> arrTag = db.getAllTags();
        Set<Tag> uniqueTags = new HashSet<>(arrTag);
        List<Tag> tagList = new ArrayList<>(uniqueTags);
        adapterTag.clear();
        adapterTag.addAll(tagList);
        adapterTag.notifyDataSetChanged();
    }

    private void ShowMenu() {

        Tag selectedTag = adapterTag.getItem(vitri);

        PopupMenu popupMenu = new PopupMenu(getContext(), editTag);
        popupMenu.getMenuInflater().inflate(R.menu.createtask_menu_tag, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_add_tag) {
                    addTag();
                    return true;
                } else if (item.getItemId() == R.id.action_edit_tag) {
                    if (selectedTag != null) {
                        editTag(selectedTag);
                    } else {
                        showAlert("Vui lòng chọn một nhãn để sửa.");
                    }
                    return true;
                } else if (item.getItemId() == R.id.action_delete_tag) {
                    if (selectedTag != null) {
                        deleteTag(selectedTag);
                    } else {
                        showAlert("Vui lòng chọn một nhãn để xóa.");
                    }
                    return true;
                } else {
                    return false;
                }
            }
        });

        popupMenu.show();
    }

    // Add Tag
    private void addTag() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.createtask_dialog_add_tag, null);
        builder.setView(dialogView);

        EditText editTextNewTag = dialogView.findViewById(R.id.editTextNewTag);
        Button buttonAddTag = dialogView.findViewById(R.id.btnAddTag);
        // Initialize RadioButtons
        RadioButton radioButtonColor1 = dialogView.findViewById(R.id.radioButtonColor1);
        RadioButton radioButtonColor2 = dialogView.findViewById(R.id.radioButtonColor2);
        RadioButton radioButtonColor3 = dialogView.findViewById(R.id.radioButtonColor3);
        RadioButton radioButtonColor4 = dialogView.findViewById(R.id.radioButtonColor4);
        RadioButton radioButtonColor5 = dialogView.findViewById(R.id.radioButtonColor5);
        RadioButton radioButtonColor6 = dialogView.findViewById(R.id.radioButtonColor6);
        RadioButton radioButtonColor7 = dialogView.findViewById(R.id.radioButtonColor7);

        AlertDialog dialog = builder.create();

        buttonAddTag.setOnClickListener(view -> {
            String newTag = editTextNewTag.getText().toString().trim();
            String selectedColor = null;

            // Check which RadioButton is selected
            if (radioButtonColor1.isChecked()) {
                selectedColor = (String) radioButtonColor1.getTag();
            } else if (radioButtonColor2.isChecked()) {
                selectedColor = (String) radioButtonColor2.getTag();
            } else if (radioButtonColor3.isChecked()) {
                selectedColor = (String) radioButtonColor3.getTag();
            } else if (radioButtonColor4.isChecked()) {
                selectedColor = (String) radioButtonColor4.getTag();
            } else if (radioButtonColor5.isChecked()) {
                selectedColor = (String) radioButtonColor5.getTag();
            } else if (radioButtonColor6.isChecked()) {
                selectedColor = (String) radioButtonColor6.getTag();
            } else if (radioButtonColor7.isChecked()) {
                selectedColor = (String) radioButtonColor7.getTag();
            }

            if (newTag.isEmpty() || selectedColor.isEmpty()) {
                showAlert("Vui lòng điền đầy đủ thông tin!");
                return;
            } else {
                Tag tag = new Tag();
                tag.setTagName(newTag);
                tag.setTagColor(selectedColor); // Assuming Tag class has a setColor method
                // Save tag to the database
                long result = db.addTag(tag);
                if (result != -1) {
                    // Successfully added, refresh the spinner
                    refreshSpinner();
                    Toast.makeText(getContext(), "Thêm nhãn thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Có lỗi xảy ra khi thêm nhãn", Toast.LENGTH_SHORT).show();
                }
            }
            // Close the dialog
            dialog.dismiss();
        });
        dialog.show();
    }

    // Phương thức xử lý sự kiện sửa Tag
    private void editTag(Tag tag) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.create_dialog_update_tag, null);
        builder.setView(dialogView);

        EditText editTextTag = dialogView.findViewById(R.id.editTextTag);
        Button buttonEditTag = dialogView.findViewById(R.id.btnEditTag);

        // Initialize RadioButtons
        RadioButton radioButtonColor1 = dialogView.findViewById(R.id.radioButtonColor1);
        RadioButton radioButtonColor2 = dialogView.findViewById(R.id.radioButtonColor2);
        RadioButton radioButtonColor3 = dialogView.findViewById(R.id.radioButtonColor3);
        RadioButton radioButtonColor4 = dialogView.findViewById(R.id.radioButtonColor4);
        RadioButton radioButtonColor5 = dialogView.findViewById(R.id.radioButtonColor5);
        RadioButton radioButtonColor6 = dialogView.findViewById(R.id.radioButtonColor6);
        RadioButton radioButtonColor7 = dialogView.findViewById(R.id.radioButtonColor7);

        editTextTag.setText(tag.getTagName()); // Set current tag name

        // Set current color selection
        String currentColor = tag.getTagColor();
        if (currentColor.equals(radioButtonColor1.getTag())) {
            radioButtonColor1.setChecked(true);
        } else if (currentColor.equals(radioButtonColor2.getTag())) {
            radioButtonColor2.setChecked(true);
        } else if (currentColor.equals(radioButtonColor3.getTag())) {
            radioButtonColor3.setChecked(true);
        } else if (currentColor.equals(radioButtonColor4.getTag())) {
            radioButtonColor4.setChecked(true);
        } else if (currentColor.equals(radioButtonColor5.getTag())) {
            radioButtonColor5.setChecked(true);
        } else if (currentColor.equals(radioButtonColor6.getTag())) {
            radioButtonColor6.setChecked(true);
        } else if (currentColor.equals(radioButtonColor7.getTag())) {
            radioButtonColor7.setChecked(true);
        }

        AlertDialog dialog = builder.create();

        buttonEditTag.setOnClickListener(view -> {
            String newTag = editTextTag.getText().toString().trim();
            String selectedColor = null;

            if (radioButtonColor1.isChecked()) {
                selectedColor = (String) radioButtonColor1.getTag();
            } else if (radioButtonColor2.isChecked()) {
                selectedColor = (String) radioButtonColor2.getTag();
            } else if (radioButtonColor3.isChecked()) {
                selectedColor = (String) radioButtonColor3.getTag();
            } else if (radioButtonColor4.isChecked()) {
                selectedColor = (String) radioButtonColor4.getTag();
            } else if (radioButtonColor5.isChecked()) {
                selectedColor = (String) radioButtonColor5.getTag();
            } else if (radioButtonColor6.isChecked()) {
                selectedColor = (String) radioButtonColor6.getTag();
            } else if (radioButtonColor7.isChecked()) {
                selectedColor = (String) radioButtonColor7.getTag();
            }

            if (newTag.isEmpty() || selectedColor.isEmpty()) {
                showAlert("Vui lòng điền đầy đủ thông tin!");
                return;
            } else {
                tag.setTagName(newTag);
                tag.setTagColor(selectedColor);

                int result = db.updateTag(tag); // Assuming you have an updateTag method in your DBHelper class
                if (result > 0) {
                    refreshSpinner();
                    Toast.makeText(getContext(), "Sửa nhãn thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Có lỗi xảy ra khi sửa nhãn", Toast.LENGTH_SHORT).show();
                }
            }
            dialog.dismiss();
        });

        dialog.show();
    }

    // Phương thức xử lý sự kiện xóa Tag
    private void deleteTag(Tag tag) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Xóa nhãn");
        builder.setMessage("Bạn có chắc chắn muốn xóa nhãn này không?");

        builder.setPositiveButton("Xóa", (dialog, which) -> {
            int result = db.deleteTag(tag.getTagID()); // Assuming you have a deleteTag method in your DBHelper class
            if (result > 0) {
                refreshSpinner();
                showAlert("Xóa nhãn thành công");
            } else {
                showAlert("Có lỗi xảy ra khi xóa nhãn");
            }
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}