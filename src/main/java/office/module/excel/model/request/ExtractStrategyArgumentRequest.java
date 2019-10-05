package office.module.excel.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ExtractStrategyArgumentRequest {
    @JsonProperty("except_sheet")
    private List<String> exceptSheet;
    @JsonProperty("pin_sheet")
    private List<String> pinSheet;
    @JsonProperty("hidden_sheet")
    private List<String> hiddenSheet;
    @JsonProperty("owner_id")
    private String ownerId;
    @JsonProperty("reference_id")
    private String referenceId;
    @JsonProperty("type")
    private String type = "xlsx";
}
