import com.aspose.cells.*;
import office.module.callback.model.entity.CallbackRestModel;
import office.module.callback.service.CallbackService;
import office.module.callback.service.HttpCallbackRequest;
import office.module.excel.model.response.ExtractSheetResponse;
import office.module.excel.service.extraction.CallbackServiceForwardHandler;
import office.module.excel.service.extraction.ExtractStrategy;
import office.module.excel.service.extraction.SimpleExtractorService;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

public class Random {

    @Test
    public void testExtract() throws IOException, InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        new CallbackService().getCallback("callbacktest1");
        ExtractStrategy strategy = new ExtractStrategy.Builder()
                .onComplete((w) -> {
                    System.out.println("call on handle");
//                    return CallbackService.call(callbackModel, new CallbackServiceForwardHandler(w, e));
                    ExtractSheetResponse body = new CallbackServiceForwardHandler(w).getBody(null, null);
                    System.out.println(body);
                })
                .build();

        SimpleExtractorService.extractSheet(FileUtils.readFileToByteArray(new File("./src/test/resources/test.xlsx")), strategy);
        countDownLatch.await();
    }

    @Test
    public void test() throws ExecutionException, InterruptedException {
        CallbackService callbackService = new CallbackService();
        CallbackRestModel callbacktest1 = callbackService.getCallback("callbacktest1");
        CallbackService.call(callbacktest1, new HttpCallbackRequest() {
            @Override
            public Object getBody(Object body, Map<String, Object> args) {
                return "hello world";
            }

            @Override
            public Map<String, String> getHeaders(Map<String, String> defaultHeader) {
                return null;
            }
        });
        System.out.println("call success");
    }

    @Test
    public void testLib() throws Exception {
        Workbook sourceWorkbook = new Workbook(new FileInputStream("./src/test/resources/test.xlsx"));

        WorksheetCollection sourceWorksheetCollection = sourceWorkbook.getWorksheets();

        NameCollection sheetNames = sourceWorksheetCollection.getNames();
        System.out.println("num of sheet : " + sheetNames.getCount());
        List<File> result = new ArrayList();
        for (int i = 0; i < sheetNames.getCount() - 1; i++) {
            Workbook targetWorkbook = new Workbook();
            WorksheetCollection targetWorksheetCollection = targetWorkbook.getWorksheets();
            Worksheet sourceWorksheet = sourceWorksheetCollection.get(i);
            String sheetName = sourceWorksheet.getName();
            Worksheet targetWorksheet = targetWorksheetCollection.add(sheetName);
            targetWorksheet.copy(sourceWorksheet);
            System.out.println("current sheet name : " + sheetName);
            File targetFile = new File("./src/test/resources/extract/" + sheetName + ".xlsx");
            targetWorkbook.save(new FileOutputStream(targetFile), SaveFormat.XLSX);
            result.add(targetFile);
//            Desktop.getDesktop().open(targetFile);
        }

    }
}
