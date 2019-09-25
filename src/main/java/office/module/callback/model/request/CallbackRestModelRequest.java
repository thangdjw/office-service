package office.module.callback.model.request;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;
import org.springframework.http.HttpMethod;

import java.io.Serializable;
import java.util.Map;

@Getter @Setter @ToString
public class CallbackRestModelRequest implements Serializable {
    @NonNull
    private String targetUrl;
    private HttpMethod requestMethod = HttpMethod.POST;
    private Map<String, String> header;
    private JsonNode body;
}
