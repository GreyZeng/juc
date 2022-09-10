package git.snippets.juc;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockAndSynchronized {
    ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
        ReentrantLockAndSynchronized t = new ReentrantLockAndSynchronized();
        new Thread(t::m).start();
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 由于前一个线程先执行m1，锁定this，所以只能等前一个线程执行完毕后才能执行下面线程的操作
        new Thread(t::m2).start();

    }

    void m() {
        lock.lock();
        try {
            for (int i = 0; i < 10; i++) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(i);
                if (i == 2) {
                    m2();
                }
            }
        } finally {
            lock.unlock();
        }
    }

    void m2() {
        lock.lock();
        try {
            System.out.println("start m2");
            int i = 1 / 0;
        } finally {
            // 如果不加这句unlock，程序会一直卡在这里
            lock.unlock();
        }

    }
}
