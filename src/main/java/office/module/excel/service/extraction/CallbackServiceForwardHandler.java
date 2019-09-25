package office.module.excel.service.extraction;

import com.aspose.cells.SaveFormat;
import com.aspose.cells.Workbook;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import office.module.callback.service.HttpCallbackRequest;
import office.module.excel.model.response.ExtractSheetResponse;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.Map;

@Slf4j
@AllArgsConstructor
public class CallbackServiceForwardHandler implements HttpCallbackRequest {
    private Workbook workbook;
    private Exception error;

    @Override
    public ExtractSheetResponse getBody(Object body, Map<String, Object> args) {
        log.info("call CallbackServiceForwardHandler::getBody");
        byte[] raw = null;
        String messenge = error.getMessage();
        String fileName = workbook.getBuiltInDocumentProperties().get("filename").getValue().toString();
        boolean isSuccessed = false;
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            log.info("writing to outstream : {}", workbook);
            workbook.save(outputStream, SaveFormat.XLSX);
            raw = outputStream.toByteArray();
            outputStream.close();
            outputStream.flush();
            isSuccessed = true;
        } catch (Exception e) {
            log.error("get raw body content error", e);
            messenge = "extract failed : " + e.getMessage();
        }

        ExtractSheetResponse result = new ExtractSheetResponse();
        result.setName(fileName);
        result.setData(raw);
        result.setLength(raw == null ? 0 : raw.length);
        result.setCreateDate(new Date());
        result.setSuccess(isSuccessed);
        result.setMessage(messenge);
        log.info("ExtractSheetResponse : {}", result);
        return result;
    }

    @Override
    public Map<String, String> getHeaders(Map<String, String> defaultHeader) {
        return defaultHeader;
    }
}
