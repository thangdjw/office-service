package office.old.excel.event;

import office.old.export.Writeable;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbookType;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.function.Supplier;

public class WorkbookHandler implements Writeable {
    private String name;
    private XSSFWorkbookType type;
    private boolean writable;
    private Workbook workbook;
    private List<WbEvent> events;
    private Properties properties;

    public WorkbookHandler(Workbook wb) {
        this.workbook = wb;
        this.writable = true;
        this.type = XSSFWorkbookType.XLSX;
        this.name = getName();
        this.events = new ArrayList<>();
        this.properties = new Properties();
    }

    public void pushEvent(String type, Supplier<WorkbookHandler> handle) {
        if (type == null) throw new NullPointerException();
        events.add(new WbEvent(type, handle));
    }

    public WbEvent popEvent(int index) {
        return events.remove(index);
    }

    public WbEvent getEvent(int index) {
        return events.get(index);
    }

    public String getName() {
        return this.name == null ? this.workbook.hashCode() + "." + type.toString().toLowerCase() : this.name;
    }

    public void setName(String name) {
        this.name = name + "." + type.toString().toLowerCase();
    }

    public boolean isWritable() {
        return writable;
    }

    public void setWritable(boolean writable) {
        this.writable = writable;
    }

    public List<WbEvent> getEvents() {
        return events;
    }

    public void setEvents(List<WbEvent> events) {
        this.events = events;
    }

    public Workbook getWorkbook() {
        return workbook;
    }

    @Override
    public void write(OutputStream out) {
        try {
            workbook.write(out);
            System.out.println("write complete !");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Properties getProperties() {
        properties.put("name", this.name);
        properties.put("writable", this.writable);
        return properties;
    }
}
