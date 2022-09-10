package git.snippets.juc;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 程序在执行过程中，如果出现异常，默认情况锁会被释放
 * 所以，在并发处理的过程中，有异常要多加小心，不然可能会发生不一致的情况。
 * 比如，在一个web app处理过程中，多个servlet线程共同访问同一个资源，这时如果异常处理不合适，
 * 在第一个线程中抛出异常，其他线程就会进入同步代码区，有可能会访问到异常产生时的数据。
 * 因此要非常小心的处理同步业务逻辑中的异常
 */
public class SynchronizedException implements Runnable {
    int count = 0;

    public static void main(String[] args) throws IOException {
        SynchronizedException myRun = new SynchronizedException();
        Thread thread = new Thread(myRun, "t1");
        Thread thread2 = new Thread(myRun, "t2");
        thread.start();
        thread2.start();
        System.in.read();

    }

    @Override
    public void run() {
        synchronized (this) {
            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("current thread is " + Thread.currentThread().getName() + " count is " + count);
                if (count == 5) {
                    count++;
                    // 遇到异常，synchronized 会自动释放锁
                    int m = 1 / 0;
                }
                count++;
            }
        }
    }

    synchronized void m1(String content) {
        System.out.println(this);
        System.out.println("m1 get content is " + content);
        m2(content);
    }

    synchronized void m2(String content) {
        System.out.println(this);
        System.out.println("m2 get content is " + content);

    }


}
