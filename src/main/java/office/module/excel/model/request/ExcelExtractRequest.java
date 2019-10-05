package office.module.excel.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import office.module.callback.model.entity.CallbackRestModel;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@ToString
public class ExcelExtractRequest {
    private String file;
    @JsonProperty("callback_model")
    private CallbackRestModel callbackModel;
}
