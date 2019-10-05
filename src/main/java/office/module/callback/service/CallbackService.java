package office.module.callback.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import kong.unirest.*;
import lombok.extern.slf4j.Slf4j;
import office.configuration.KongConfig;
import office.module.callback.model.entity.CallbackRestModel;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class CallbackService {

    static {
        new KongConfig().initKongUnirest();
    }


    public HttpResponse<Empty> call(CallbackRestModel callbackRestModel, HttpCallbackRequest callbackRequest) {
        log.info("being callback : {}", callbackRestModel.getTargetUrl());
        Map<String, String> headers = new HashMap<>(callbackRestModel.getHeaders());
        headers.putAll(callbackRequest.getHeaders());
        HttpRequestWithBody requestContent = Unirest.post(callbackRestModel.getTargetUrl())
                .headers(headers);
        HttpRequest request;

        if ("multipart/form-data".equals(headers.get("Content-Type"))
                || "application/x-www-form-urlencoded".equals(headers.get("Content-Type"))) {
            log.info("send body thought fields");
            Map<String, Object> multipartValues = new ObjectMapper().convertValue(callbackRequest.getBody(), Map.class);
            request = requestContent.fields(multipartValues);
        } else {
            log.info("send body thought raw");
            request = requestContent.body(callbackRequest.getBody());

        }
        try {
            HttpResponse callbackResponse = request.asEmpty();
            log.info("callback response : {}", callbackResponse.getStatusText());
            return callbackResponse;
        } catch (Exception e) {
            log.error("callback failed to : " + callbackRestModel.getTargetUrl(), e);
            return null;
        }
    }

}
