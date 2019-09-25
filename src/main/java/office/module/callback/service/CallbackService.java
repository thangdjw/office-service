package office.module.callback.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import kong.unirest.*;
import lombok.extern.slf4j.Slf4j;
import office.module.callback.model.entity.CallbackRestModel;
import office.module.callback.model.request.CallbackRegisterRequest;
import office.module.callback.model.request.CallbackRestModelRequest;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class CallbackService {
    private static final String CALLBACK_REQUEST_HOLDER_PATH = "CallbackRequestHolder";
    private static Map<String, CallbackRestModel> requestModel;

    static {
        configKong();
        loadModel();
    }

    private static void configKong() {
        ObjectMapper objectMapper = new ObjectMapper() {
            com.fasterxml.jackson.databind.ObjectMapper mapper
                    = new com.fasterxml.jackson.databind.ObjectMapper();

            public String writeValue(Object value) {
                try {
                    return mapper.writeValueAsString(value);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            public <T> T readValue(String value, Class<T> valueType) {
                try {
                    return mapper.readValue(value, valueType);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
        Unirest.config().setObjectMapper(objectMapper);

    }

    public HttpResponse<Empty> call(CallbackRestModel callbackModel, HttpCallbackRequest callbackBinding) {
        CallbackRestModel model = requestModel.get(callbackModel.getCallbackKey());
        Object bodyContent = callbackBinding.getBody(model.getBody(), model.getArguments());
        log.info("being http call with body : {}", bodyContent);
        RequestBodyEntity requestContent = Unirest.request(model.getRequestMethod().name(), model.getTargetUrl())
                .headers(Optional.ofNullable(model.getHeaders()).orElse(new HashMap<>()))
                .body(bodyContent);
        if (bodyContent != null && model.getRequestMethod().equals(HttpMethod.GET)
                || model.getRequestMethod().equals(HttpMethod.HEAD)) {
            requestContent.queryString((Map<String, Object>) bodyContent);
        }
        HttpResponse callbackResponse = requestContent.asEmpty();
        log.info("callback response : {}", callbackResponse);
        if (callbackResponse.isSuccess()) log.info("calback success to : " + callbackModel.getTargetUrl());
        else log.error("callback failed to : " + callbackModel.getTargetUrl(), callbackResponse);
        return callbackResponse;
    }

    public static void loadModel() {
        try {
            FileInputStream fileInputStream = new FileInputStream(CALLBACK_REQUEST_HOLDER_PATH);
            ObjectInputStream inputStream = new ObjectInputStream(fileInputStream);
            requestModel = (Map) inputStream.readObject();
            log.info("requestModel have been loaded : {}", requestModel);
        } catch (IOException | ClassNotFoundException e) {
            log.error("requestModel load failed", e);
            requestModel = new HashMap<>();
        }
    }

    public void addCallback(String url, CallbackRegisterRequest registerRequest) throws Exception {
        log.info("call addCallback() with:\n url: {}, registerRequest: {}", url, ReflectionToStringBuilder.toString(registerRequest));
        if (!isValidApi(registerRequest.getRequestModel()))
            throw new Exception("provided target url is not valid");
        CallbackRestModel model = new CallbackRestModel();
        model.setCallbackKey(registerRequest.getCallbackKey());
        model.setTargetUrl(registerRequest.getRequestModel().getTargetUrl());
        model.setBody(registerRequest.getRequestModel().getBody());
        model.setRequestMethod(registerRequest.getRequestModel().getRequestMethod());
        model.setHeaders(registerRequest.getRequestModel().getHeader());
        model.setArguments(registerRequest.getArguments());
        requestModel.put(url, model);
        save();
    }

    private boolean isValidApi(CallbackRestModelRequest modelRequest) {
        Map<String, String> pingHeaders = Optional.ofNullable(modelRequest.getHeader()).orElse(new HashMap<>());
        pingHeaders.put("connect-type", "ping");
        HttpResponse pingResponse = Unirest.request(modelRequest.getRequestMethod().name(), modelRequest.getTargetUrl())
                .headers(pingHeaders)
                .asEmpty();
        HttpStatus status = HttpStatus.valueOf(pingResponse.getStatus());
        log.info("validate endpoint url : {}", ReflectionToStringBuilder.toString(pingResponse));
        return status.is2xxSuccessful()
                || status.is3xxRedirection();
    }

    public Map<String, CallbackRestModel> list() {
        return requestModel;
    }

    public CallbackRestModel getCallback(String callbackKey) {
        return requestModel.get(callbackKey);
    }

    public void save() throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(CALLBACK_REQUEST_HOLDER_PATH);
        ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream);
        outputStream.writeObject(requestModel);
        log.info("saved requestModel : {}", requestModel);
    }
}
