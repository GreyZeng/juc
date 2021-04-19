package git.snippets.juc;

/**
 * 对比AddByAdder, AddByAtomic, AddBySync几个程序，在数据量比较大的情况下，AddByAdder的效率最高
 */
public class AddBySynchronized {
    static long count = 0;


    public static void main(String[] args) {
        Thread[] all = new Thread[1000];
        AddBySynchronized t = new AddBySynchronized();
        Object o = new Object();
        for (int i = 0; i < all.length; i++) {
            all[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < 1000000; j++) {
                        synchronized (o) {
                            count++;
                        }
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
        System.out.println("by synchronized , result is " + count + " time is " + (end - start));

    }

}
