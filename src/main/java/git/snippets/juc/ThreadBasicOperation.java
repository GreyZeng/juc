package git.snippets.juc;

import java.util.concurrent.TimeUnit;

/**
 * 线程的基本操作（yield, join, sleep）
 * @author <a href="mailto:410486047@qq.com">Grey</a>
 * @since 1.8
 */
public class ThreadBasicOperation {
    static volatile int sum = 0;

    public static void main(String[] args) throws Exception {
        // join方法
        Thread t = new Thread(()->{
            for (int i = 1; i <= 100; i++) {
                sum += i;
            }
        });
        t.start();
        // join 方法表示主线程愿意等待子线程执行完毕后才继续执行
        // 如果不使用join方法，那么sum输出的可能是一个很小的值，因为还没等子线程
        // 执行完毕后，主线程就已经执行了打印sum的操作
        t.join();
        System.out.println(sum);

        // sleep方法
        Thread t2 = new Thread(() -> {
            System.out.println("t2 started");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) { 
                e.printStackTrace();
            }
            System.out.println("t2 end");
        });

        t2.start();
        t2.join();

        // yield 操作
        // yield以后，也有可能自己抢到下一次的运行时间
         new Thread(()->{
            for (int i = 0; i < 10; i++) {
                if (i % 2 == 0) {
                    Thread.yield();
                }
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(i);
            }
        }).start();
        new Thread(()->{
            for (int i = 10; i < 20; i++) {
                if (i % 3 == 0) {
                    Thread.yield();
                }
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) { 
                    e.printStackTrace();
                }
                System.out.println(i);
            }
        }).start();

    }
}
