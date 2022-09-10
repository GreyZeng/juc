package git.snippets.juc;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockFair extends Thread {
    static ReentrantLock lock = new ReentrantLock(true/*false*/);

    public static void main(String[] args) {
        ReentrantLockFair tl = new ReentrantLockFair();
        Thread t1 = new Thread(tl);
        Thread t2 = new Thread(tl);
        t1.start();
        t2.start();
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            lock.lock();
            try {
                System.out.println("current thread :" + Thread.currentThread().getName() + " get the lock");
                SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }

        }
    }
}
