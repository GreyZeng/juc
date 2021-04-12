package git.snippets.juc;

import java.util.concurrent.TimeUnit;

/**
 * 如何结束一个线程
 * 
 * @author <a href="mailto:410486047@qq.com">Grey</a>
 * @since 1.8
 */
public class ThreadFinished {
    private static volatile boolean flag = true;

    public static void main(String[] args) throws InterruptedException {
        // 推荐方式:设置标志位
        Thread t3 = new Thread(() -> {
            long i = 0L;
            while (flag) {
                i++;
            }
            System.out.println("count sum i = " + i);
        });
        t3.start();
        TimeUnit.SECONDS.sleep(1);
        flag = false;
        // 推荐方式:使用interrupt
        Thread t4 = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {

            }
            System.out.println("t4 end");
        });
        t4.start();
        TimeUnit.SECONDS.sleep(1);
        t4.interrupt();

        // 使用stop方法来结束线程,不推荐
        Thread t = new Thread(() -> {
            System.out.println("t start");
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                // e.printStackTrace();
            }
            System.out.println("t finished");
        });
        t.start();
        TimeUnit.SECONDS.sleep(1);
        t.stop();

        // 使用suspend/resume方法来结束线程,不推荐
        Thread t2 = new Thread(() -> {
            System.out.println("t2 start");
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                // e.printStackTrace();
            }
            System.out.println("t2 finished");
        });
        t2.start();
        TimeUnit.SECONDS.sleep(1);
        t2.suspend();
        TimeUnit.SECONDS.sleep(1);
        t2.resume();
    }
}
