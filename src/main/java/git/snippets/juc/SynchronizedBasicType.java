package git.snippets.juc;

/**
 * synchronized不能锁定String常量，Integer，Long等基础类型
 * @author <a href="mailto:410486047@qq.com">Grey</a>
 * @since
 */
public class SynchronizedBasicType implements Runnable {
    public static Integer i = 0;
    static SynchronizedBasicType instance = new SynchronizedBasicType();

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(instance);
        Thread t2 = new Thread(instance);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(i);
    }

    @Override
    public void run() {
        for (int j = 0; j < 10000000; j++) {
            synchronized (i) {
                i++;
            }
        }
    }
}
