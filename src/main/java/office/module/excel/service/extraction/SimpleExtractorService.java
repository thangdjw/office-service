package office.module.excel.service.extraction;

import com.aspose.cells.NameCollection;
import com.aspose.cells.Workbook;
import com.aspose.cells.Worksheet;
import com.aspose.cells.WorksheetCollection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
@Slf4j
public class SimpleExtractorService {
    private static ExecutorService service;

    static {
        initExecutorService();
    }

    public static void initExecutorService() {
        if (service != null && service.isShutdown()) return;
        int availableThread = Runtime.getRuntime().availableProcessors();
        if (availableThread == 1) {
            service = Executors.newCachedThreadPool();
            log.info("init executer in cached threadpool mode");
        } else {
            service = Executors.newFixedThreadPool(availableThread - 1);
            log.info("init executer in fixed thread pool mode : " + (availableThread - 1));
        }

        log.info("Threadpool created");
    }

    public List<Future<Object>> extractSheet(byte[] data, ExtractStrategy strategy) {
        List<Future<Object>> tasks = new ArrayList<>();
        try {
            Workbook sourceWorkbook = new Workbook(new ByteArrayInputStream(data));
            WorksheetCollection sourceWorksheetCollection = sourceWorkbook.getWorksheets();
            NameCollection sheetNames = sourceWorksheetCollection.getNames();
            System.out.println("num of sheet : " + sheetNames.getCount());

            for (int i = 0; i < sheetNames.getCount() - 1; i++) {
                Worksheet sourceWorksheet = sourceWorksheetCollection.get(i);
                String sheetName = sourceWorksheet.getName();
                if (strategy.getExceptSheet().contains(sheetName)) continue;

                Callable<Object> task = () -> {
                    strategy.getPinSheet().add(sheetName);
                    Workbook targetWorkbook = new Workbook();
                    for (String pinsheetName : strategy.getPinSheet()) {
                        WorksheetCollection targetWorksheetCollection = targetWorkbook.getWorksheets();
                        Worksheet targetWorksheet = targetWorksheetCollection.add(pinsheetName);
                        try {
                            targetWorksheet.copy(sourceWorksheet);
                            return strategy.getOnComplete().apply(targetWorkbook);
                        } catch (Exception e) {
                            e.printStackTrace();
                            return strategy.getOnError().apply(e);
                        }
                    }
                    return targetWorkbook;
                };

                tasks.add(service.submit(task));
            }
        } catch (Exception e) {
            strategy.getOnError().apply(e);
        }
        return tasks;
    }
}
