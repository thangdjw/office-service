package office.module.excel.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ExtractStrategyArgumentRequest {
    private List<String> exceptsheet;
    private List<String> pinsheet;
    private List<String> hiddensheet;
}
