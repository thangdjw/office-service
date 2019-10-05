package office.module.callback.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ToString
public class CallbackRestModel implements Serializable {
    @NotNull
    @JsonProperty("target_url")
    private String targetUrl;
    private Map<String, String> headers = new HashMap<>();
    private Map<String, Object> arguments = new HashMap<>();
    @JsonProperty("create_date")
    private Date createDate;
}
