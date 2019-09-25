package office.module.excel.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import office.module.callback.model.entity.CallbackRestModel;
import office.module.callback.service.CallbackService;
import office.module.callback.service.HttpCallbackRequest;
import office.module.excel.model.request.ExcelExtractRequest;
import office.module.excel.model.request.ExtractStrategyArgumentRequest;
import office.module.excel.model.response.ExtractSheetCountResponse;
import office.module.excel.service.extraction.CallbackServiceForwardHandler;
import office.module.excel.service.extraction.ExtractStrategy;
import office.module.excel.service.extraction.SimpleExtractorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Future;

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
    public ResponseEntity extractSheet(ExcelExtractRequest extractRequest) throws IOException {
        CallbackRestModel callbackModel = extractRequest.getCallbackModel();
        log.info("extract callback model : {}", callbackModel);
        Map<String, Object> arguments = Optional.ofNullable(callbackModel.getArguments()).orElse(Collections.emptyMap());
        log.info("required callback arguments : {}", arguments);
        ExtractStrategyArgumentRequest extractStrategyArguments = mapper.convertValue(arguments, ExtractStrategyArgumentRequest.class);
        ExtractStrategy strategy = ExtractStrategy.builder()
                .exceptSheet(extractStrategyArguments.getExceptsheet())
                .pinSheet(extractStrategyArguments.getPinsheet())
                .hiddenSheet(extractStrategyArguments.getHiddensheet())
                .onComplete((w) -> {
                    log.info("call callback for data");
                    HttpCallbackRequest forwardHandle = new CallbackServiceForwardHandler(w, null);
                    return callbackService.call(callbackModel, forwardHandle);
                })
                .onError((e) -> {
                    log.info("call callback for data");
                    HttpCallbackRequest forwardHandle = new CallbackServiceForwardHandler(null, e);
                    return callbackService.call(callbackModel, forwardHandle);
                })
                .build();
        List<Future<Object>> futures = simpleExtractorService.extractSheet(extractRequest.getFile().getBytes(), strategy);
        ExtractSheetCountResponse countResponse = new ExtractSheetCountResponse(futures.size());
        return ResponseEntity.ok(countResponse);
    }

}
