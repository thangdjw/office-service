package office.module.excel.service.extraction;

import com.aspose.cells.Workbook;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.function.Function;

@Getter
@Slf4j
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ExtractStrategy {
    private List<String> pinSheet;
    private List<String> exceptSheet;
    private List<String> hiddenSheet;
    private Function<Workbook, Object> onComplete;
    private Function<Exception, Object> onError;
}

