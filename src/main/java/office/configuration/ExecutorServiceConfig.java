package office.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ExecutorServiceConfig {

    @Bean("fixedThreadPool")
    public ExecutorService fixedThreadPool() {
        int availableThread = Runtime.getRuntime().availableProcessors();
        return Executors.newFixedThreadPool(availableThread);
    }
//
//    @Bean("singleThreaded")
//    public ExecutorService singleThreadedExecutor() {
//        return Executors.newSingleThreadExecutor();
//    }
//
//    @Bean("cachedThreadPool")
//    public ExecutorService cachedThreadPool() {
//        return Executors.newCachedThreadPool();
//    }
}
