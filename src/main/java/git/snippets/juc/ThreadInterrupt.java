package git.snippets.juc;

import java.util.concurrent.TimeUnit;

public class ThreadInterrupt {
    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(() ->{
            for(;;) {
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("current thread interrupted");
                    System.out.println(Thread.currentThread().isInterrupted());
                    break;
                }
            }
        });
        t.start();
        TimeUnit.SECONDS.sleep(3);
        t.interrupt();
    }
}
