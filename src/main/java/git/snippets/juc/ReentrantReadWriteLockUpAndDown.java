package git.snippets.juc;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 读锁无法升级为写锁
 * 写锁可以降级成读锁
 *
 * @author <a href="mailto:410486047@qq.com">Grey</a>
 * @date 2021/4/21
 * @since
 */
public class ReentrantReadWriteLockUpAndDown {
    private static final ReentrantReadWriteLock reentrantLock = new ReentrantReadWriteLock();
    private static final ReentrantReadWriteLock.ReadLock readLock = reentrantLock.readLock();
    private static final ReentrantReadWriteLock.WriteLock writeLock = reentrantLock.writeLock();

    public static void read() {
        System.out.println(Thread.currentThread().getName() + "开始尝试获取读锁");
        readLock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + "获取读锁，开始执行");
            Thread.sleep(20);
            System.out.println(Thread.currentThread().getName() + "尝试升级读锁为写锁");
            //读锁升级为写锁(失败)
            writeLock.lock();
            System.out.println(Thread.currentThread().getName() + "读锁升级为写锁成功");
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
            System.out.println(Thread.currentThread().getName() + "尝试降级写锁为读锁");
            //写锁降级为读锁（成功）
            readLock.lock();
            System.out.println(Thread.currentThread().getName() + "写锁降级为读锁成功");
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println(Thread.currentThread().getName() + "释放写锁");
            writeLock.unlock();
            readLock.unlock();
        }
    }

    public static void main(String[] args) {
        new Thread(ReentrantReadWriteLockUpAndDown::write, "Thread1").start();
        new Thread(ReentrantReadWriteLockUpAndDown::read, "Thread2").start();
    }
}
