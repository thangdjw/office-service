package office.module.excel.service.extraction;

import com.aspose.cells.Workbook;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import office.module.excel.model.ExtractedWorksheetInfo;

import java.util.*;
import java.util.function.BiFunction;

@Getter
@Slf4j
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ExtractStrategy {
    private List<String> pinSheet;
    private List<String> exceptSheet;
    private List<String> hiddenSheet;
    private BiFunction<Workbook, ExtractedWorksheetInfo, Object> onComplete;
    private BiFunction<Exception, ExtractedWorksheetInfo, Object> onError;

    public static class Builder {
        private List<String> pinSheet = new ArrayList<>();
        private List<String> exceptSheet = new ArrayList<>();
        private List<String> hiddenSheet = new ArrayList<>();
        private BiFunction<Workbook, ExtractedWorksheetInfo, Object> onComplete = (w, i)->null;
        private BiFunction<Exception, ExtractedWorksheetInfo, Object> onError = (e, i)->null;

        public Builder pinSheet(List<String> pinSheet){
            this.pinSheet = Optional.ofNullable(pinSheet).orElse(Collections.emptyList());
            return this;
        }

        public Builder exceptSheet(List<String> exceptSheet){
            this.exceptSheet = Optional.ofNullable(exceptSheet).orElse(Collections.emptyList());
            return this;
        }

        public Builder hiddenSheet(List<String> hiddenSheet){
            this.hiddenSheet = Optional.ofNullable(hiddenSheet).orElse(Collections.emptyList());
            return this;
        }

        public Builder onComplete(BiFunction<Workbook, ExtractedWorksheetInfo, Object> action){
            this.onComplete = Optional.ofNullable(action).orElse((w, i)->w);
            return this;
        }

        public Builder onError(BiFunction<Exception, ExtractedWorksheetInfo, Object> action){
            this.onError = Optional.ofNullable(action).orElse((e, i)->{
                e.printStackTrace();
                return e;
            });
            return this;
        }

        public ExtractStrategy build(){
            return new ExtractStrategy(pinSheet, exceptSheet, hiddenSheet, onComplete, onError);
        }

    }
}

