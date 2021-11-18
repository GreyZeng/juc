package git.snippets.juc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

// 实现一个容器，提供两个方法，add，size写两个线程，线程1添加10个元素到容器中，
// 线程2实现监控元素的个数，当个数到5个时，线程2给出提示并结束
public class MonitorContainer {

    public static void main(String[] args) {

        useLockSupport();
        // useCountDownLatch();
        // useNotifyAndWait();
    }


    /**
     * 使用LockSupport
     */
    private static void useLockSupport() {
        System.out.println("use LockSupport...");
        Thread adder;
        List<Object> list = Collections.synchronizedList(new ArrayList<>());
        Thread finalMonitor = new Thread(() -> {
            LockSupport.park();
            if (match(list)) {
                System.out.println("filled 5 elements size is " + list.size());
                LockSupport.unpark(null);
            }
        });
        adder = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                increment(list);
                if (match(list)) {
                    LockSupport.unpark(finalMonitor);
                }
            }
        });
        adder.start();
        finalMonitor.start();
    }

    /**
     * 使用CountDownLatch
     */
    private static void useCountDownLatch() {
        System.out.println("use CountDownLatch...");
        List<Object> list = Collections.synchronizedList(new ArrayList<>());
        CountDownLatch latch = new CountDownLatch(5);
        Thread adder = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                increment(list);
                if (i <= 4) {
                    latch.countDown();
                }
            }
        });
        Thread monitor = new Thread(() -> {
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (match(list)) {
                System.out.println("filled 5 elements");
            }
        });
        adder.start();
        monitor.start();
    }

    /**
     * notify + wait 实现
     */
    private static void useNotifyAndWait() {
        System.out.println("use notify and wait...");
        List<Object> list = Collections.synchronizedList(new ArrayList<>());
        final Object o = new Object();
        Thread adder = new Thread(() -> {
            synchronized (o) {
                for (int i = 0; i < 10; i++) {
                    increment(list);
                    if (match(list)) {
                        o.notify();
                        try {
                            o.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                System.out.println("add finished");
                o.notify();
            }
        });
        Thread monitor = new Thread(() -> {
            synchronized (o) {
                if (match(list)) {
                    System.out.println("5 elements added " + list.size());
                    o.notify();
                    try {
                        o.wait();
                        System.out.println("monitor finished");
                        o.notify();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        adder.start();
        monitor.start();
    }

    /**
     * 只要是5的倍数，就循环打印
     */
    private static void useNotifyAndWaitLoop() {
        List<Object> list = Collections.synchronizedList(new ArrayList<>());
        final Object o = new Object();
        Thread adder = new Thread(() -> {
            synchronized (o) {
                for (; ; ) {
                    increment(list);
                    if (match(list)) {
                        o.notify();
                        try {
                            o.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        Thread monitor = new Thread(() -> {
            synchronized (o) {
                while (true) {
                    if (match(list)) {
                        System.out.println("filled 5 elements");
                    }
                    o.notify();
                    try {
                        o.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        adder.start();
        monitor.start();
    }

    private static void increment(List<Object> list) {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        list.add(new Object());
        System.out.println("list add the ele, size is " + list.size());
    }

    private static boolean match(List<Object> list) {
        return list.size() % 5 == 0;
    }
}
