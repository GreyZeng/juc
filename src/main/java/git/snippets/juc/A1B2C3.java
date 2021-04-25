package git.snippets.juc;

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

    public static void main(String[] args) {
        useWaitNotify();
        useLockSupport();
    }

    /**
     * 使用wait和notify
     */
    public static void useWaitNotify() {
        System.out.println("use wait and notify");
        final Object o = new Object();
        new Thread(() -> {
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
        }).start();
        new Thread(() -> {
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
        }).start();
    }

    /**
     * 使用LockSupport
     */
    public static void useLockSupport() {
        System.out.println("use LockSupport...");
        // TODO
    }
}
