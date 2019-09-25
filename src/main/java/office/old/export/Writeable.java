package office.old.export;

import java.io.OutputStream;
import java.util.Properties;

public interface Writeable {
    void write(OutputStream out);
    Properties getProperties();
}
