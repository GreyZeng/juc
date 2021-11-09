package git.snippets.juc;


import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * ReentrantReadWriteLock读写锁示例
 **/
public class ReentrantLockReadAndWrite {

    private static final ReentrantReadWriteLock reentrantLock = new ReentrantReadWriteLock();
    private static final ReentrantReadWriteLock.ReadLock readLock = reentrantLock.readLock();
    private static final ReentrantReadWriteLock.WriteLock writeLock = reentrantLock.writeLock();

    public static void read() {
        readLock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + "获取读锁，开始执行");
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            readLock.unlock();
            System.out.println(Thread.currentThread().getName() + "释放读锁");
        }
    }

    public static void write() {
        writeLock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + "获取写锁，开始执行");
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            writeLock.unlock();
            System.out.println(Thread.currentThread().getName() + "释放写锁");
        }
    }

    public static void main(String[] args) {
        new Thread(ReentrantLockReadAndWrite::read, "Thread1").start();
        new Thread(ReentrantLockReadAndWrite::read, "Thread2").start();
        new Thread(ReentrantLockReadAndWrite::write, "Thread3").start();
        new Thread(ReentrantLockReadAndWrite::write, "Thread4").start();
    }
}