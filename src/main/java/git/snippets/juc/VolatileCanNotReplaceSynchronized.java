package git.snippets.juc;

import java.util.ArrayList;
import java.util.List;

/**
 * volatile并不能保证多个线程共同修改变量时所带来的不一致问题，也就是说volatile不能替代synchronized
 *
 * @author <a href="mailto:410486047@qq.com">Grey</a>
 * @date 2021/4/19
 * @since
 */
public class VolatileCanNotReplaceSynchronized {
    volatile int count = 0;
    int count2 = 0;

    public static void main(String[] args) {
        VolatileCanNotReplaceSynchronized t = new VolatileCanNotReplaceSynchronized();
        List<Thread> threads = new ArrayList<>();
        List<Thread> threads2 = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            threads.add(new Thread(t::m));
            threads2.add(new Thread(t::m2));
        }
        threads.forEach(item -> item.start());
        threads2.forEach(item -> item.start());
        threads.forEach(item -> {
            try {
                item.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        threads2.forEach(item -> {
            try {
                item.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println(t.count);
        System.out.println(t.count2);
    }

    void m() {
        for (int i = 0; i < 1000; i++) {
            count++;
        }
    }

    synchronized void m2() {
        for (int i = 0; i < 1000; i++) {
            count2++;
        }
    }
}
