package office.module.excel.service.extraction;

import com.aspose.cells.*;
import lombok.extern.slf4j.Slf4j;
import office.module.excel.model.ExtractedWorksheetInfo;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
public class SimpleExtractorService {
    private static ExecutorService async;

    static {
        initExecutorService();
    }

    public static void initExecutorService() {
        if (async != null && async.isShutdown()) return;
        int availableThread = Runtime.getRuntime().availableProcessors();
        if (availableThread == 1) {
            async = Executors.newCachedThreadPool();
            log.info("init executer in cached threadpool mode");
        } else {
            async = Executors.newFixedThreadPool(availableThread - 1);
            log.info("init executer in fixed thread pool mode : " + (availableThread - 1));
        }

        log.info("Threadpool created");
    }

    public int extractWorkbook(byte[] data, ExtractStrategy strategy) {
        try {
            Workbook sourceWorkbook = new Workbook(new ByteArrayInputStream(data));
            WorksheetCollection sourceWorksheetCollection = sourceWorkbook.getWorksheets();
            strategy.getExceptSheet().forEach(sourceWorksheetCollection::removeAt);
            sourceWorkbook.getSettings().setCreateCalcChain(false);
            sourceWorkbook.calculateFormula();

            System.out.println("num of sheet : " + sourceWorksheetCollection.getCount());

            for (int i = 0; i < sourceWorksheetCollection.getCount(); i++) {

                final int sheetIndex = i;
                async.submit(() -> {
                    Worksheet sourceWorksheet = sourceWorksheetCollection.get(sheetIndex);
                    String sheetName = sourceWorksheet.getName();
                    extractWorksheet(strategy, sourceWorkbook, sheetName, sheetIndex);
                });
            }
            return sourceWorksheetCollection.getCount();
        } catch (Exception e) {
            e.printStackTrace();
            ExtractedWorksheetInfo info = new ExtractedWorksheetInfo();
            info.setSheetName("root");
            strategy.getOnError().apply(e, info);
        }
        return 0;
    }

    private void extractWorksheet(ExtractStrategy strategy, Workbook sourceWorksbook, String sheetName, int sheetIndex) {
        List<String> includeSheets = new ArrayList<>(strategy.getPinSheet());
        includeSheets.add(0, sheetName);
        System.out.println("extract sheet set : " + includeSheets);
        Workbook targetWorkbook = new Workbook();
        targetWorkbook.getSettings().setCreateCalcChain(false);
        targetWorkbook.calculateFormula();
        targetWorkbook.getWorksheets().removeAt(0);
        ExtractedWorksheetInfo info = new ExtractedWorksheetInfo();
        info.setSheetName(sheetName);
        info.setSheetIndex(sheetIndex);
        boolean isHiddenAllSheet = strategy.getHiddenSheet().size() > 0
                && strategy.getHiddenSheet().containsAll(includeSheets);

        if (isHiddenAllSheet) {
            strategy.getOnComplete().apply(targetWorkbook, info);
            log.info("skip because hidden all");
            return;
        }
        WorksheetCollection targetWorksheetCollection = targetWorkbook.getWorksheets();
        for (String pinsheetName : includeSheets) {
            Worksheet targetWorksheet = targetWorksheetCollection.add(pinsheetName);
            Worksheet sourceWorksheet = sourceWorksbook.getWorksheets().get(pinsheetName);
            try {
//                log.info("copy sheet : {}", pinsheetName);
                CopyOptions options = new CopyOptions();
                options.setCopyNames(true);
                options.setReferToSheetWithSameName(true);
                options.setCopyInvalidFormulasAsValues(true);
                options.setReferToDestinationSheet(false);
                options.setExtendToAdjacentRange(true);
                targetWorksheet.copy(sourceWorksheet, options);
                if (strategy.getHiddenSheet().contains(pinsheetName)) {
                    targetWorksheet.setVisibilityType(VisibilityType.HIDDEN);
                }
            } catch (Exception e) {
                e.printStackTrace();
                strategy.getOnError().apply(e, info);
            }
        }
        strategy.getOnComplete().apply(targetWorkbook, info);
    }
}
