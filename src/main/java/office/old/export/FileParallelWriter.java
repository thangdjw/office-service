package office.old.export;


import com.google.common.base.Stopwatch;
import office.old.util.ParallelProcess;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class FileParallelWriter extends ParallelProcess<File> {
    private String path;

    public FileParallelWriter(String path) {
        this.path = path;
    }

    public void addWriteProcess(Writeable job) {
        super.addProcess(() -> {
            String targetFilePath = this.path + "/" + job.getProperties().get("name");
            File targetFile = new File(targetFilePath);
            try {
                FileOutputStream outputStream = new FileOutputStream(targetFile);
                job.write(outputStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return targetFile;
        });
    }

    public FileParallelWriter addBucketPackage(List<? extends Writeable> writeable) {
        writeable.forEach(this::addWriteProcess);
        return this;
    }

    public List<File> write() throws ExecutionException, InterruptedException {
        Stopwatch timer = Stopwatch.createStarted();
        System.out.println("start write to path : " + this.path);

        return super.execute().thenApply((file) -> {
            System.out.println("write complete ! total write time : " + timer);
            return file;
        }).get();
    }
}
