package git.snippets.juc;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * 对比AddByAdder, AddByAtomic, AddBySync几个程序，在数据量比较大的情况下，AddByAdder的效率最高
 */
public class AddWays {
    public static void main(String[] args) {
        addBySync();
        addByAtomicLong();
        addByLongAdder();
    }

    // 使用AtomicLong
    public static void addByAtomicLong() {
        AtomicLong count = new AtomicLong(0);
        Thread[] all = new Thread[1000];
        AddWays t = new AddWays();
        for (int i = 0; i < all.length; i++) {
            all[i] = new Thread(() -> {
                for (int j = 0; j < 1000000; j++) {
                    count.incrementAndGet();
                }
            });
        }
        long start = System.currentTimeMillis();
        for (Thread thread : all) {
            thread.start();
        }
        for (Thread thread : all) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("result is " + count.get() + " time is " + (end - start) + "ms (by AtomicLong)");

    }

    // 使用LongAdder
    public static void addByLongAdder() {
        Thread[] all = new Thread[1000];
        LongAdder count = new LongAdder();
        for (int i = 0; i < all.length; i++) {
            all[i] = new Thread(() -> {
                for (int j = 0; j < 1000000; j++) {
                    count.increment();
                }
            });
        }
        long start = System.currentTimeMillis();
        for (Thread thread : all) {
            thread.start();
        }
        for (Thread thread : all) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("result is " + count + " time is " + (end - start) + "ms (by LongAdder)");

    }

    static long count = 0;

    public static void addBySync() {


        Thread[] all = new Thread[1000];
        Object o = new Object();
        for (int i = 0; i < all.length; i++) {
            all[i] = new Thread(() -> {
                for (int j = 0; j < 1000000; j++) {
                    synchronized (o) {
                        count++;
                    }
                }
            });
        }
        long start = System.currentTimeMillis();
        for (Thread thread : all) {
            thread.start();
        }
        for (Thread thread : all) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("result is " + count + " time is " + (end - start) + "ms (by synchronized)");

    }
}
