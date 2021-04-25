package git.snippets.juc;

import java.util.concurrent.locks.LockSupport;

/**
 * 要求线程打印A1B2C3
 *
 * @author <a href="mailto:410486047@qq.com">Grey</a>
 * @date 2021/4/25
 * @since
 */
public class A1B2C3 {
    private static final char[] a = {'A', 'B', 'C', 'D', 'E'};
    private static final char[] b = {'1', '2', '3', '4', '5'};

    static Thread t1;
    static Thread t2;

    public static void main(String[] args) {
        useVolatile();
        useWaitNotify();
        useLockSupport();

    }

    volatile static boolean flag = false;

    /**
     * 使用volatile
     */
    public static void useVolatile() {
        System.out.println("use volatile...");
        t1 = new Thread(() -> {
            for (int i = 0; i < a.length; i++) {
                while (flag) {
                }
                System.out.print(a[i]);
                flag = !flag;
            }
        });
        t2 = new Thread(() -> {
            for (int i = 0; i < b.length; i++) {
                while (!flag) {
                }
                System.out.print(b[i]);
                flag = !flag;
            }
        });
        t1.start();
        t2.start();
        join();
    }

    /**
     * 使用wait和notify
     */
    public static void useWaitNotify() {
        System.out.println("use wait and notify");
        final Object o = new Object();
        t1 = new Thread(() -> {
            synchronized (o) {
                for (int i = 0; i < a.length; i++) {
                    System.out.print(a[i]);
                    o.notify();
                    try {
                        o.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                o.notify();
            }
        });
        t1.start();
        t2 = new Thread(() -> {
            synchronized (o) {
                for (int i = 0; i < b.length; i++) {
                    System.out.print(b[i]);
                    o.notify();
                    try {
                        o.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                o.notify();
            }
        });
        t2.start();

        join();
    }

    public static void join() {
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println();
    }

    /**
     * 使用LockSupport
     */
    public static void useLockSupport() {
        System.out.println("use LockSupport...");
        t1 = new Thread(() -> {
            for (int i = 0; i < a.length; i++) {
                System.out.print(a[i]);
                LockSupport.unpark(t2);
                LockSupport.park();
            }
            LockSupport.unpark(t2);
        });
        t1.start();
        t2 = new Thread(() -> {
            for (int i = 0; i < b.length; i++) {
                System.out.print(b[i]);
                LockSupport.unpark(t1);
                LockSupport.park();
            }
            LockSupport.unpark(t1);
        });
        t2.start();
        join();
    }
}
