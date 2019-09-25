package office.module.excel.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import office.module.callback.model.entity.CallbackRestModel;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class ExcelExtractRequest {
    @NotNull
    private MultipartFile file;
    @NotNull
    private CallbackRestModel callbackModel;
}
