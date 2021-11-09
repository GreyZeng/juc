package git.snippets.juc;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * ReentrantLock的读锁插队策略
 */
public class ReentrantLockCut {
    private static final ReentrantReadWriteLock reentrantLock = new ReentrantReadWriteLock();
    private static final ReentrantReadWriteLock.ReadLock readLock = reentrantLock.readLock();
    private static final ReentrantReadWriteLock.WriteLock writeLock = reentrantLock.writeLock();

    public static void read() {
        System.out.println(Thread.currentThread().getName() + "开始尝试获取读锁");
        readLock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + "获取读锁，开始执行");
            Thread.sleep(20);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            readLock.unlock();
            System.out.println(Thread.currentThread().getName() + "释放读锁");
        }
    }

    public static void write() {
        System.out.println(Thread.currentThread().getName() + "开始尝试获取写锁");
        writeLock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + "获取写锁，开始执行");
            Thread.sleep(40);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println(Thread.currentThread().getName() + "释放写锁");
            writeLock.unlock();
        }
    }

    public static void main(String[] args) {
        new Thread(ReentrantLockCut::write, "Thread1").start();
        new Thread(ReentrantLockCut::read, "Thread2").start();
        new Thread(ReentrantLockCut::read, "Thread3").start();
        new Thread(ReentrantLockCut::write, "Thread4").start();
        new Thread(ReentrantLockCut::read, "Thread5").start();
        new Thread(() -> {
            Thread[] threads = new Thread[1000];
            for (int i = 0; i < 1000; i++) {
                threads[i] = new Thread(ReentrantLockCut::read, "子线程创建的Thread" + i);
            }
            for (int i = 0; i < 1000; i++) {
                threads[i].start();
            }
        }).start();
    }

}
