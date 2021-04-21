package git.snippets.juc;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * 阻塞指定线程，唤醒指定线程
 */
public class LockSupportUsage {
    public static void main(String[] args) {
        Thread t = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    if (i == 5) {
                        LockSupport.park();
                    }
                    if (i == 8) {
                        LockSupport.park();
                    }
                    TimeUnit.SECONDS.sleep(1);
                    System.out.println(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        // unpark可以先于park调用
        //LockSupport.unpark(t);
        try {
            TimeUnit.SECONDS.sleep(8);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        LockSupport.unpark(t);
        System.out.println("after 8 seconds");
    }
}
