package git.snippets.juc;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock; 

public class ReentrantLockInterrupt {
	static ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            lock.lock();
            try {
                System.out.println("a thread started and sleep forever");
                SECONDS.sleep(Integer.MAX_VALUE);
                System.out.println("a thread stopped");
            } catch (InterruptedException e) {
                System.out.println("the thread has been interrupted");
            } finally {
                lock.unlock();
            }
        });
        t1.start();
        Thread t2 = new Thread(() -> {
            try {
                lock.lockInterruptibly();
                System.out.println("if lock thread is interrupted, it will run");
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                System.out.println("interrupted");
            } finally {
                lock.unlock();
            }
        });
        t2.start();
        try {
            SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t2.interrupt();

    }
}

