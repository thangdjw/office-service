package office.module.excel.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import office.module.callback.model.entity.CallbackRestModel;
import office.module.callback.service.CallbackService;
import office.module.excel.model.request.ExcelExtractRequest;
import office.module.excel.model.request.ExtractStrategyArgumentRequest;
import office.module.excel.service.extraction.CallbackServiceForwardHandler;
import office.module.excel.service.extraction.ExtractStrategy;
import office.module.excel.service.extraction.SimpleExtractorService;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/excel")
public class ExcelController {
    @Autowired
    private CallbackService callbackService;
    @Autowired
    private SimpleExtractorService simpleExtractorService;
    private static ObjectMapper mapper = new ObjectMapper();

    @PostMapping("/extract-sheet")
    public ResponseEntity extractSheet(@RequestBody ExcelExtractRequest extractRequest) throws IOException {
        byte[] fileRaw = Base64.decodeBase64(extractRequest.getFile());
        CallbackRestModel callbackModel = extractRequest.getCallbackModel();
        log.info("extract callback model : {}", callbackModel);
        Map<String, Object> arguments = Optional.ofNullable(callbackModel.getArguments()).orElse(Collections.emptyMap());
        log.info("required callback arguments : {}", arguments);
        ExtractStrategyArgumentRequest extractStrategyArguments = mapper.convertValue(arguments, ExtractStrategyArgumentRequest.class);
        ExtractStrategy strategy = new ExtractStrategy.Builder()
                .exceptSheet(extractStrategyArguments.getExceptSheet())
                .pinSheet(extractStrategyArguments.getPinSheet())
                .hiddenSheet(extractStrategyArguments.getHiddenSheet())
                .onComplete((workbook, info) -> {
                    log.info("call complete action");
                    CallbackServiceForwardHandler forwardHandle = new CallbackServiceForwardHandler(extractStrategyArguments);
                    forwardHandle.setBody(workbook, info);
                    return callbackService.call(callbackModel, forwardHandle);
                })
                .onError((error, info) -> {
                    CallbackServiceForwardHandler forwardHandle = new CallbackServiceForwardHandler(extractStrategyArguments);
                    forwardHandle.setErrorBody(error, info);
                    return callbackService.call(callbackModel, forwardHandle);
                })
                .build();
        int count = simpleExtractorService.extractWorkbook(fileRaw, strategy);
        return ResponseEntity.ok(count);
    }

}
