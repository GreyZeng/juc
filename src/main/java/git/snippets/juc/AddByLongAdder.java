package git.snippets.juc;

import java.util.concurrent.atomic.LongAdder;

/**
 * 对比AddByAdder, AddByAtomic, AddBySync几个程序，在数据量比较大的情况下，AddByAdder的效率最高
 *
 * @since 1.8
 */
public class AddByLongAdder {
    LongAdder count = new LongAdder();


    public static void main(String[] args) {
        Thread[] all = new Thread[1000];
        AddByLongAdder t = new AddByLongAdder();
        for (int i = 0; i < all.length; i++) {
            all[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < 1000000; j++) {
                        t.count.increment();
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
        System.out.println("by LongAdder , result is " + t.count.doubleValue() + " time is " + (end - start));

    }

}
