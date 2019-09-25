package office.old.excel.event;

import java.util.List;
import java.util.function.Supplier;

public interface EventHandle {
    String getEventType();
    List<Supplier<WorkbookHandler>> getHandle(WorkbookHandler root, byte[] rawWorkbook);
}
