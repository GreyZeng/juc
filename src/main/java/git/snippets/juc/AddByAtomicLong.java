package git.snippets.juc;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 对比AddByAdder, AddByAtomic, AddBySync几个程序，在数据量比较大的情况下，AddByAdder的效率最高
 */
public class AddByAtomicLong {
    AtomicLong count = new AtomicLong(0);


    public static void main(String[] args) {
        Thread[] all = new Thread[1000];
        AddByAtomicLong t = new AddByAtomicLong();
        for (int i = 0; i < all.length; i++) {
            all[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < 1000000; j++) {
                        t.count.incrementAndGet();
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
        System.out.println("by AtomicLong , result is " + t.count.get() + " time is " + (end - start));

    }

}
