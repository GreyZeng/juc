package git.snippets.juc;

/**
 * synchronized锁定方法
 *
 * @author <a href="mailto:410486047@qq.com">Grey</a>
 * @date 2021/4/15
 * @since
 */
public class SynchronizedMethod implements Runnable {
    static SynchronizedMethod instance = new SynchronizedMethod();
    static volatile int i = 0;

    @Override
    public void run() {
        increase();
    }
    void increase() {
        for (int j = 0; j < 1000000; j++) {
            synchronized (this) {
                i++;
            }
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
