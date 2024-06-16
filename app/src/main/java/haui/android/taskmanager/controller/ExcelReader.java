package haui.android.taskmanager.controller;

import static android.content.ContentValues.TAG;

import android.util.Log;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import haui.android.taskmanager.models.Task;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

public class ExcelReader {

    public static List<String> readExcelFile(InputStream inputStream) {
        Workbook workbook = null;
        StringBuilder contentBuilder = new StringBuilder();

        List<String> sheetContent = new ArrayList<String>();
        try {
            // Tải tệp Excel
            workbook = Workbook.getWorkbook(inputStream);
            // Lấy sheet đầu tiên
            Sheet sheet = workbook.getSheet(0);

            // Lặp qua các hàng và cột
            for (int row = 0; row < sheet.getRows(); row++) {
                String rowContent = "";
                for (int col = 0; col < sheet.getColumns(); col++) {
                    if (row <= 6 || col == 0 || col >= 10) { continue; }
                    Cell cell = sheet.getCell(col, row);
                    rowContent += (cell.getContents() + "|");
                }
                if (!rowContent.contains("|||") && !rowContent.equals("")) {
                    sheetContent.add(rowContent);
                    Log.d(TAG, "readExcelFile: " + rowContent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (workbook != null) {
                workbook.close();
            }
        }

        return sheetContent;
    }
}
