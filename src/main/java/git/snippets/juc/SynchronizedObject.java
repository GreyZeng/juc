package git.snippets.juc;

/**
 * synchronized锁定对象
 *
 * @author <a href="mailto:410486047@qq.com">Grey</a>
 * @date 2021/4/15
 * @since
 */
public class SynchronizedObject implements Runnable {
    static SynchronizedObject instance = new SynchronizedObject();
    final Object object = new Object();
    static volatile int i = 0;

    @Override
    public void run() {
        for (int j = 0; j < 1000000; j++) {
            // 任何线程要执行下面的代码，必须先拿到object的锁
            synchronized (object) {
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
