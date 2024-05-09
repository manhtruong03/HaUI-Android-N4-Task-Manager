package haui.android.taskmanager.controller;

import java.io.InputStream;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

public class ExcelReader {

    public static String readExcelFile(InputStream inputStream) {
        Workbook workbook = null;
        StringBuilder contentBuilder = new StringBuilder();

        try {
            // Load the Excel file
            workbook = Workbook.getWorkbook(inputStream);

            // Get the first sheet
            Sheet sheet = workbook.getSheet(0);

            // Loop through the rows and columns
            for (int row = 0; row < sheet.getRows(); row++) {
                for (int col = 0; col < sheet.getColumns(); col++) {
                    Cell cell = sheet.getCell(col, row);
                    System.out.print(cell.getContents() + "\t");
                    contentBuilder.append(cell.getContents()).append("|");
                }
                contentBuilder.append("\n");
                System.out.println();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (workbook != null) {
                workbook.close();
            }
        }

        return contentBuilder.toString();
    }
}
