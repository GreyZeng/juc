package git.snippets.juc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author <a href="mailto:410486047@qq.com">Grey</a>
 * @date 2021/4/26
 * @since
 */
public class AtomVSSync {
    public static void main(String[] args) {
        test1();
        test2();
        test3();
    }


    AtomicInteger atomicCount = new AtomicInteger(0);
    int count = 0;

    void m() {
        for (int i = 0; i < 1000000; i++) {
            atomicCount.incrementAndGet(); //原子操作
        }
    }

    void m2() {
        for (int i = 0; i < 1000000; i++) {
            synchronized (this) {
                count++;
            }
        }
    }

    //证明原子操作类比synchronized更高效
    public static void test1() {
        AtomVSSync t1 = new AtomVSSync();
        AtomVSSync t2 = new AtomVSSync();
        long time1 = time(t1::m);
        System.out.println(t1.atomicCount);
        long time2 = time(t2::m2);
        System.out.println(t2.count);

        System.out.println(time1);
        System.out.println(time2);
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

    //AtomXXX类可以保证可见性吗？请写一个程序来证明
    AtomicBoolean running = new AtomicBoolean(true);

    void m3() {
        System.out.println("m1 start");
        while (running.get()) {  //死循环。只有running=false时，才能执行后面的语句

        }
        System.out.println("m2 end");
    }

    public static void test2() {
        AtomVSSync t = new AtomVSSync();
        new Thread(t::m3, "t1").start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t.running.getAndSet(false);
    }


    AtomicInteger count2 = new AtomicInteger(0);

    void m4() {
        for (int i = 0; i < 10000; i++) {
            if (count2.get() < 999 && count2.get() >= 0) { //如果未加锁,之间还会有其他线程插进来
                count2.incrementAndGet();
            }
        }
    }

    //写一个程序证明AtomXXX类的多个方法并不构成原子性
    public static void test3() {
        AtomVSSync t = new AtomVSSync();
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            threads.add(new Thread(t::m4, "thread" + i));
        }
        threads.forEach(Thread::start);
        threads.forEach((o) -> {
            try {
                //join()方法阻塞调用此方法的线程,直到线程t完成，此线程再继续。通常用于在main()主线程内，等待其它线程完成再结束main()主线程。
                o.join(); //相当于在main线程中同步o线程，o执行完了，main线程才有执行的机会
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println(t.count2);
    }


}
