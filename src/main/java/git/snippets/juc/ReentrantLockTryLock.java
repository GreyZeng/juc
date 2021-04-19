package git.snippets.juc;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.concurrent.locks.ReentrantLock; 

public class ReentrantLockTryLock {
	ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
    	ReentrantLockTryLock t = new ReentrantLockTryLock();
        new Thread(t::m).start();
        try {
            SECONDS.sleep(2);
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
                    SECONDS.sleep(3);
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
        boolean locked = false;
        try {
            locked = lock.tryLock(1, SECONDS);
            if (locked) {
                System.out.println("get lock");
                System.out.println("start m2");
            } else {
                System.out.println("not get m2");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (locked) {
                lock.unlock();
            }
        }

    }
}

