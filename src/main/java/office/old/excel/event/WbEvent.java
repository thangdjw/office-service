package office.old.excel.event;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class WbEvent {
    private String type;
    private Supplier<WorkbookHandler> handle;
    private boolean runable;
    private Map<String, Object> props;

    public WbEvent(String type, Supplier<WorkbookHandler> handle) {
        this.type = type;
        this.handle = handle;
        this.runable = true;
        this.props = new HashMap<>();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Supplier<WorkbookHandler> getHandle() {
        return handle;
    }

    public void setHandle(Supplier<WorkbookHandler> handle) {
        this.handle = handle;
    }

    public boolean isRunable() {
        return runable;
    }

    public void setRunable(boolean runable) {
        this.runable = runable;
    }

    public Object getProperties(String name) {
        return props.get(name);
    }

    public void setProperties(String key, String value) {
        this.props.put(key, value);
    }
}
