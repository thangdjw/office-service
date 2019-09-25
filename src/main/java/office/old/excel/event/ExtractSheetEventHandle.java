package office.old.excel.event;

import com.google.common.base.Stopwatch;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class ExtractSheetEventHandle implements EventHandle {
    public static final String EVENT_TYPE_EXTRACT_SHEET = "extract-sheet";
    private String[] pinSheet;

    public ExtractSheetEventHandle(String... pinSheet) {
        this.pinSheet = pinSheet;
    }

    @Override
    public String getEventType() {
        return EVENT_TYPE_EXTRACT_SHEET;
    }

    @Override
    public List<Supplier<WorkbookHandler>> getHandle(WorkbookHandler handler, byte[] rawWorkbook) {
        List<Supplier<WorkbookHandler>> result = new ArrayList<>();
        for (Sheet sheet : handler.getWorkbook()) {
            result.add(() -> {
                Stopwatch stopWatch = Stopwatch.createStarted();
                String curSheetName = sheet.getSheetName();
                String[] ignoreSheet = ArrayUtils.add(this.pinSheet, curSheetName);
                System.out.println("begin extract sheet : " + Arrays.asList(ignoreSheet));
                try {
                    Workbook targetWorkbook = new XSSFWorkbook(new ByteArrayInputStream(rawWorkbook));
                    removeUnnecessarySheet(targetWorkbook, ignoreSheet);
                    WorkbookHandler workbookHandler = new WorkbookHandler(targetWorkbook);
                    workbookHandler.setName(curSheetName);
                    System.out.println("extract complete " + curSheetName + " : " + stopWatch);
                    return workbookHandler;
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("extract complete : " + stopWatch);
                    return null;
                }
            });
        }
        return result;
    }

    private void removeUnnecessarySheet(Workbook workbook, String[] ignoreSheet) {

        List<String> curIgnoreSheet = new ArrayList<>(Arrays.asList(ignoreSheet));
        int p = 0;
        while (p < workbook.getNumberOfSheets()) {
            int i = workbook.getNumberOfSheets() - 1 - p;
            String entrySheetName = workbook.getSheetName(i);
            if (curIgnoreSheet.contains(entrySheetName)) {
                p++;
            } else {
                workbook.removeSheetAt(i);
            }
        }
    }

}
