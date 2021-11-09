package git.snippets.juc;

/**
 * 并发编程的三大特性之：有序性
 *
 * @author <a href="mailto:410486047@qq.com">Grey</a>
 * @since 1.8
 */
public class DisOrder {
    private static int x = 0, y = 0;
    private static int a = 0, b = 0;

    // 以下程序可能会执行比较长的时间
    public static void main(String[] args) throws InterruptedException {
        int i = 0;
        for (; ; ) {
            i++;
            x = 0;
            y = 0;
            a = 0;
            b = 0;
            Thread one = new Thread(() -> {
                // 由于线程one先启动，下面这句话让它等一等线程two. 读着可根据自己电脑的实际性能适当调整等待时间.
                shortWait(100000);
                a = 1;
                x = b;
            });

            Thread other = new Thread(() -> {
                b = 1;
                y = a;
            });
            one.start();
            other.start();
            one.join();
            other.join();
            String result = "第" + i + "次 (" + x + "," + y + "）";
            if (x == 0 && y == 0) {
                // 出现这个分支，说明指令出现了重排
                // 否则不可能 x和y同时都为0
                System.err.println(result);
                break;
            } else {
                // System.out.println(result);
            }
        }
    }

    public static void shortWait(long interval) {
        long start = System.nanoTime();
        long end;
        do {
            end = System.nanoTime();
        } while (start + interval >= end);
    }
}
