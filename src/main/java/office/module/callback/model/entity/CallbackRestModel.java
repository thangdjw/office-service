package office.module.callback.model.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpMethod;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

@Getter
@Setter
@ToString
public class CallbackRestModel implements Serializable {
    @NotNull
    private String targetUrl;
    @NotNull
    private String callbackKey;
    private HttpMethod requestMethod;
    private Map<String, String> headers;
    private Object body;
    private Map<String, Object> arguments;
    private Date createDate;
}
