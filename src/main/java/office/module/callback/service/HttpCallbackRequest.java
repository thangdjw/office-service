package office.module.callback.service;

import com.fasterxml.jackson.databind.JsonNode;
import office.module.callback.model.entity.CallbackRestModel;

import java.util.Map;

public interface HttpCallbackRequest {
    Object getBody(Object body, Map<String, Object> args);

    Map<String, String> getHeaders(Map<String, String> defaultHeader);
}
