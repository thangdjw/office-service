import com.aspose.cells.CalculationOptions;
import com.aspose.cells.NameCollection;
import com.aspose.cells.Workbook;
import com.aspose.cells.WorksheetCollection;
import com.google.common.io.Files;
import office.module.excel.model.request.ExtractStrategyArgumentRequest;
import office.module.excel.service.extraction.CallbackServiceForwardHandler;
import office.module.excel.service.extraction.ExtractStrategy;
import office.module.excel.service.extraction.SimpleExtractorService;
import org.apache.commons.codec.binary.Base64;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;

public class Random {

    @Test
    public void testAPI() throws IOException, InterruptedException {
        byte[] fileRaw = Files.toByteArray(new File("./src/test/resources/09_2019_DS P1_luong_kinh doanh_thang_09.2019.xlsx"));
        ExtractStrategy strategy = new ExtractStrategy.Builder()
//                .pinSheet(Arrays.asList("Tonghop", "data", "rp"))
//                .hiddenSheet(Arrays.asList("Tonghop", "data", "rp"))
                .onComplete((workbook, info) -> {
                    try {
                        String path = "./src/test/resources/extract/" + info.getSheetName()+".xlsx";
                        System.out.println("save file to : " + path);
                        workbook.calculateFormula();
                        workbook.save(path);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .onError((error, info) -> {
                    CallbackServiceForwardHandler forwardHandle = new CallbackServiceForwardHandler(new ExtractStrategyArgumentRequest());
                    forwardHandle.setErrorBody(error, info);
                    error.printStackTrace();
                    return null;
                })
                .build();
        int count = new SimpleExtractorService().extractWorkbook(fileRaw, strategy);
        System.out.println("count : "+count);
        Thread.sleep(20000);
    }

    @Test
    public void calculateFormular() throws Exception {
        String path = "./src/test/resources/09_2019_DS P1_luong_kinh doanh_thang_09.2019.xlsx";
        Workbook sourceWorkbook = new Workbook(path);
        WorksheetCollection names = sourceWorkbook.getWorksheets();
        int count = names.getCount();
        System.out.println(count);
        for(int i = 0; i<count; i++){
            System.out.println(sourceWorkbook.getWorksheets().get(i).getName());
        }

        Desktop.getDesktop().open(new File(path));
    }

    @Test
    public void calculateFormular1() throws Exception {
        ZipSecureFile.setMinInflateRatio(0);
        String path = "./src/test/resources/09_2019_DS P1_luong_kinh doanh_thang_09.2019.xlsx";
        org.apache.poi.ss.usermodel.Workbook workbook = WorkbookFactory.create(new File(path));
        System.out.println(workbook.getAllNames().size());
    }

    @Test
    public void mapperTest() throws IOException {
        List<String> input = Files.readLines(new File( "./src/test/resources/enc"), Charset.defaultCharset());
        byte[] data = Base64.decodeBase64(input.get(0));
        String tpath = "./src/test/resources/test-enc.xlsx";
        Files.write(data, new File(tpath));

    }

}
