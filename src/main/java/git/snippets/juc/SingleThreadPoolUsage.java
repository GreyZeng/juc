package git.snippets.juc;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.concurrent.TimeUnit.SECONDS;

public class SingleThreadPoolUsage {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService service = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 10; i++) {
            final int j = i;
            service.submit(() -> System.out.println("current thread " + Thread.currentThread() + "  " + j));
        }
        service.shutdown();
        service.awaitTermination(60, SECONDS);
    }
}
