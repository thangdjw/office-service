package office.module.excel.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ExtractorMapperRequest {
    private List<String> pinSheet;
    private List<String> hiddenSheet;
    private List<String> exceptSheet;
}
