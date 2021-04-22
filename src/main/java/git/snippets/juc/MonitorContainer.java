package git.snippets.juc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

// 实现一个容器，提供两个方法，add，size写两个线程，线程1添加10个元素到容器中，
// 线程2实现监控元素的个数，当个数到5个时，线程2给出提示并结束
public class MonitorContainer {


    public static void main(String[] args) {
        useNotifyAndWait();
    }

    private static void useNotifyAndWait() {
        List<Integer> list = Collections.synchronizedList(new ArrayList<>());
        final Object o = new Object();
        Thread adder = new Thread(() -> {
            synchronized (o) {
                for (int i = 0; i < 10; i++) {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    list.add(i);
                    System.out.println("list add the " + i + " element ");
                    if (list.size() == 5) {
                        o.notify();
                        try {
                            o.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                o.notify();
            }
        });
        Thread monitor = new Thread(() -> {
            synchronized (o) {
                while (true) {
                    if (list.size() == 5) {
                        System.out.println("filled 5 elements");
                    }
                    o.notify();
                    try {
                        o.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        });
        adder.start();
        monitor.start();
    }
}
