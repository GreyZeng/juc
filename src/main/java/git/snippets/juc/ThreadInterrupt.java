package git.snippets.juc;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * interrupt示例
 *
 * @author <a href="mailto:410486047@qq.com">Grey</a>
 * @since 1.8
 */
public class ThreadInterrupt {
    private static final ReentrantLock LOCK = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(() -> {
            for (; ; ) {
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("t thread interrupted");
                    System.out.println(Thread.currentThread().isInterrupted());
                    break;
                }
            }
        });
        t.start();
        TimeUnit.SECONDS.sleep(3);
        t.interrupt();

        Thread t2 = new Thread(() -> {
            for (; ; ) {
                if (Thread.interrupted()) {
                    System.out.println("t2 thread interrupted");
                    // Thread.interrupted()会将线程中断状态置为false
                    System.out.println(Thread.currentThread().isInterrupted());
                    break;
                }
            }
        });
        t2.start();
        TimeUnit.SECONDS.sleep(3);
        t2.interrupt();

        Thread t3 = new Thread(() -> {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                System.out.println("t3 interrupted");
                // 如果不加上这一句，那么Thread.currentThread().isInterrupted()将会都是false，因为在捕捉到InterruptedException异常的时候就会自动的中断标志置为了false
                Thread.currentThread().interrupt();
                System.out.println(Thread.currentThread().isInterrupted());
            }
        });

        t3.start();
        TimeUnit.SECONDS.sleep(3);
        t3.interrupt();

        final Object o = new Object();
        Thread t4 = new Thread(() -> {
            synchronized (o) {
                try {
                    o.wait();
                } catch (InterruptedException e) {
                    System.out.println("t4 interrupted!");
                    Thread.currentThread().interrupt();
                    System.out.println(Thread.currentThread().isInterrupted());
                }
            }
        });
        t4.start();
        TimeUnit.SECONDS.sleep(10);
        t4.interrupt();

        Thread t5 = new Thread(() -> {
            synchronized (o) {
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t5.start();
        TimeUnit.SECONDS.sleep(1);
        Thread t6 = new Thread(() -> {
            synchronized (o) {

            }
            System.out.println("t6 finished");
        });
        t6.start();
        t6.interrupt();


        Thread t7 = new Thread(() -> {
            LOCK.lock();
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                LOCK.unlock();
            }
            System.out.println("t7 end");
        });
        t7.start();
        TimeUnit.SECONDS.sleep(1);
        Thread t8 = new Thread(() -> {
            LOCK.lock();
            try {
            } finally {
                LOCK.unlock();
            }
            System.out.println("t8 end");
        });
        t8.start();
        TimeUnit.SECONDS.sleep(1);
        t8.interrupt();

        Thread t9 = new Thread(() -> {
            LOCK.lock();
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                LOCK.unlock();
            }
            System.out.println("t7 end");
        });
        t9.start();
        TimeUnit.SECONDS.sleep(1);
        Thread t10 = new Thread(() -> {
            System.out.println("t10 start");
            try {
                LOCK.lockInterruptibly();
            } catch (InterruptedException e) {
                System.out.println("t10 interrupted");
            } finally {
                LOCK.unlock();
            }
            System.out.println("t8 end");
        });
        t10.start();
        TimeUnit.SECONDS.sleep(1);
        t10.interrupt();

    }
}
