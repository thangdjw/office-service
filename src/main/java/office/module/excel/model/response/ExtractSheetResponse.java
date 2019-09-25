package office.module.excel.model.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class ExtractSheetResponse {
    private String name;
    private byte[] data;
    private int length;
    private Date createDate;
    private boolean isSuccess;
    private String message;
}
