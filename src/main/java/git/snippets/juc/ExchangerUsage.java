package git.snippets.juc;

import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;

/**
 * Exchanger用于两个线程之间交换变量
 */
public class ExchangerUsage {
    static Exchanger<String> semaphore = new Exchanger<>();

    public static void main(String[] args) {

        new Thread(() -> {
            String s = "T1";
            try {
                s = semaphore.exchange(s);
                TimeUnit.SECONDS.sleep(2);
                System.out.println("Thread 1(T1) executed, Result is " + s);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            String s = "T2";
            try {
                s = semaphore.exchange(s);
                TimeUnit.SECONDS.sleep(2);
                System.out.println("Thread 2(T2) executed, Result is " + s);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
