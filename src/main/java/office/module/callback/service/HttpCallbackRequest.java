package office.module.callback.service;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@Getter @Setter @ToString
public abstract class HttpCallbackRequest<R> {
    protected R body;
    protected Map<String, String> headers = new HashMap<>();
}
