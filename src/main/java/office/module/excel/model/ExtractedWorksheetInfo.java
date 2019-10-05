package office.module.excel.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ExtractedWorksheetInfo {
    private String fileName;
    private String sheetName;
    private int sheetIndex;
}
