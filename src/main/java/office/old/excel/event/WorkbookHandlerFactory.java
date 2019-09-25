package office.old.excel.event;

import com.google.common.base.Stopwatch;
import office.old.util.ParallelProcess;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class WorkbookHandlerFactory {
    private byte[] rawWorkbook;
    private WorkbookHandler rootHandler;

    private WorkbookHandlerFactory(byte[] input) throws IOException {
        ZipSecureFile.setMinInflateRatio(0);
        Stopwatch timer = Stopwatch.createStarted();
        this.rawWorkbook = input;
        Workbook sourceWorkbook = WorkbookFactory.create(new ByteArrayInputStream(input));
        this.rootHandler = new WorkbookHandler(sourceWorkbook);
        System.out.println(timer + " > reading completed ! ");
    }

    public static WorkbookHandlerFactory read(String path) throws IOException {
        File source = new File(path);
        byte[] raw = FileUtils.readFileToByteArray(source);
        WorkbookHandlerFactory instance = new WorkbookHandlerFactory(raw);
        return instance;
    }

    public static WorkbookHandlerFactory read(InputStream inputStream) throws IOException {
        byte[] raw = IOUtils.toByteArray(inputStream);
        WorkbookHandlerFactory instance = new WorkbookHandlerFactory(raw);
        return instance;
    }

    public static WorkbookHandlerFactory read(byte[] data) throws IOException {
        WorkbookHandlerFactory instance = new WorkbookHandlerFactory(data);
        return instance;
    }

    public WorkbookHandlerFactory addAction(EventHandle eventHandle) {
        List<Supplier<WorkbookHandler>> handle = eventHandle.getHandle(this.rootHandler, rawWorkbook);
        handle.forEach(h -> this.rootHandler.pushEvent(eventHandle.getEventType(), h));
        return this;
    }

    public CompletableFuture<List<WorkbookHandler>> executeForWorkbooks(Predicate<WbEvent> filter) throws ExecutionException, InterruptedException {
        ParallelProcess<WorkbookHandler> process = new ParallelProcess<>();
        this.rootHandler.getEvents().stream()
                .filter(filter)
                .map(WbEvent::getHandle)
                .forEach(process::addProcess);
        return process.execute();
    }

    public CompletableFuture<List<WorkbookHandler>> executeForAllWorkbooks() throws ExecutionException, InterruptedException {
        ParallelProcess<WorkbookHandler> process = new ParallelProcess<>();
        this.rootHandler.getEvents().stream()
                .map(WbEvent::getHandle)
                .forEach(process::addProcess);
        return process.execute();
    }

}
