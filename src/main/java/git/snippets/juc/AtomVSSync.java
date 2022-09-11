package git.snippets.juc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 证明原子操作类比synchronized更高效
 *
 * @author <a href="mailto:410486047@qq.com">Grey</a>
 * @date 2021/4/26
 */
public class AtomVSSync {
    public static void main(String[] args) {
        test1();
    }

    AtomicInteger atomicCount = new AtomicInteger(0);
    int count = 0;
    final static int TIMES = 80000000;

    void m() {
        for (int i = 0; i < TIMES; i++) {
            atomicCount.incrementAndGet(); //原子操作
        }
    }

    void m2() {
        for (int i = 0; i < TIMES; i++) {
            synchronized (this) {
                count++;
            }
        }
    }


    public static void test1() {
        AtomVSSync t1 = new AtomVSSync();
        AtomVSSync t2 = new AtomVSSync();
        long time1 = time(t1::m);
        System.out.println("使用原子类得到的结果是：" + t1.atomicCount);
        long time2 = time(t2::m2);
        System.out.println("使用synchronized得到的结果是：" + t2.count);

        System.out.println("使用原子类花费的时间是：" + time1);
        System.out.println("使用 synchronized 花费的时间是 ：" + time2);
    }

    private static long time(Runnable runnable) {
        List<Thread> threads = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            threads.add(new Thread(runnable, "thread-" + i));
        }
        threads.forEach(Thread::start);
        threads.forEach(o -> {
            try {
                o.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }
}
