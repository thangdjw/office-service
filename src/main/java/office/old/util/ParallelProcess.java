package office.old.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ParallelProcess<T> {
    private ArrayList<CompletableFuture<T>> processPool = new ArrayList<>();

    public void addProcess(Supplier<T> process) {
        processPool.add(CompletableFuture.supplyAsync(process));
    }

    public CompletableFuture<List<T>> execute() {
        CompletableFuture[] pendingJob = processPool.toArray(new CompletableFuture[0]);
        return CompletableFuture
                .anyOf(pendingJob)
                .thenApply((e) -> processPool
                        .stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList()));
    }

}
