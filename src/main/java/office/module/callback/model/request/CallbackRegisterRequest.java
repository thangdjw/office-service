package office.module.callback.model.request;

import lombok.*;

import java.io.Serializable;
import java.util.Map;

@Getter @Setter @ToString
public class CallbackRegisterRequest implements Serializable {
    @NonNull
    private String callbackKey;
    @NonNull
    private CallbackRestModelRequest requestModel;
    private Map<String, Object> arguments;
}
