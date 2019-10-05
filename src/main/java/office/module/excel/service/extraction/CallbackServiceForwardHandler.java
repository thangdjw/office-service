package office.module.excel.service.extraction;

import com.aspose.cells.SaveFormat;
import com.aspose.cells.Workbook;
import com.google.common.io.Files;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import office.module.callback.service.HttpCallbackRequest;
import office.module.excel.model.ExtractedWorksheetInfo;
import office.module.excel.model.request.ExtractStrategyArgumentRequest;
import office.module.excel.model.response.ExtractSheetResponse;
import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;

@Slf4j
@AllArgsConstructor
public class CallbackServiceForwardHandler extends HttpCallbackRequest<ExtractSheetResponse> {

    private ExtractStrategyArgumentRequest strategyRequest;

    public void setBody(Workbook workbook, ExtractedWorksheetInfo info) {
        log.info("save to stream");
        byte[] raw = null;
        String message = null;
        boolean isSuccess = false;
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.save(outputStream, SaveFormat.XLSX);
            raw = outputStream.toByteArray();
            outputStream.close();
            outputStream.flush();
            isSuccess = true;
        } catch (Exception e) {
            log.error("get raw body content error", e);
            message = e.getMessage();
        }

        ExtractSheetResponse result = new ExtractSheetResponse();
        String sheetName = info.getSheetName();
        result.setFileName(sheetName + ".xlsx");
        result.setType("XLSX");
        result.setSheetName(sheetName);
        result.setData(Base64.encodeBase64String(raw));
        result.setSheetIndex(info.getSheetIndex());
        result.setSize(raw == null ? 0 : raw.length);
        result.setCreateDate(new Date());
        result.setSuccess(isSuccess);
        result.setMessage(message);
        result.setOwnerId(strategyRequest.getOwnerId());
        result.setReferenceId(strategyRequest.getReferenceId());
//        log.info("ExtractSheetResponse : {}", result);
        this.body = result;
    }

    public void setErrorBody(Exception error, ExtractedWorksheetInfo info) {
        ExtractSheetResponse result = new ExtractSheetResponse();
        result.setSheetName(info.getSheetName());
        result.setCreateDate(new Date());
        result.setType("XLSX");
        result.setSuccess(false);
        result.setMessage(error.getMessage());
        result.setOwnerId(strategyRequest.getOwnerId());
        result.setReferenceId(strategyRequest.getReferenceId());
        log.info("Error body : {}", result);
        this.body = result;
    }

}
