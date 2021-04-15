package git.snippets.juc;

/**
 * synchronized锁定静态方法，相当于锁定当前类
 *
 * @author <a href="mailto:410486047@qq.com">Grey</a>
 * @date 2021/4/15
 * @since
 */
public class SynchronizedStatic implements Runnable {
    static SynchronizedStatic instance = new SynchronizedStatic();
    static volatile int i = 0;

    @Override
    public void run() {
        increase();
    }

    // 相当于synchronized(SynchronizedStatic.class)
    synchronized static void increase() {
        for (int j = 0; j < 1000000; j++) {
            i++;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(instance);
        Thread t2 = new Thread(instance);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(i);
    }
}
