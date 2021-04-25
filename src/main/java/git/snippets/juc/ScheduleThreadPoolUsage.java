package git.snippets.juc;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.SECONDS;
//- scheduleAtFixedRate
//
//        > 当前任务执行时间小于间隔时间，每次到点即执行；
//        > 当前任务执行时间大于等于间隔时间，任务执行后立即执行下一次任务。相当于连续执行了。
//
// - scheduleWithFixedDelay
//
//        > 每当上次任务执行完毕后，间隔一段时间执行。不管当前任务执行时间大于、等于还是小于间隔时间，执行效果都是一样的。

public class ScheduleThreadPoolUsage {
    static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);


    public static void main(String[] args) {
        test1();
        test2();
        test3();
    }

    /**
     * 任务执行时间（8s）小于间隔时间(10s)
     */
    public static void test1() {
        scheduler.scheduleAtFixedRate(() -> {
            System.out.println("Start: scheduleAtFixedRate:    " + new Date());
            try {
                Thread.sleep(8000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("End  : scheduleAtFixedRate:    " + new Date());
        }, 0, 10, SECONDS);
    }

    /**
     * 任务执行时间（12s）大于间隔时间(10s)
     */
    public static void test2() {
        scheduler.scheduleAtFixedRate(() -> {
            System.out.println("Start: scheduleAtFixedRate:    " + new Date());
            try {
                Thread.sleep(12000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("End  : scheduleAtFixedRate:    " + new Date());
        }, 0, 10, SECONDS);
    }

    /**
     * 任务执行时间（8s）小于间隔时间(10s)
     */
    public static void test3() {
        scheduler.scheduleWithFixedDelay(() -> {
            System.out.println("Start: scheduleWithFixedDelay: " + new Date());
            try {
                Thread.sleep(12000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("End  : scheduleWithFixedDelay: " + new Date());
        }, 0, 10, SECONDS);
    }
}
