package office.module.excel.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@ToString
public class ExtractSheetResponse implements Serializable {
    @JsonProperty("file_name")
    private String fileName;
    @JsonProperty("sheet_name")
    private String sheetName;
    private String type;
    private String data;
    private long size;
    @JsonProperty("sheet_index")
    private int sheetIndex;
    @JsonProperty("create_date")
    private Date createDate;
    @JsonProperty("is_success")
    private boolean isSuccess;
    private String message;
    @JsonProperty("owner_id")
    private String ownerId;
    @JsonProperty("reference_id")
    private String referenceId;
}
