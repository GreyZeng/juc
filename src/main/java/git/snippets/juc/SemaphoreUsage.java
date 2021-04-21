package git.snippets.juc;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Semaphore用于限流
 */
public class SemaphoreUsage {
    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(1);
        new Thread(() -> {
            try {
                semaphore.acquire();
                TimeUnit.SECONDS.sleep(2);
                System.out.println("Thread 1 executed");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                semaphore.release();
            }
        }).start();

        new Thread(() -> {
            try {
                semaphore.acquire();
                TimeUnit.SECONDS.sleep(2);
                System.out.println("Thread 2 executed");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                semaphore.release();
            }
        }).start();
    }
}
